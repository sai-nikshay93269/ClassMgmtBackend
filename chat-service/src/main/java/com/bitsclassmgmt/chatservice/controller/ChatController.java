package com.bitsclassmgmt.chatservice.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import com.bitsclassmgmt.chatservice.model.Chat;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.chatservice.dto.ChatDto;
import com.bitsclassmgmt.chatservice.request.chat.ChatCreateRequest;
import com.bitsclassmgmt.chatservice.request.chat.ChatFilterRequest;
import com.bitsclassmgmt.chatservice.request.chat.ClassesUpdateRequest;
import com.bitsclassmgmt.chatservice.service.ChatService;

@RestController
@RequestMapping("/v1/chat-service/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ModelMapper modelMapper;

    @PostMapping("/message")
    public ResponseEntity<ChatDto> createChat(@Valid @RequestBody ChatCreateRequest request) {
   	System.out.println(request);
        Chat savedChat = chatService.createChat(request); // ✅ Now handles RabbitMQ too
       return ResponseEntity.status(HttpStatus.CREATED)
             .body(modelMapper.map(savedChat, ChatDto.class));
   }

    @GetMapping("/getAll")
    public ResponseEntity<List<ChatDto>> getAllChats() {
        return ResponseEntity.ok(chatService.getAll().stream()
                .map(advert -> modelMapper.map(advert, ChatDto.class)).toList());
    }
    
    @PostMapping("/history")
    public ResponseEntity<List<ChatDto>> getFilteredChats(@RequestBody ChatFilterRequest filterRequest) {
        List<Chat> chats = chatService.getFilteredChats(filterRequest);
        List<ChatDto> dtos = chats.stream()
                .map(chat -> modelMapper.map(chat, ChatDto.class))
                .toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ChatDto> getClassesById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(chatService.getAdvertById(id), ChatDto.class));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @advertService.authorizeCheck(#request.id, principal)")
    public ResponseEntity<ChatDto> updateClassesById(@Valid @RequestPart ClassesUpdateRequest request,
                                                     @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(chatService.updateAdvertById(request, file), ChatDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @advertService.authorizeCheck(#id, principal)")
    public ResponseEntity<Void> deleteClassesById(@PathVariable String id) {
        chatService.deleteAdvertById(id);
        return ResponseEntity.ok().build();
    }
}
