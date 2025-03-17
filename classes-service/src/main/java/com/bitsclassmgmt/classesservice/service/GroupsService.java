package com.bitsclassmgmt.classesservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.classesservice.client.UserServiceClient;
import com.bitsclassmgmt.classesservice.dto.GroupsDto;
import com.bitsclassmgmt.classesservice.dto.UserDto;
import com.bitsclassmgmt.classesservice.exc.NotFoundException;
import com.bitsclassmgmt.classesservice.model.Classes;
import com.bitsclassmgmt.classesservice.model.Groups;
import com.bitsclassmgmt.classesservice.repository.ClassesRepository;
import com.bitsclassmgmt.classesservice.repository.GroupsRepository;
import com.bitsclassmgmt.classesservice.request.classes.ClassesCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.classesservice.request.classes.GroupsCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final ClassesRepository classesRepository;
    private final UserServiceClient userServiceclient;
    private final ModelMapper modelMapper;

    public Groups createGroups(GroupsCreateRequest request) {
    	Classes classEntity = classesRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + request.getClassId()));

        Groups toSave = Groups.builder()
                .name(request.getName())
                .classEntity(classEntity)
                .build();
        return groupsRepository.save(toSave);
    }

    public List<Groups> getAll() {
        return groupsRepository.findAll();
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
        // Fetch groups from repository
        List<Groups> groups = groupsRepository.findByClassEntityId(classId);

        // Convert entities to DTOs
        return groups.stream()
                .map(group -> {
                    GroupsDto dto = modelMapper.map(group, GroupsDto.class);
                    dto.setClassId(group.getClassEntity().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
