package com.bitsclassmgmt.projectservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bitsclassmgmt.projectservice.client.ClassesServiceClient;
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
import com.bitsclassmgmt.projectservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.TaskCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ProjectRepository projectRepository;
    private final SubProjectRepository subProjectRepository;
    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceclient;
    private final ClassesServiceClient classesServiceclient;
    private final ModelMapper modelMapper;

    public Task createTask(TaskCreateRequest request) {
        // Fetch the project entity using projectId
        Project projectEntity = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + request.getProjectId()));

        // If subprojectId is provided, fetch the subproject entity
        SubProject subProjectEntity = null;
        if (request.getSubProjectId() != null) {
            subProjectEntity = subProjectRepository.findById(request.getSubProjectId())
                    .orElseThrow(() -> new RuntimeException("SubProject not found with ID: " + request.getSubProjectId()));
        }
        
        String userId = getUserById(request.getAssignedTo()).getId();
        
        // Create and save the task
        Task toSave = Task.builder()
                .project(projectEntity)
                .subProject(subProjectEntity) // Can be null if task is directly under a project
                .assignedTo(userId) // User ID from Auth Service
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .status(TaskStatus.PENDING) // Default status
                .build();

        return taskRepository.save(toSave);
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
        return Optional.ofNullable(classesServiceclient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


    public Task updatTaskById(ClassesUpdateRequest request) {
    	Task toUpdate = findTaskById(request.getId());
        modelMapper.map(request, toUpdate);


        return taskRepository.save(toUpdate);
    }

    public void deleteTaskById(String id) {
    	taskRepository.deleteById(id);
    }

    protected Task findTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }
}
