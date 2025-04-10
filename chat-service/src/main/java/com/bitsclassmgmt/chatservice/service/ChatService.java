package com.bitsclassmgmt.chatservice.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.chatservice.client.ClassGroupServiceClient;
import com.bitsclassmgmt.chatservice.client.UserServiceClient;
import com.bitsclassmgmt.chatservice.dto.ClassesDto;
import com.bitsclassmgmt.chatservice.dto.GroupsDto;
import com.bitsclassmgmt.chatservice.dto.UserDto;
import com.bitsclassmgmt.chatservice.exc.NotFoundException;
import com.bitsclassmgmt.chatservice.model.Chat;
import com.bitsclassmgmt.chatservice.repository.ChatRepository;
import com.bitsclassmgmt.chatservice.request.chat.ChatCreateRequest;
import com.bitsclassmgmt.chatservice.request.chat.ChatFilterRequest;
import com.bitsclassmgmt.chatservice.request.chat.ClassesUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
    private final UserServiceClient userServiceClient;
    private final ClassGroupServiceClient classGroupServiceClient;
    private final ModelMapper modelMapper;
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange chatExchange;
    private final RabbitAdmin rabbitAdmin;
    private final ObjectMapper objectMapper;

    public Chat createChat(ChatCreateRequest request) {
        // Validate class existence if provided
        if (request.getClassId() != null) {
            getClassById(request.getClassId());
        }

        // Validate group existence if provided
        if (request.getGroupId() != null) {
            getGroupById(request.getGroupId());
        }

        // Validate receiver if it's a private message
        if (request.getReceiverId() != null) {
            getUserById(request.getReceiverId());
        }

        // Create and save the chat message
        Chat chat = Chat.builder()
                .classId(request.getClassId()) // Nullable for private messages
                .groupId(request.getGroupId()) // Nullable if not a group chat
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId()) // Nullable for group messages
                .message(request.getMessage())
                .hasAttachment(request.getHasAttachment() != null ? request.getHasAttachment() : false)
                .timestamp(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now()) // Ensure timestamp is set

                // New properties from ChatCreateRequest
                .type(request.getType())
                .subtype(request.getSubtype())
                .img(request.getImg())
                .preview(request.getPreview())
                .reply(request.getReply())
                .fileUrl(request.getFileUrl())
                .dividerText(request.getDividerText())
                .build();
        
        Chat savedChat = chatRepository.save(chat);

        // 🐇 Build payload and send to RabbitMQ
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", savedChat.getId());
            payload.put("room", getRoomKey(savedChat));
            payload.put("senderId", savedChat.getSenderId());
            payload.put("message", savedChat.getMessage());
            payload.put("timestamp", savedChat.getTimestamp());
            payload.put("hasAttachment", savedChat.getHasAttachment());
            payload.put("type", savedChat.getType());
            payload.put("subtype", savedChat.getSubtype());
            payload.put("img", savedChat.getImg());
            payload.put("preview", savedChat.getPreview());
            payload.put("reply", savedChat.getReply());
            payload.put("fileUrl", savedChat.getFileUrl());

            String routingKey = getRoutingKey(savedChat);
            String json = objectMapper.writeValueAsString(payload);

            rabbitTemplate.convertAndSend(chatExchange.getName(), routingKey, payload); // ✅ send Map directly


            System.out.println("📤 Published to RabbitMQ [" + routingKey + "]: " + json);

        } catch (JsonProcessingException e) {
            System.err.println("❌ Failed to publish to RabbitMQ: " + e.getMessage());
        }

        
        return savedChat;
    }
    private String getRoomKey(Chat chat) {
        if (chat.getGroupId() != null) {
            return "group-" + chat.getGroupId();
        } else if (chat.getClassId() != null) {
            return "class-" + chat.getClassId();
        } else if (chat.getReceiverId() != null) {
            return "user-" + chat.getReceiverId();
        } else {
            return "general";
        }
    }

    private String getRoutingKey(Chat chat) {
        if (chat.getGroupId() != null) {
            return "group-" + chat.getGroupId() + ".message";
        } else if (chat.getClassId() != null) {
            return "class-" + chat.getClassId() + ".message";
        } else if (chat.getReceiverId() != null) {
            return "user-" + chat.getReceiverId() + ".message";
        } else {
            return "general.message";
        }
    }


    public List<Chat> getAll() {
        return chatRepository.findAll();
    }

    public Chat getAdvertById(String id) {
        return findAdvertById(id);
    }



    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceClient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public ClassesDto getClassById(String id) {
        return Optional.ofNullable(classGroupServiceClient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("Class not found"));
    }

    public GroupsDto getGroupById(String id) {
        return Optional.ofNullable(classGroupServiceClient.getGroupsById(id).getBody())
                .orElseThrow(() -> new NotFoundException("Group not found"));
    }

    public Chat updateAdvertById(ClassesUpdateRequest request, MultipartFile file) {
        Chat toUpdate = findAdvertById(request.getId());
        modelMapper.map(request, toUpdate);


        return chatRepository.save(toUpdate);
    }

    public void deleteAdvertById(String id) {
    	chatRepository.deleteById(id);
    }


    protected Chat findAdvertById(String id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advert not found"));
    }
    
    public List<Chat> getFilteredChats(ChatFilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(filterRequest.getOffset(), filterRequest.getLimit());

        return chatRepository.findByGroupIdOrClassId(
            filterRequest.getGroupId(),
            filterRequest.getClassId(),
            pageable
        );
    }
}
