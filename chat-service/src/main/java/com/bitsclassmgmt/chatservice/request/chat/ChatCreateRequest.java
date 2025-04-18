package com.bitsclassmgmt.chatservice.request.chat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatCreateRequest {

    private String classId; // Nullable if it's a direct message
    private String groupId; // Nullable if not a group chat

    @NotNull(message = "Sender ID is required")
    private String senderId;

    private String receiverId; // Nullable if it's a group message

    @NotBlank(message = "Message content cannot be empty")
    private String message;

    private Boolean hasAttachment = false;
    
    private LocalDateTime timestamp = LocalDateTime.now();

    // New properties from Chat_History
    private String type; // "msg", "divider"
    private String subtype; // "img", "doc", "link", "reply"
    private String img; // URL of image (if message is an image)
    private String preview; // Preview image (for links)
    private String reply; // Original message being replied to
    private String fileUrl; // URL for document/file messages
    private String dividerText; // Used for "divider" type (e.g., "Today")
}
