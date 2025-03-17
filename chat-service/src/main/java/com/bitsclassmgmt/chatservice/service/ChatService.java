package com.bitsclassmgmt.chatservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.chatservice.client.ClassesServiceClient;
import com.bitsclassmgmt.chatservice.client.GroupsServiceClient;
import com.bitsclassmgmt.chatservice.client.UserServiceClient;
import com.bitsclassmgmt.chatservice.dto.ClassesDto;
import com.bitsclassmgmt.chatservice.dto.GroupsDto;
import com.bitsclassmgmt.chatservice.dto.UserDto;
import com.bitsclassmgmt.chatservice.exc.NotFoundException;
import com.bitsclassmgmt.chatservice.model.Chat;
import com.bitsclassmgmt.chatservice.repository.ChatRepository;
import com.bitsclassmgmt.chatservice.request.chat.ChatCreateRequest;
import com.bitsclassmgmt.chatservice.request.chat.ClassesUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
    private final UserServiceClient userServiceClient;
    private final ClassesServiceClient classesServiceClient;
    private final GroupsServiceClient groupsServiceClient;
    private final ModelMapper modelMapper;

    public Chat createChat(ChatCreateRequest request) {
        // Validate class/group existence if provided
        if (request.getClassId() != null) {
            getClassById(request.getClassId());
        }
        
     // Validate class/group existence if provided
        if (request.getGroupId() != null) {
            getGroupById(request.getGroupId());
        }


        // Validate receiver if it's a private message
        if (request.getReceiverId() != null) {
            getUserById(request.getReceiverId());
        }

        // Create and save the chat message
        Chat chat = Chat.builder()
                .classId(request.getClassId())  // Nullable for private messages
                .groupId(request.getGroupId())  // Nullable if not a group chat
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId()) // Nullable for group messages
                .message(request.getMessage())
                .hasAttachment(false)
                .build();

        return chatRepository.save(chat);
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
        return Optional.ofNullable(classesServiceClient.getClassesById(id).getBody())
                .orElseThrow(() -> new NotFoundException("Class not found"));
    }

    public GroupsDto getGroupById(String id) {
        return Optional.ofNullable(groupsServiceClient.getGroupsById(id).getBody())
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

	public boolean authorizeCheck(String id, String principal) {
        return getUserById(getAdvertById(id).getUserId()).getUsername().equals(principal);
    }

    protected Chat findAdvertById(String id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advert not found"));
    }
}
