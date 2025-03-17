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

import com.bitsclassmgmt.projectservice.dto.ProjectDto;
import com.bitsclassmgmt.projectservice.dto.SubProjectDto;
import com.bitsclassmgmt.projectservice.dto.TaskDto;
import com.bitsclassmgmt.projectservice.model.Task;
import com.bitsclassmgmt.projectservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.TaskCreateRequest;
import com.bitsclassmgmt.projectservice.service.ProjectService;
import com.bitsclassmgmt.projectservice.service.SubProjectService;
import com.bitsclassmgmt.projectservice.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/project-service/subprojects")
@RequiredArgsConstructor
public class SubProjectController {
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    
    @GetMapping("/getAll")
    public ResponseEntity<List<SubProjectDto>> getAllSubProjects() {
        return ResponseEntity.ok(subProjectService.getAll().stream()
                .map(subProject -> modelMapper.map(subProject, SubProjectDto.class)).toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SubProjectDto> getSubProjectById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(subProjectService.getSubProjectById(id), SubProjectDto.class));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @advertService.authorizeCheck(#request.id, principal)")
    public ResponseEntity<ProjectDto> updateSubProjectById(@Valid @RequestPart ClassesUpdateRequest request) {
        return ResponseEntity.ok(modelMapper.map(subProjectService.updateSubProjectById(request), ProjectDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @advertService.authorizeCheck(#id, principal)")
    public ResponseEntity<Void> deleteSubProjectById(@PathVariable String id) {
    	subProjectService.deleteSubProjectById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/tasks")
    public ResponseEntity<TaskDto> createTask(@PathVariable("id") String projectId, 
            @Valid @RequestBody TaskCreateRequest request) {
    	request.setProjectId(projectId);
    	 // Call service to create class member
        Task task = taskService.createTask(request);

        // Convert entity to DTO
        TaskDto responseDto = modelMapper.map(task, TaskDto.class);
        responseDto.setProjectId(task.getProject().getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<ProjectDto>> getAllTasksbySubProject() {
        return ResponseEntity.ok(subProjectService.getAll().stream()
                .map(advert -> modelMapper.map(advert, ProjectDto.class)).toList());
    }
    
}
