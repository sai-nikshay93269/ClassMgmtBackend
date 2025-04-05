package com.bitsclassmgmt.classesservice.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.classesservice.client.UserServiceClient;
import com.bitsclassmgmt.classesservice.dto.GroupMembersDto;
import com.bitsclassmgmt.classesservice.dto.UserDto;
import com.bitsclassmgmt.classesservice.exc.GenericErrorResponse;
import com.bitsclassmgmt.classesservice.exc.NotFoundException;
import com.bitsclassmgmt.classesservice.model.ClassMembers;
import com.bitsclassmgmt.classesservice.model.Classes;
import com.bitsclassmgmt.classesservice.model.GroupMembers;
import com.bitsclassmgmt.classesservice.model.Groups;
import com.bitsclassmgmt.classesservice.repository.ClassMembersRepository;
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
    private final ClassMembersRepository classMembersRepository;
    private final GroupsRepository groupsRepository;
    private final UserServiceClient userServiceclient;
    private final ModelMapper modelMapper;
    private final ClassesRepository classesRepository;

    public List<GroupMembersDto> createGroupMembersBatch(GroupMembersCreateRequest request) {
        Groups groupEntity = groupsRepository.findById(request.getGroupId())
                .orElseThrow(() -> new NotFoundException("Group not found with ID: " + request.getGroupId()));

        List<String> userIds = request.getStudentIds();

        // Validate users via Auth/User service (if applicable)
        List<String> validatedUserIds = userIds.stream()
                .map(id -> getUserById(id).getId())
                .collect(Collectors.toList());

        // ✅ Fetch all class members' studentIds for the group’s class
        String classId = groupEntity.getClassEntity().getId();
        Set<String> classMemberIds = classMembersRepository.findByClassEntityId(classId).stream()
                .map(ClassMembers::getStudentId)
                .collect(Collectors.toSet());

        // ✅ Check for any invalid users not in the class
        List<String> invalidStudentIds = validatedUserIds.stream()
                .filter(id -> !classMemberIds.contains(id))
                .collect(Collectors.toList());

        if (!invalidStudentIds.isEmpty()) {
            throw new GenericErrorResponse("The following users are not members of the class: " + invalidStudentIds,
                    HttpStatus.BAD_REQUEST);
        }

        // ✅ Filter out users already in the group
        Set<String> existingStudentIds = groupMembersRepository.findByGroupEntityId(groupEntity.getId()).stream()
                .map(GroupMembers::getStudentId)
                .collect(Collectors.toSet());

        List<GroupMembers> toCreate = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        for (String studentId : validatedUserIds) {
            if (existingStudentIds.contains(studentId)) {
                skipped.add(studentId);
                continue;
            }

            GroupMembers member = GroupMembers.builder()
                    .studentId(studentId)
                    .groupEntity(groupEntity)
                    .build();

            toCreate.add(member);
        }

        if (toCreate.isEmpty()) {
            throw new GenericErrorResponse("All students are already members of this group", HttpStatus.CONFLICT);
        }

        List<GroupMembers> saved = groupMembersRepository.saveAll(toCreate);

        return saved.stream().map(member -> {
            GroupMembersDto dto = modelMapper.map(member, GroupMembersDto.class);
            dto.setGroupId(member.getGroupEntity().getId());
            return dto;
        }).collect(Collectors.toList());
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
