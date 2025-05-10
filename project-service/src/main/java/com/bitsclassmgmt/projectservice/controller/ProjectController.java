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
import org.springframework.web.bind.annotation.RestController;

import com.bitsclassmgmt.projectservice.dto.ProjectDto;
import com.bitsclassmgmt.projectservice.dto.SubProjectDto;
import com.bitsclassmgmt.projectservice.dto.TaskDto;
import com.bitsclassmgmt.projectservice.model.Project;
import com.bitsclassmgmt.projectservice.model.SubProject;
import com.bitsclassmgmt.projectservice.model.Task;
import com.bitsclassmgmt.projectservice.request.project.ProjectCreateRequest;
import com.bitsclassmgmt.projectservice.request.project.ProjectUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.SubProjectCreateRequest;
import com.bitsclassmgmt.projectservice.service.ProjectService;
import com.bitsclassmgmt.projectservice.service.SubProjectService;
import com.bitsclassmgmt.projectservice.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/project-service/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final ModelMapper modelMapper;
    private final TaskService taskService;

    @PostMapping("/")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody  ProjectCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(projectService.createProject(request), ProjectDto.class));
    }
    
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<ProjectDto>> getProjectsByClassId(@PathVariable String classId) {
        List<Project> projects = projectService.getProjectsByClassId(classId);
        List<ProjectDto> dtos = projects.stream()
                .map(project -> modelMapper.map(project, ProjectDto.class))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAll().stream()
                .map(advert -> modelMapper.map(advert, ProjectDto.class)).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectsById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(projectService.getProjectById(id), ProjectDto.class));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ProjectDto> updateProject(@Valid @RequestBody ProjectUpdateRequest request) {
        Project updated = projectService.updateProjectById(request);
        ProjectDto dto = modelMapper.map(updated, ProjectDto.class);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or @advertService.authorizeCheck(#id, principal)")
    public ResponseEntity<Void> deleteProjectsById(@PathVariable String id) {
    	projectService.deleteProjectById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/subprojects")
    public ResponseEntity<SubProjectDto> createSubProject(@PathVariable("id") String projectId, 
            @Valid @RequestBody SubProjectCreateRequest request) {
        
        request.setProjectId(projectId); // Associate request with the project

        // Call service to create a sub-project
        SubProject subProject = subProjectService.createSubProject(request);

        // Convert entity to DTO
        SubProjectDto responseDto = modelMapper.map(subProject, SubProjectDto.class);
        responseDto.setProjectId(subProject.getProject().getId()); // Ensure correct mapping

        if (subProject.getGroupId() != null) {
            responseDto.setGroupId(subProject.getGroupId()); // Set group ID if applicable
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @GetMapping("/{projectId}/subprojects")
    public ResponseEntity<List<SubProjectDto>> getSubProjectsByProjectId(@PathVariable String projectId) {
        List<SubProject> subProjects = subProjectService.getSubProjectsByProjectId(projectId);
        List<SubProjectDto> dtos = subProjects.stream()
                .map(sub -> {
                    SubProjectDto dto = modelMapper.map(sub, SubProjectDto.class);
                    dto.setProjectId(sub.getProject().getId());
                    if (sub.getGroupId() != null) {
                        dto.setGroupId(sub.getGroupId());
                    }
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByProjectId(@PathVariable String projectId) {
        List<Task> tasks = taskService.getTasksByProjectId(projectId);
        List<TaskDto> dtos = tasks.stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
