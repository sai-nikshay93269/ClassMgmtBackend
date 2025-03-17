package com.bitsclassmgmt.classesservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.classesservice.client.UserServiceClient;
import com.bitsclassmgmt.classesservice.dto.UserDto;
import com.bitsclassmgmt.classesservice.exc.NotFoundException;
import com.bitsclassmgmt.classesservice.model.ClassMembers;
import com.bitsclassmgmt.classesservice.model.Classes;
import com.bitsclassmgmt.classesservice.repository.ClassMembersRepository;
import com.bitsclassmgmt.classesservice.repository.ClassesRepository;
import com.bitsclassmgmt.classesservice.request.classes.ClassMembersCreateRequest;
import com.bitsclassmgmt.classesservice.request.classes.ClassesUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassMembersService {
    private final ClassMembersRepository classMembersRepository;
    private final ClassesRepository classesRepository;
    private final UserServiceClient userServiceclient;
    private final ModelMapper modelMapper;

    public ClassMembers createClassMembers(ClassMembersCreateRequest request) {
    	
    	String studentId = getUserById(request.getStudentId()).getId();
        // Fetch the class entity using classId
        Classes classEntity = classesRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + request.getClassId()));

        // Create and save class member
        ClassMembers toSave = ClassMembers.builder()
                .studentId(studentId)  // Fix: Use request.getStudentId()
                .classEntity(classEntity)  // Fix: Assign class entity correctly
                .build();
        
        return classMembersRepository.save(toSave);
    }

    public List<ClassMembers> getAll() {
        return classMembersRepository.findAll();
    }


    public List<ClassMembers> getMembersByClassId(String classId) {
        return classMembersRepository.findByClassEntityId(classId);
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
