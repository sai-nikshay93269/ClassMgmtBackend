package com.bitsclassmgmt.chatservice.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDto {
    private String id;
    private String classId; // Nullable if it's a direct message
    private String groupId; // Nullable if not a group chat
    private String senderId;
    private String receiverId; // Nullable if it's a group message
    private String message;
    private Boolean hasAttachment;
    private LocalDateTime timestamp;
}
