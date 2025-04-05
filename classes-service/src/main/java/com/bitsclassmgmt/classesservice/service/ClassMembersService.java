package com.bitsclassmgmt.classesservice.service;

import com.bitsclassmgmt.classesservice.client.UserServiceClient;
import com.bitsclassmgmt.classesservice.dto.ClassMembersDto;
import com.bitsclassmgmt.classesservice.dto.UserDto;
import com.bitsclassmgmt.classesservice.exc.GenericErrorResponse;
import com.bitsclassmgmt.classesservice.exc.NotFoundException;
import com.bitsclassmgmt.classesservice.model.ClassMembers;
import com.bitsclassmgmt.classesservice.model.Classes;
import com.bitsclassmgmt.classesservice.repository.ClassMembersRepository;
import com.bitsclassmgmt.classesservice.repository.ClassesRepository;
import com.bitsclassmgmt.classesservice.request.classes.ClassMembersCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.ClassesUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassMembersService {

    private final ClassMembersRepository classMembersRepository;
    private final ClassesRepository classesRepository;
    private final UserServiceClient userServiceclient;
    private final ModelMapper modelMapper;

    public List<ClassMembersDto> createClassMembersBatch(ClassMembersCreateRequest request) {
        Classes classEntity = classesRepository.findById(request.getClassId())
                .orElseThrow(() -> new NotFoundException("Class not found with ID: " + request.getClassId()));

        List<String> userIds = request.getStudentIds();

        // Validate all user IDs
        List<String> validatedUserIds = userIds.stream()
                .map(id -> getUserById(id).getId())
                .collect(Collectors.toList());

        // Find existing studentIds in the class
        Set<String> existingStudentIds = classMembersRepository.findByClassEntityId(classEntity.getId()).stream()
                .map(ClassMembers::getStudentId)
                .collect(Collectors.toSet());

        List<ClassMembers> toCreate = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        for (String studentId : validatedUserIds) {
            if (existingStudentIds.contains(studentId)) {
                skipped.add(studentId);
                continue;
            }

            ClassMembers member = ClassMembers.builder()
                    .studentId(studentId)
                    .classEntity(classEntity)
                    .build();

            toCreate.add(member);
        }

        if (toCreate.isEmpty()) {
            throw new GenericErrorResponse("All students are already members of this class", HttpStatus.CONFLICT);
        }

        List<ClassMembers> saved = classMembersRepository.saveAll(toCreate);

        return saved.stream().map(member -> {
            ClassMembersDto dto = modelMapper.map(member, ClassMembersDto.class);
            dto.setClassId(member.getClassEntity().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ClassMembers> getAll() {
        return classMembersRepository.findAll();
    }

    public List<ClassMembers> getMembersByClassId(String classId) {
        return classMembersRepository.findByClassEntityId(classId);
    }

    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
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
