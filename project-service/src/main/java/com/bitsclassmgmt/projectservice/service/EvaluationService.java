package com.bitsclassmgmt.projectservice.service;

import java.util.List;
import java.util.Optional;

import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.projectservice.client.ClassGroupServiceClient;
import com.bitsclassmgmt.projectservice.client.UserServiceClient;
import com.bitsclassmgmt.projectservice.dto.ClassesDto;
import com.bitsclassmgmt.projectservice.dto.UserDto;
import com.bitsclassmgmt.projectservice.exc.NotFoundException;
import com.bitsclassmgmt.projectservice.model.Evaluation;
import com.bitsclassmgmt.projectservice.model.Task;
import com.bitsclassmgmt.projectservice.repository.EvaluationRepository;
import com.bitsclassmgmt.projectservice.repository.TaskFileRepository;
import com.bitsclassmgmt.projectservice.repository.TaskRepository;
import com.bitsclassmgmt.projectservice.request.notification.SendNotificationRequest;
import com.bitsclassmgmt.projectservice.request.project.EvaluationCreateRequest;
import com.bitsclassmgmt.projectservice.request.project.ProjectUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final TaskRepository taskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UserServiceClient userServiceclient;
    private final ClassGroupServiceClient classGroupServiceclient;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, SendNotificationRequest> kafkaTemplate;
    private final NewTopic topic;

    public Evaluation createEvaluation(EvaluationCreateRequest request) {
        Task taskEntity = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + request.getTaskId()));

        String evaluatorId = getUserById(request.getEvaluatorId()).getId();

        Evaluation evaluation = Evaluation.builder()
                .task(taskEntity)
                .evaluatorId(evaluatorId)
                .score(request.getScore())
                .comments(request.getComments())
                .fileId(request.getFileId())
                .build();

        Evaluation saved = evaluationRepository.save(evaluation);

        String message = String.format(
                "📋 *Task Evaluated*\n\n" +
                "🔹 Task: *%s* (ID: `%s`)\n" +
                "📚 Class ID: `%s`\n" +
                "✅ Score: *%d*\n" +
                "📝 Comments: %s",
                taskEntity.getTitle(),
                taskEntity.getId(),
                taskEntity.getProject().getClassId(),
                request.getScore(),
                request.getComments() == null ? "No comments." : request.getComments()
        );

        SendNotificationRequest notification = SendNotificationRequest.builder()
                .userId(taskEntity.getAssignedTo())
                .classId(taskEntity.getProject().getClassId())
                .message(message)
                .build();

        kafkaTemplate.send(topic.name(), notification);

        return saved;
    }

    public List<Evaluation> getAll() {
        return evaluationRepository.findAll();
    }
    
    public List<Evaluation> getByTaskId(String taskId) {
        return evaluationRepository.findByTaskId(taskId);
    }


    public Evaluation getAdvertById(String id) {
        return findAdvertById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public ClassesDto getClassById(String id) {
        return Optional.ofNullable(classGroupServiceclient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


    public Evaluation updateAdvertById(ProjectUpdateRequest request, MultipartFile file) {
    	Evaluation toUpdate = findAdvertById(request.getId());
        modelMapper.map(request, toUpdate);


        return evaluationRepository.save(toUpdate);
    }

    public void deleteAdvertById(String id) {
    	evaluationRepository.deleteById(id);
    }



    protected Evaluation findAdvertById(String id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advert not found"));
    }
}
