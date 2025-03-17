package com.bitsclassmgmt.projectservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.projectservice.client.GroupsServiceClient;
import com.bitsclassmgmt.projectservice.client.UserServiceClient;
import com.bitsclassmgmt.projectservice.dto.ClassesDto;
import com.bitsclassmgmt.projectservice.dto.GroupsDto;
import com.bitsclassmgmt.projectservice.dto.UserDto;
import com.bitsclassmgmt.projectservice.exc.NotFoundException;
import com.bitsclassmgmt.projectservice.model.Project;
import com.bitsclassmgmt.projectservice.model.SubProject;
import com.bitsclassmgmt.projectservice.repository.ProjectRepository;
import com.bitsclassmgmt.projectservice.repository.SubProjectRepository;
import com.bitsclassmgmt.projectservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.projectservice.request.project.SubProjectCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubProjectService {
    private final SubProjectRepository subProjectRepository;
    private final ProjectRepository projectRepository;
    private final UserServiceClient userServiceclient;
    private final GroupsServiceClient groupServiceclient;
    private final ModelMapper modelMapper;

    public SubProject createSubProject(SubProjectCreateRequest request) {

        // Fetch the project entity using projectId
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + request.getProjectId()));

        // Fetch the group entity if provided (optional)
        String groupId = getGroupById(request.getGroupId()).getId();

        // Create and save the sub-project
        SubProject subProject = SubProject.builder()
                .project(project) // Associate with the project
                .groupId(groupId) // Associate with the group if applicable
                .title(request.getTitle()) // Set sub-project title
                .description(request.getDescription()) // Set description
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
        return Optional.ofNullable(groupServiceclient.getGroupsById(id).getBody())
                .orElseThrow(() -> new NotFoundException("Group not found"));
    }
    
    public SubProject updateSubProjectById(ClassesUpdateRequest request) {
    	SubProject toUpdate = findSubProjectById(request.getId());
        modelMapper.map(request, toUpdate);


        return subProjectRepository.save(toUpdate);
    }

    public void deleteSubProjectById(String id) {
    	subProjectRepository.deleteById(id);
    }


    protected SubProject findSubProjectById(String id) {
        return subProjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sub-Project not found"));
    }
}
