package com.bitsclassmgmt.projectservice.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bitsclassmgmt.projectservice.client.ClassGroupServiceClient;
import com.bitsclassmgmt.projectservice.client.UserServiceClient;
import com.bitsclassmgmt.projectservice.dto.ClassesDto;
import com.bitsclassmgmt.projectservice.dto.UserDto;
import com.bitsclassmgmt.projectservice.enums.TaskStatus;
import com.bitsclassmgmt.projectservice.exc.NotFoundException;
import com.bitsclassmgmt.projectservice.model.Project;
import com.bitsclassmgmt.projectservice.model.SubProject;
import com.bitsclassmgmt.projectservice.model.Task;
import com.bitsclassmgmt.projectservice.repository.ProjectRepository;
import com.bitsclassmgmt.projectservice.repository.SubProjectRepository;
import com.bitsclassmgmt.projectservice.repository.TaskRepository;
import com.bitsclassmgmt.projectservice.request.notification.SendNotificationRequest;
import com.bitsclassmgmt.projectservice.request.project.ProjectUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.TaskCreateRequest;
import com.bitsclassmgmt.projectservice.request.project.TaskUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ProjectRepository projectRepository;
    private final SubProjectRepository subProjectRepository;
    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceclient;
    private final ClassGroupServiceClient classGroupServiceclient;   
    private final ModelMapper modelMapper;
    private final NewTopic topic;
    
    private final KafkaTemplate<String, SendNotificationRequest> kafkaTemplate;
    public Task createTask(TaskCreateRequest request) {
        if (request.getProjectId() == null && request.getSubProjectId() == null) {
            throw new RuntimeException("Project ID or SubProject ID is required");
        }

        Project projectEntity = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + request.getProjectId()));

        SubProject subProjectEntity = null;
        if (request.getSubProjectId() != null) {
            subProjectEntity = subProjectRepository.findById(request.getSubProjectId())
                    .orElseThrow(() -> new RuntimeException("SubProject not found with ID: " + request.getSubProjectId()));
        }

        String userId = getUserById(request.getAssignedTo()).getId();

        Task toSave = Task.builder()
                .project(projectEntity)
                .subProject(subProjectEntity)
                .assignedTo(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .status(TaskStatus.OPEN)
                .build();

        Task savedTask = taskRepository.save(toSave);

        // 📨 Build and send notification
        String message = String.format(
                "🆕 *New Task Assigned*\n\n" +
                "🔹 Task: *%s* (ID: `%s`)\n" +
                "📚 Class ID: `%s`\n" +
                "📁 Project: *%s*\n%s" +
                "📅 Due Date: %s\n" +
                "📌 Status: *%s*",
                savedTask.getTitle(),
                savedTask.getId(),
                projectEntity.getClassId(),
                projectEntity.getTitle(),
                subProjectEntity != null ? String.format("📂 Subproject: *%s*\n", subProjectEntity.getTitle()) : "",
                savedTask.getDueDate() != null ? savedTask.getDueDate().toString() : "No due date",
                savedTask.getStatus().name()
        );

        SendNotificationRequest notification = SendNotificationRequest.builder()
                .userId(userId)
                .classId(projectEntity.getClassId())
                .message(message)
                .build();

        kafkaTemplate.send(topic.name(), notification);

        return savedTask;
    }



    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Task getTaskById(String id) {
        return findTaskById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public ClassesDto getClassById(String id) {
        return Optional.ofNullable(classGroupServiceclient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


//    public Task updateTaskById(TaskUpdateRequest request) {
//        Task existingTask = taskRepository.findById(request.getId())
//                .orElseThrow(() -> new NotFoundException("Task not found with ID: " + request.getId()));
//
//        // Map fields from the request to the existing task
//        existingTask.setTitle(request.getTitle());
//        existingTask.setDescription(request.getDescription());
//        existingTask.setAssignedTo(request.getAssignedTo());
//        existingTask.setDueDate(request.getDueDate());
//        existingTask.setStatus(TaskStatus.valueOf(request.getStatus()));
//
//        return taskRepository.save(existingTask);
//    }
    public Task updateTaskById(TaskUpdateRequest request) {
        Task existingTask = taskRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Task not found with ID: " + request.getId()));

        StringBuilder changeDetails = new StringBuilder();
        boolean changed = false;

        if (!Objects.equals(existingTask.getTitle(), request.getTitle())) {
            changeDetails.append(String.format("• Title: '%s' ➜ '%s'\n", existingTask.getTitle(), request.getTitle()));
            existingTask.setTitle(request.getTitle());
            changed = true;
        }

        if (!Objects.equals(existingTask.getDescription(), request.getDescription())) {
            changeDetails.append("• Description updated\n");
            existingTask.setDescription(request.getDescription());
            changed = true;
        }

        if (!Objects.equals(existingTask.getAssignedTo(), request.getAssignedTo())) {
            changeDetails.append(String.format("• Assigned to: '%s' ➜ '%s'\n", existingTask.getAssignedTo(), request.getAssignedTo()));
            existingTask.setAssignedTo(request.getAssignedTo());
            changed = true;
        }

        if (!Objects.equals(existingTask.getDueDate(), request.getDueDate())) {
            changeDetails.append(String.format("• Due Date: ➜ %s\n", request.getDueDate()));
            existingTask.setDueDate(request.getDueDate());
            changed = true;
        }

        TaskStatus newStatus = TaskStatus.valueOf(request.getStatus());
        if (existingTask.getStatus() != newStatus) {
            changeDetails.append(String.format("• Status: %s ➜ %s\n", existingTask.getStatus(), newStatus));
            existingTask.setStatus(newStatus);
            changed = true;
        }

        if (!changed) return existingTask;

        Task savedTask = taskRepository.save(existingTask);

        String message = String.format(
                "🛠️ *Task Updated*\n\n" +
                "🔹 Task: *%s* (ID: `%s`)\n" +
                "📚 Class ID: `%s`\n\n" +
                "Changes:\n%s",
                savedTask.getTitle(),
                savedTask.getId(),
                savedTask.getProject().getClassId(),
                changeDetails.toString().trim()
        );

        SendNotificationRequest notification = SendNotificationRequest.builder()
                .userId(savedTask.getAssignedTo())
                .classId(savedTask.getProject().getClassId())
                .message(message)
                .build();

        kafkaTemplate.send(topic.name(), notification);

        return savedTask;
    }

    public void deleteTaskById(String id) {
    	taskRepository.deleteById(id);
    }

    protected Task findTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }
    public List<Task> getTasksByProjectId(String projectId) {
        return taskRepository.findByProjectId(projectId); // Assuming the repository method is defined
    }
    public List<Task> getTasksBySubProjectId(String subProjectId) {
        return taskRepository.findBySubProjectId(subProjectId); // Assuming the repository method is defined
    }
}
