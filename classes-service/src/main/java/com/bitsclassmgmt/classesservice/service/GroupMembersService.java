package com.bitsclassmgmt.classesservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.classesservice.client.UserServiceClient;
import com.bitsclassmgmt.classesservice.dto.GroupMembersDto;
import com.bitsclassmgmt.classesservice.dto.UserDto;
import com.bitsclassmgmt.classesservice.exc.NotFoundException;
import com.bitsclassmgmt.classesservice.model.Classes;
import com.bitsclassmgmt.classesservice.model.GroupMembers;
import com.bitsclassmgmt.classesservice.model.Groups;
import com.bitsclassmgmt.classesservice.repository.ClassesRepository;
import com.bitsclassmgmt.classesservice.repository.GroupMembersRepository;
import com.bitsclassmgmt.classesservice.repository.GroupsRepository;
import com.bitsclassmgmt.classesservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.classesservice.request.classes.GroupMembersCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupMembersService {
    private final GroupMembersRepository groupMembersRepository;
    private final GroupsRepository groupsRepository;
    private final UserServiceClient userServiceclient;
    private final ModelMapper modelMapper;
    private final ClassesRepository classesRepository;

    public GroupMembers createGroupMembers(GroupMembersCreateRequest request) {
    	
    	String studentId = getUserById(request.getStudentId()).getId();
        // Fetch the class entity using classId
        Groups groupsEntity = groupsRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + request.getGroupId()));

        // Create and save class member
        GroupMembers toSave = GroupMembers.builder()
                .studentId(studentId)  // Fix: Use request.getStudentId()
                .groupEntity(groupsEntity)  // Fix: Assign class entity correctly
                .build();
        
        return groupMembersRepository.save(toSave);
    }
    
    public List<GroupMembersDto> getMembersByGroup(String groupId) {
        // Fetch group members from repository
        List<GroupMembers> groupMembers = groupMembersRepository.findByGroupEntityId(groupId);

        // Convert entities to DTOs
        return groupMembers.stream()
                .map(member -> modelMapper.map(member, GroupMembersDto.class))
                .collect(Collectors.toList());
    }

    public List<GroupMembers> getAll() {
        return groupMembersRepository.findAll();
    }

    public Classes getAdvertById(String id) {
        return findAdvertById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Classes updateAdvertById(ClassesUpdateRequest request, MultipartFile file) {
        Classes toUpdate = findAdvertById(request.getId());
        modelMapper.map(request, toUpdate);


        return classesRepository.save(toUpdate);
    }

    public void deleteAdvertById(String id) {
        classesRepository.deleteById(id);
    }



    protected Classes findAdvertById(String id) {
        return classesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advert not found"));
    }
}
