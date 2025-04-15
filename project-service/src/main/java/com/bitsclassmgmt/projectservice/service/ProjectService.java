package com.bitsclassmgmt.projectservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bitsclassmgmt.projectservice.client.ClassGroupServiceClient;
import com.bitsclassmgmt.projectservice.client.UserServiceClient;
import com.bitsclassmgmt.projectservice.dto.ClassesDto;
import com.bitsclassmgmt.projectservice.dto.UserDto;
import com.bitsclassmgmt.projectservice.exc.NotFoundException;
import com.bitsclassmgmt.projectservice.model.Project;
import com.bitsclassmgmt.projectservice.repository.ProjectRepository;
import com.bitsclassmgmt.projectservice.request.project.ProjectCreateRequest;
import com.bitsclassmgmt.projectservice.request.project.ProjectUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserServiceClient userServiceclient;
    private final ClassGroupServiceClient classGroupServiceclient;
    private final ModelMapper modelMapper;

    public Project createProject(ProjectCreateRequest request) {
    	
    	String classId = getClassById(request.getClassId()).getId();
        Project toSave = Project.builder()
                .classId(classId) // Linking project to a class
                .title(request.getTitle()) // Using 'title' instead of 'name'
                .description(request.getDescription())
                .dueDate(request.getDueDate()) // Setting due date
                .build();
        
        return projectRepository.save(toSave); // Use correct repository name
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Project getProjectById(String id) {
        return findProjectById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public ClassesDto getClassById(String id) {
        return Optional.ofNullable(classGroupServiceclient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("Class not found"));
    }

    public Project updateProjectById(ProjectUpdateRequest request) {
        // Validate the ID
        if (request.getId() == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }

        // Fetch the project or throw NotFoundException
        Project existingProject = projectRepository.findById(request.getId())
            .orElseThrow(() -> new NotFoundException("Project not found with ID: " + request.getId()));

        // Map non-null fields from request to existing entity
        modelMapper.map(request, existingProject);

        // Save and return updated project
        return projectRepository.save(existingProject);
    }


    public void deleteProjectById(String id) {
    	projectRepository.deleteById(id);
    }


    protected Project findProjectById(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }
    public List<Project> getProjectsByClassId(String classId) {
        return projectRepository.findByClassId(classId);
    }
}
