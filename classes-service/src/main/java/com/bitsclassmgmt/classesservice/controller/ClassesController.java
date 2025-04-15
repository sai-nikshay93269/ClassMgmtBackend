package com.bitsclassmgmt.classesservice.controller;

import java.util.List;
import java.util.Map;
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

import com.bitsclassmgmt.classesservice.dto.ClassMembersDto;
import com.bitsclassmgmt.classesservice.dto.ClassesDto;
import com.bitsclassmgmt.classesservice.dto.GroupsDto;
import com.bitsclassmgmt.classesservice.jwt.AuthUtil;
import com.bitsclassmgmt.classesservice.model.ClassMembers;
import com.bitsclassmgmt.classesservice.model.Groups;
import com.bitsclassmgmt.classesservice.request.classes.ClassMembersCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.ClassesCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.ClassesUpdateRequest;
import com.bitsclassmgmt.classesservice.service.ClassMembersService;
import com.bitsclassmgmt.classesservice.service.ClassesService;
import com.bitsclassmgmt.classesservice.service.GroupsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/classes-service/classes")
@RequiredArgsConstructor
public class ClassesController {
    private final ClassesService classesService;
    private final ClassMembersService classMembersService;
    private final GroupsService groupsService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @PostMapping("")
    public ResponseEntity<ClassesDto> createClasses(@Valid @RequestBody  ClassesCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(classesService.createClasses(request), ClassesDto.class));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ClassesDto>> getAllClasses() {
        return ResponseEntity.ok(classesService.getAll());
    }
    
    @GetMapping("/my-classes")
    public ResponseEntity<List<ClassesDto>> getClassesForCurrentUser() {
        String userId = AuthUtil.getCurrentUserId();
        String role = AuthUtil.getCurrentUserRole();
        System.out.println(userId);
        System.out.println(role);
        List<ClassesDto> result;

        if ("ROLE_TEACHER".equals(role)) {
            result = classesService.getAll(); // all classes for teachers
        } else {
        	System.out.println(userId);
            result = classesService.getClassesForStudent(userId); // filtered
        }

        return ResponseEntity.ok(result);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<ClassesDto> getClassesById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(classesService.getClassById(id), ClassesDto.class));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @classesService.authorizeCheck(#request.id, principal)")
    public ResponseEntity<ClassesDto> updateClassesById(@Valid @RequestPart ClassesUpdateRequest request) {
        return ResponseEntity.ok(modelMapper.map(classesService.updateClassById(request), ClassesDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @classesService.authorizeCheck(#id, principal)")
    public ResponseEntity<Void> deleteClassesById(@PathVariable String id) {
        classesService.deleteClassById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/members")
    public ResponseEntity<List<ClassMembersDto>> createClassMembers(
            @PathVariable("id") String classId,
            @RequestBody ClassMembersCreateRequest request) {
    	 request.setClassId(classId);

         // Manually validate after setting classId
         BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(request, "classMembersCreateRequest");
         validator.validate(request, bindingResult);
        List<ClassMembersDto> savedMembers = classMembersService.createClassMembersBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMembers);
    }

    
    @GetMapping("/{id}/members")
    public ResponseEntity<List<ClassMembersDto>> getClassMembersByClass(@PathVariable("id") String classId) {
        // Fetch class members from service
        List<ClassMembers> classMembers = classMembersService.getMembersByClassId(classId);

        // Convert entity list to DTO list
        List<ClassMembersDto> responseDtoList = classMembers.stream()
                .map(member -> {
                    ClassMembersDto dto = modelMapper.map(member, ClassMembersDto.class);
                    dto.setClassId(member.getClassEntity().getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }


    @GetMapping("/members/getAll")
    public ResponseEntity<List<ClassMembersDto>> getAllClassMembers() {
        return ResponseEntity.ok(classMembersService.getAll().stream()
                .map(classMember -> modelMapper.map(classMember, ClassMembersDto.class)).toList());
    }
    
    @GetMapping("/{id}/groups")
    public ResponseEntity<List<GroupsDto>> getGroupsByClass(@PathVariable("id") String classId) {
        // Fetch groups from service
        List<GroupsDto> responseDtoList = groupsService.getGroupsByClassId(classId);

        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/{classId}/my-groups")
    public ResponseEntity<List<GroupsDto>> getGroupsForClassAndUser(@PathVariable String classId) {
        String userId = AuthUtil.getCurrentUserId();
        String role = AuthUtil.getCurrentUserRole();

        List<GroupsDto> result;

        if ("ROLE_TEACHER".equals(role)) {
            result = groupsService.getGroupsByClassId(classId);
        } else {
            result = groupsService.getGroupsForStudentInClass(userId, classId);
        }

        return ResponseEntity.ok(result);
    }

    
}
