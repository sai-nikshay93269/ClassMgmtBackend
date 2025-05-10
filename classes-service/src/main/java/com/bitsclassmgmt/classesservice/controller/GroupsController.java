package com.bitsclassmgmt.classesservice.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.bitsclassmgmt.classesservice.dto.ClassMembersDto;
import com.bitsclassmgmt.classesservice.dto.ClassesDto;
import com.bitsclassmgmt.classesservice.dto.GroupMembersDto;
import com.bitsclassmgmt.classesservice.dto.GroupsDto;
import com.bitsclassmgmt.classesservice.mapper.GroupsMapper;
import com.bitsclassmgmt.classesservice.model.ClassMembers;
import com.bitsclassmgmt.classesservice.model.GroupMembers;
import com.bitsclassmgmt.classesservice.request.classes.ClassMembersCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.ClassesCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.classesservice.request.classes.GroupMembersCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.GroupsCreateRequest;
import com.bitsclassmgmt.classesservice.service.ClassMembersService;
import com.bitsclassmgmt.classesservice.service.ClassesService;
import com.bitsclassmgmt.classesservice.service.GroupMembersService;
import com.bitsclassmgmt.classesservice.service.GroupsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/classes-service/groups")
@RequiredArgsConstructor
public class GroupsController {
    private final GroupsService groupsService;
    private final ClassMembersService classMembersService;
    private final GroupMembersService groupMembersService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @PostMapping("")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<GroupsDto> createGroups(@Valid @RequestBody  GroupsCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(groupsService.createGroups(request), GroupsDto.class));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GroupsDto>> getAllGroups() {
        return ResponseEntity.ok(
            groupsService.getAll().stream()
                .map(GroupsMapper::toDto)  // Use custom mapper
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupsDto> getGroupById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(groupsService.getGroupById(id), GroupsDto.class));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<GroupsDto> updateGroupById(@Valid @RequestPart ClassesUpdateRequest request) {
        return ResponseEntity.ok(modelMapper.map(groupsService.updateGroupById(request), GroupsDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteGroupById(@PathVariable String id) {
    	groupsService.deleteGroupById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/members")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<GroupMembersDto>> createGroupMembers(
            @PathVariable("id") String groupId,
            @RequestBody GroupMembersCreateRequest request) {

        request.setGroupId(groupId);

        // Manual validation
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(request, "groupMembersCreateRequest");
        validator.validate(request, bindingResult);

        List<GroupMembersDto> savedMembers = groupMembersService.createGroupMembersBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMembers);
    }
    
    @GetMapping("/{id}/members")
    public ResponseEntity<List<GroupMembersDto>> getGroupMembersByGroup(@PathVariable("id") String groupId) {
        // Fetch group members from service
        List<GroupMembersDto> responseDtoList = groupMembersService.getMembersByGroup(groupId);

        return ResponseEntity.ok(responseDtoList);
    }


    @GetMapping("/members/getAll")
    public ResponseEntity<List<GroupMembersDto>> getAllClassMembers() {
        return ResponseEntity.ok(groupMembersService.getAll().stream()
                .map(member -> modelMapper.map(member, GroupMembersDto.class)).toList());
    }

    
}
