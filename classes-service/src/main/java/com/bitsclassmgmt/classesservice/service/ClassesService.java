package com.bitsclassmgmt.classesservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.classesservice.client.UserServiceClient;
import com.bitsclassmgmt.classesservice.dto.ClassesDto;
import com.bitsclassmgmt.classesservice.dto.UserDto;
import com.bitsclassmgmt.classesservice.exc.NotFoundException;
import com.bitsclassmgmt.classesservice.mapper.ClassesMapper;
import com.bitsclassmgmt.classesservice.model.Classes;
import com.bitsclassmgmt.classesservice.repository.ClassesRepository;
import com.bitsclassmgmt.classesservice.request.classes.ClassesCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.ClassesUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassesService {
    private final ClassesRepository classesRepository;
    private final UserServiceClient userServiceclient;
    private final ModelMapper modelMapper;

    public Classes createClasses(ClassesCreateRequest request) {
        String userId = getUserById(request.getTeacherId()).getId();

        // ✅ Check if a class with the same name already exists
        if (classesRepository.existsByName(request.getName())) {
            throw new RuntimeException("Class with name '" + request.getName() + "' already exists.");
        }

        Classes toSave = Classes.builder()
                .teacherId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return classesRepository.save(toSave);
    }

    public List<ClassesDto> getAll() {
        List<Classes> classesList = classesRepository.findAll();
        return ClassesMapper.toDtoList(classesList);
    }

    public Classes getClassById(String id) {
        return findClassById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Classes updateClassById(ClassesUpdateRequest request) {
    	 // Fetch existing class entity by ID
        Classes toUpdate = findClassById(request.getId());

        // Validate and fetch the teacher ID
        if (request.getTeacherId() != null && !request.getTeacherId().isEmpty()) {
            String userId = getUserById(request.getTeacherId()).getId();
            toUpdate.setTeacherId(userId); // Update teacher ID if provided
        }

        // Update class details
        modelMapper.map(request, toUpdate);


        return classesRepository.save(toUpdate);
    }

    public void deleteClassById(String id) {
        classesRepository.deleteById(id);
    }

    public boolean authorizeCheck(String id, String principal) {
        return getUserById(findClassById(id).getTeacherId()).getUsername().equals(principal);
    }

    protected Classes findClassById(String id) {
        return classesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class not found"));
    }
}
