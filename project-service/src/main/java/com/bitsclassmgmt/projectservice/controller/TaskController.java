package com.bitsclassmgmt.projectservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.projectservice.dto.EvaluationDto;
import com.bitsclassmgmt.projectservice.dto.ProjectDto;
import com.bitsclassmgmt.projectservice.dto.TaskDto;
import com.bitsclassmgmt.projectservice.dto.TaskFileDto;
import com.bitsclassmgmt.projectservice.model.Evaluation;
import com.bitsclassmgmt.projectservice.model.TaskFile;
import com.bitsclassmgmt.projectservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.EvaluationCreateRequest;
import com.bitsclassmgmt.projectservice.request.project.TaskFileCreateRequest;
import com.bitsclassmgmt.projectservice.service.EvaluationService;
import com.bitsclassmgmt.projectservice.service.ProjectService;
import com.bitsclassmgmt.projectservice.service.SubProjectService;
import com.bitsclassmgmt.projectservice.service.TaskFileService;
import com.bitsclassmgmt.projectservice.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/project-service/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final TaskService taskService;
    private final TaskFileService taskFileService;
    private final EvaluationService evaluationService;
    private final ModelMapper modelMapper;

    
    @GetMapping("/getAll")
    public ResponseEntity<List<TaskDto>> getAllTask() {
        return ResponseEntity.ok(taskService.getAll().stream()
                .map(task -> modelMapper.map(task, TaskDto.class)).toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(taskService.getTaskById(id), TaskDto.class));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @advertService.authorizeCheck(#request.id, principal)")
    public ResponseEntity<TaskDto> updateTaskById(@Valid @RequestPart ClassesUpdateRequest request) {
        return ResponseEntity.ok(modelMapper.map(taskService.updatTaskById(request), TaskDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @advertService.authorizeCheck(#id, principal)")
    public ResponseEntity<Void> deleteTaskById(@PathVariable String id) {
    	taskService.deleteTaskById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/files")
    public ResponseEntity<TaskFileDto> createTaskFile(@PathVariable("id") String taskId, 
            @Valid @RequestBody TaskFileCreateRequest request) {
    	request.setTaskId(taskId);
    	 // Call service to create class member
        TaskFile taskFile = taskFileService.createTaskFile(request);

        // Convert entity to DTO
        TaskFileDto responseDto = modelMapper.map(taskFile, TaskFileDto.class);
        responseDto.setTaskId(taskFile.getTask().getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<TaskFileDto>> getAllTaskFiles() {
        return ResponseEntity.ok(taskFileService.getAll().stream()
                .map(taskFile -> modelMapper.map(taskFile, TaskFileDto.class)).toList());
    }
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteTaskFileById(@PathVariable String id) {
    	taskFileService.deleteTaskFileById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/evaluations")
    public ResponseEntity<EvaluationDto> createEvaluation(
            @PathVariable("id") String taskId, 
            @Valid @RequestBody EvaluationCreateRequest request) {
        
    	request.setTaskId(taskId);
        
        // Call service to create evaluation
        Evaluation evaluation = evaluationService.createEvaluation(request);

        // Convert entity to DTO
        EvaluationDto responseDto = modelMapper.map(evaluation, EvaluationDto.class);
        responseDto.setTaskId(evaluation.getTask().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @GetMapping("/{id}/evaluations")
    public ResponseEntity<List<EvaluationDto>> getAllEvaluationsByTaskID() {
        return ResponseEntity.ok(evaluationService.getAll().stream()
                .map(evaluation -> modelMapper.map(evaluation, EvaluationDto.class)).toList());
    }
    
}
