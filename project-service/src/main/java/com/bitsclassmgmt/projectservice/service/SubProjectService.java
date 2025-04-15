package com.bitsclassmgmt.projectservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bitsclassmgmt.projectservice.client.ClassGroupServiceClient;
import com.bitsclassmgmt.projectservice.client.UserServiceClient;
import com.bitsclassmgmt.projectservice.dto.GroupsDto;
import com.bitsclassmgmt.projectservice.dto.UserDto;
import com.bitsclassmgmt.projectservice.exc.NotFoundException;
import com.bitsclassmgmt.projectservice.model.Project;
import com.bitsclassmgmt.projectservice.model.SubProject;
import com.bitsclassmgmt.projectservice.repository.ProjectRepository;
import com.bitsclassmgmt.projectservice.repository.SubProjectRepository;
import com.bitsclassmgmt.projectservice.request.project.ProjectUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.SubProjectCreateRequest;
import com.bitsclassmgmt.projectservice.request.project.SubProjectUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubProjectService {
    private final SubProjectRepository subProjectRepository;
    private final ProjectRepository projectRepository;
    private final UserServiceClient userServiceclient;
    private final ClassGroupServiceClient classGroupServiceclient;
    private final ModelMapper modelMapper;

    public SubProject createSubProject(SubProjectCreateRequest request) {

        // Fetch the project entity using projectId
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + request.getProjectId()));

        // Fetch the group entity if provided (optional)
        if (request.getGroupId() != null) {
        	getGroupById(request.getGroupId());
        }

        // Create and save the sub-project
        SubProject subProject = SubProject.builder()
                .project(project) // Associate with the project
                .groupId(request.getGroupId()) // Associate with the group if applicable
                .title(request.getTitle()) // Set sub-project title
                .description(request.getDescription()) // Set description
                .dueDate(request.getDueDate()) 
                .build();

        return subProjectRepository.save(subProject);
    }


    public List<SubProject> getAll() {
        return subProjectRepository.findAll();
    }

    public SubProject getSubProjectById(String id) {
        return findSubProjectById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public GroupsDto getGroupById(String id) {
        return Optional.ofNullable(classGroupServiceclient.getGroupsById(id).getBody())
                .orElseThrow(() -> new NotFoundException("Group not found"));
    }
    
    public SubProject updateSubProjectById(SubProjectUpdateRequest request) {
    	SubProject existingSubProject = subProjectRepository.findById(request.getId())
    		    .orElseThrow(() -> new NotFoundException("SubProject not found with ID: " + request.getId()));

    		modelMapper.map(request, existingSubProject);
    		return subProjectRepository.save(existingSubProject);

    }


    public void deleteSubProjectById(String id) {
    	subProjectRepository.deleteById(id);
    }


    protected SubProject findSubProjectById(String id) {
        return subProjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sub-Project not found"));
    }
    public List<SubProject> getSubProjectsByProjectId(String projectId) {
        return subProjectRepository.findByProjectId(projectId);
    }
}
