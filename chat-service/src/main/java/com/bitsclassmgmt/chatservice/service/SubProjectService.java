package com.bitsclassmgmt.chatservice.service;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.chatservice.client.GroupsServiceClient;
import com.bitsclassmgmt.chatservice.client.UserServiceClient;
import com.bitsclassmgmt.chatservice.dto.GroupsDto;
import com.bitsclassmgmt.chatservice.dto.UserDto;
import com.bitsclassmgmt.chatservice.exc.NotFoundException;
import com.bitsclassmgmt.chatservice.model.Chat;
import com.bitsclassmgmt.chatservice.model.SubProject;
import com.bitsclassmgmt.chatservice.repository.ChatRepository;
import com.bitsclassmgmt.chatservice.repository.SubProjectRepository;
import com.bitsclassmgmt.chatservice.request.chat.ClassesUpdateRequest;
import com.bitsclassmgmt.chatservice.request.chat.SubProjectCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubProjectService {
    private final ChatRepository projectRepository;
    private final SubProjectRepository subProjectRepository;
    private final UserServiceClient userServiceclient;
    private final GroupsServiceClient groupsServiceclient;
    private final ModelMapper modelMapper;

    public SubProject createTask(SubProjectCreateRequest request) {
        
        // Fetch the Project entity using projectId
        Chat projectEntity = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + request.getProjectId()));
        String groupId = getGroupById(request.getGroupId()).getId();

        // Create and save the SubProject
        SubProject toSave = SubProject.builder()
                .project(projectEntity) // Assign project entity
                .groupId(groupId) // Optional: Assign group if provided
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .build();
        
        return subProjectRepository.save(toSave);
    }

    public GroupsDto getGroupById(String id) {
        return Optional.ofNullable(groupsServiceclient.getGroupsById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    
    public List<Chat> getAll() {
        return classesRepository.findAll();
    }

    public Chat getAdvertById(String id) {
        return findAdvertById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Chat updateAdvertById(ClassesUpdateRequest request, MultipartFile file) {
        Chat toUpdate = findAdvertById(request.getId());
        modelMapper.map(request, toUpdate);


        return classesRepository.save(toUpdate);
    }

    public void deleteAdvertById(String id) {
        classesRepository.deleteById(id);
    }

    public boolean authorizeCheck(String id, String principal) {
        return getUserById(getAdvertById(id).getUserId()).getUsername().equals(principal);
    }

    protected Chat findAdvertById(String id) {
        return classesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advert not found"));
    }
}
