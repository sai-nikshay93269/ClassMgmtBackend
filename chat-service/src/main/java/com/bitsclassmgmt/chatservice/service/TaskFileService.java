package com.bitsclassmgmt.chatservice.service;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.time.LocalDateTime;
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
import com.bitsclassmgmt.chatservice.model.Task;
import com.bitsclassmgmt.chatservice.model.TaskFile;
import com.bitsclassmgmt.chatservice.repository.ChatRepository;
import com.bitsclassmgmt.chatservice.repository.TaskFileRepository;
import com.bitsclassmgmt.chatservice.repository.TaskRepository;
import com.bitsclassmgmt.chatservice.request.chat.ClassesUpdateRequest;
import com.bitsclassmgmt.chatservice.request.chat.TaskFileCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskFileService {
    private final ChatRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UserServiceClient userServiceclient;
    private final ClassesServiceClient classesServiceclient;
    private final ModelMapper modelMapper;

    public TaskFile createTaskFile(TaskFileCreateRequest request) {
        // Fetch the task entity using taskId
        Task taskEntity = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + request.getTaskId()));

        // Fetch the file metadata from the File Storage Service
        FileMetadata fileMetadata = fileStorageService.getFileById(request.getFileId())
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + request.getFileId()));

        // Create and save the TaskFile entry
        TaskFile taskFile = TaskFile.builder()
                .task(taskEntity)
                .fileId(fileMetadata.getId()) // Storing file ID from File Storage Service
                .uploadedAt(LocalDateTime.now()) // Capture upload timestamp
                .build();

        return taskFileRepository.save(taskFile);
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
