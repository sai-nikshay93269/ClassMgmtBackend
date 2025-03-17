package com.bitsclassmgmt.projectservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.projectservice.client.ClassesServiceClient;
import com.bitsclassmgmt.projectservice.client.UserServiceClient;
import com.bitsclassmgmt.projectservice.dto.ClassesDto;
import com.bitsclassmgmt.projectservice.dto.UserDto;
import com.bitsclassmgmt.projectservice.exc.NotFoundException;
import com.bitsclassmgmt.projectservice.model.Evaluation;
import com.bitsclassmgmt.projectservice.model.Project;
import com.bitsclassmgmt.projectservice.model.Task;
import com.bitsclassmgmt.projectservice.repository.EvaluationRepository;
import com.bitsclassmgmt.projectservice.repository.TaskFileRepository;
import com.bitsclassmgmt.projectservice.repository.TaskRepository;
import com.bitsclassmgmt.projectservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.EvaluationCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final TaskRepository taskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UserServiceClient userServiceclient;
    private final ClassesServiceClient classesServiceclient;
    private final ModelMapper modelMapper;

    public Evaluation createEvaluation(EvaluationCreateRequest request) {
        // Fetch the task entity using taskId
        Task taskEntity = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + request.getTaskId()));

        // Fetch evaluator user (e.g., teacher or admin)
        String evaluatorId = getUserById(request.getEvaluatorId()).getId();

        // Create and save the Evaluation entry
        Evaluation evaluation = Evaluation.builder()
                .task(taskEntity) // Now storing Task entity instead of just taskId
                .evaluatorId(evaluatorId)
                .score(request.getScore()) // Score is required
                .comments(request.getComments()) // Optional feedback
                .fileId(request.getFileId()) // Optional file reference
                .build();

        return evaluationRepository.save(evaluation);
    }

    public List<Evaluation> getAll() {
        return evaluationRepository.findAll();
    }

    public Evaluation getAdvertById(String id) {
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


    public Evaluation updateAdvertById(ClassesUpdateRequest request, MultipartFile file) {
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
