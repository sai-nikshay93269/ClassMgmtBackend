package com.bitsclassmgmt.projectservice.service;

import java.util.List;
import java.util.Optional;

import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bitsclassmgmt.projectservice.client.ClassGroupServiceClient;
import com.bitsclassmgmt.projectservice.client.FileMetadataClient;
import com.bitsclassmgmt.projectservice.client.UserServiceClient;
import com.bitsclassmgmt.projectservice.dto.ClassesDto;
import com.bitsclassmgmt.projectservice.dto.FileMetadataDto;
import com.bitsclassmgmt.projectservice.dto.UserDto;
import com.bitsclassmgmt.projectservice.exc.NotFoundException;
import com.bitsclassmgmt.projectservice.model.Task;
import com.bitsclassmgmt.projectservice.model.TaskFile;
import com.bitsclassmgmt.projectservice.repository.ProjectRepository;
import com.bitsclassmgmt.projectservice.repository.TaskFileRepository;
import com.bitsclassmgmt.projectservice.repository.TaskRepository;
import com.bitsclassmgmt.projectservice.request.notification.SendNotificationRequest;
import com.bitsclassmgmt.projectservice.request.project.ProjectUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.TaskFileCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskFileService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UserServiceClient userServiceclient;
    private final ClassGroupServiceClient classGroupServiceclient;
    private final FileMetadataClient fileStorageClient;
    private final ModelMapper modelMapper;
    
    private final KafkaTemplate<String, SendNotificationRequest> kafkaTemplate;
    private final NewTopic topic;

    public TaskFile createTaskFile(TaskFileCreateRequest request) {
        Task taskEntity = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + request.getTaskId()));

        String fileMetadataId = getFileMetadataById(request.getFileId()).getId();

        TaskFile taskFile = TaskFile.builder()
                .task(taskEntity)
                .fileId(fileMetadataId)
                .build();

        TaskFile savedTaskFile = taskFileRepository.save(taskFile);

        String message = String.format(
                "📁 *New File Uploaded*\n\n" +
                "🔹 Task: *%s* (ID: `%s`)\n" +
                "📚 Class ID: `%s`\n" +
                "🆔 File ID: `%s`",
                taskEntity.getTitle(),
                taskEntity.getId(),
                taskEntity.getProject().getClassId(),
                fileMetadataId
        );

        SendNotificationRequest notification = SendNotificationRequest.builder()
                .userId(taskEntity.getAssignedTo())
                .classId(taskEntity.getProject().getClassId())
                .message(message)
                .build();

        kafkaTemplate.send(topic.name(), notification);

        return savedTaskFile;
    }


    public List<TaskFile> getAll() {
        return taskFileRepository.findAll();
    }
    public List<TaskFile> getByTaskId(String taskId) {
        return taskFileRepository.findByTaskId(taskId);
    }


    public TaskFile getAdvertById(String id) {
        return findTaskFileById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public ClassesDto getClassById(String id) {
        return Optional.ofNullable(classGroupServiceclient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public FileMetadataDto getFileMetadataById(String id) {
        return Optional.ofNullable(fileStorageClient.getFileMetadataById(id).getBody())
                .orElseThrow(() -> new NotFoundException("File not found"));
    }


    public TaskFile updateTaskFileById(ProjectUpdateRequest request) {
    	TaskFile toUpdate = findTaskFileById(request.getId());
        modelMapper.map(request, toUpdate);


        return taskFileRepository.save(toUpdate);
    }

    public void deleteTaskFileById(String id) {
    	taskFileRepository.deleteById(id);
    }


    protected TaskFile findTaskFileById(String id) {
        return taskFileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task file not found"));
    }
}
