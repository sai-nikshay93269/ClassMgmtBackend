package com.bitsclassmgmt.chatservice.service;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.chatservice.client.ClassesServiceClient;
import com.bitsclassmgmt.chatservice.client.UserServiceClient;
import com.bitsclassmgmt.chatservice.dto.ClassesDto;
import com.bitsclassmgmt.chatservice.dto.UserDto;
import com.bitsclassmgmt.chatservice.enums.TaskStatus;
import com.bitsclassmgmt.chatservice.exc.NotFoundException;
import com.bitsclassmgmt.chatservice.model.Chat;
import com.bitsclassmgmt.chatservice.model.SubProject;
import com.bitsclassmgmt.chatservice.model.Task;
import com.bitsclassmgmt.chatservice.repository.ChatRepository;
import com.bitsclassmgmt.chatservice.repository.TaskRepository;
import com.bitsclassmgmt.chatservice.request.chat.ClassesUpdateRequest;
import com.bitsclassmgmt.chatservice.request.chat.TaskCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ChatRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceclient;
    private final ClassesServiceClient classesServiceclient;
    private final ModelMapper modelMapper;

    public Task createTask(TaskCreateRequest request) {
        // Fetch the project entity using projectId
        Chat projectEntity = projectRepository.findById(request.getProjectId())
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


    public List<Chat> getAll() {
        return projectRepository.findAll();
    }

    public Chat getAdvertById(String id) {
        return findAdvertById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public ClassesDto getClassById(String id) {
        return Optional.ofNullable(classesServiceclient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


    public Chat updateAdvertById(ClassesUpdateRequest request, MultipartFile file) {
        Chat toUpdate = findAdvertById(request.getId());
        modelMapper.map(request, toUpdate);


        return projectRepository.save(toUpdate);
    }

    public void deleteAdvertById(String id) {
    	projectRepository.deleteById(id);
    }

    public boolean authorizeCheck(String id, String principal) {
        return getUserById(getAdvertById(id).getUserId()).getUsername().equals(principal);
    }

    protected Chat findAdvertById(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advert not found"));
    }
}
