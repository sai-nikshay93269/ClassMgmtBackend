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
import com.bitsclassmgmt.projectservice.model.SubProject;
import com.bitsclassmgmt.projectservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.projectservice.request.classes.ProjectCreateRequest;
import com.bitsclassmgmt.projectservice.request.project.SubProjectCreateRequest;
import com.bitsclassmgmt.projectservice.service.ProjectService;
import com.bitsclassmgmt.projectservice.service.SubProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/project-service/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final ModelMapper modelMapper;

    @PostMapping("/")
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody  ProjectCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(projectService.createProject(request), ProjectDto.class));
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
    @PreAuthorize("hasRole('TEACHER') or @advertService.authorizeCheck(#request.id, principal)")
    public ResponseEntity<ProjectDto> updateProjectsById(@Valid @RequestPart ClassesUpdateRequest request) {
        return ResponseEntity.ok(modelMapper.map(projectService.updateProjectById(request), ProjectDto.class));
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
    
}
