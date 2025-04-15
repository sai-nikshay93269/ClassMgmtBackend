package com.bitsclassmgmt.classesservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bitsclassmgmt.classesservice.client.UserServiceClient;
import com.bitsclassmgmt.classesservice.dto.GroupsDto;
import com.bitsclassmgmt.classesservice.dto.UserDto;
import com.bitsclassmgmt.classesservice.exc.NotFoundException;
import com.bitsclassmgmt.classesservice.mapper.GroupsMapper;
import com.bitsclassmgmt.classesservice.model.Classes;
import com.bitsclassmgmt.classesservice.model.GroupMembers;
import com.bitsclassmgmt.classesservice.model.Groups;
import com.bitsclassmgmt.classesservice.repository.ClassesRepository;
import com.bitsclassmgmt.classesservice.repository.GroupMembersRepository;
import com.bitsclassmgmt.classesservice.repository.GroupsRepository;
import com.bitsclassmgmt.classesservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.classesservice.request.classes.GroupsCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final GroupMembersRepository groupMembersRepository;
    private final ClassesRepository classesRepository;
    private final UserServiceClient userServiceclient;
    private final ModelMapper modelMapper;

    public Groups createGroups(GroupsCreateRequest request) {
        Classes classEntity = classesRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + request.getClassId()));

        // ✅ Check if group with same name exists for the class
        boolean groupExists = groupsRepository.existsByNameAndClassEntity_Id(request.getName(), request.getClassId());
        if (groupExists) {
            throw new RuntimeException("Group with name '" + request.getName() + "' already exists in this class.");
        }

        Groups toSave = Groups.builder()
                .name(request.getName())
                .classEntity(classEntity)
                .build();

        return groupsRepository.save(toSave);
    }


    public List<Groups> getAll() {
        return groupsRepository.findAll();
    }
    
    public List<GroupsDto> getGroupsForStudentInClass(String studentId, String classId) {
        List<GroupMembers> memberships = groupMembersRepository.findByStudentIdAndGroupEntity_ClassEntity_Id(studentId, classId);
        return memberships.stream()
                .map(GroupMembers::getGroupEntity)
                .map(group -> modelMapper.map(group, GroupsDto.class))
                .collect(Collectors.toList());
    }


    public Groups getGroupById(String id) {
        return findGroupById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Groups updateGroupById(ClassesUpdateRequest request) {
        Groups toUpdate = getGroupById(request.getId());
        modelMapper.map(request, toUpdate);


        return groupsRepository.save(toUpdate);
    }

    public void deleteGroupById(String id) {
    	groupsRepository.deleteById(id);
    }


    protected Groups findGroupById(String id) {
        return groupsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("group not found"));
    }
    
    public List<GroupsDto> getGroupsByClassId(String classId) {
        List<Groups> groups = groupsRepository.findByClassEntityId(classId);
        return groups.stream()
                .map(GroupsMapper::toDto)  // Use custom mapper
                .collect(Collectors.toList());
    }

}
