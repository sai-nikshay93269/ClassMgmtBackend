package com.bitsclassmgmt.chatservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
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

import com.bitsclassmgmt.chatservice.dto.ChatDto;
import com.bitsclassmgmt.chatservice.dto.SubProjectDto;
import com.bitsclassmgmt.chatservice.dto.TaskDto;
import com.bitsclassmgmt.chatservice.model.SubProject;
import com.bitsclassmgmt.chatservice.request.chat.ClassesUpdateRequest;
import com.bitsclassmgmt.chatservice.request.chat.ChatCreateRequest;
import com.bitsclassmgmt.chatservice.request.chat.SubProjectCreateRequest;
import com.bitsclassmgmt.chatservice.request.chat.TaskCreateRequest;
import com.bitsclassmgmt.chatservice.service.ChatService;
import com.bitsclassmgmt.chatservice.service.SubProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/project-service/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final ChatService projectService;
    private final SubProjectService subProjectService;
    private final ModelMapper modelMapper;

    
    @GetMapping("/getAll")
    public ResponseEntity<List<ChatDto>> getAllClassMembers() {
        return ResponseEntity.ok(subProjectService.getAll().stream()
                .map(advert -> modelMapper.map(advert, ChatDto.class)).toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ChatDto> getClassMembersById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(subProjectService.getAdvertById(id), ChatDto.class));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @advertService.authorizeCheck(#request.id, principal)")
    public ResponseEntity<ChatDto> updateClassMemberById(@Valid @RequestPart ClassesUpdateRequest request,
                                                      @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(subProjectService.updateAdvertById(request, file), ChatDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @advertService.authorizeCheck(#id, principal)")
    public ResponseEntity<Void> deleteClassMembersById(@PathVariable String id) {
    	subProjectService.deleteAdvertById(id);
        return ResponseEntity.ok().build();
    }
    

    
}
