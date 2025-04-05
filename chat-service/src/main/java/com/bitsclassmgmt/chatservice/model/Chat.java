package com.bitsclassmgmt.chatservice.model;

import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity(name = "chats")  
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Chat extends BaseEntity {

    private String classId; // Nullable if it's a direct message
    private String groupId; // Nullable if not a group chat

    @Column(nullable = false)
    private String senderId;

    private String receiverId; // Nullable if it's a group message

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private Boolean hasAttachment = false;

    @CreationTimestamp
    private LocalDateTime timestamp; // Auto-generated timestamp

    // New properties to support different message types
    private String type; // "msg", "divider"
    private String subtype; // "img", "doc", "link", "reply"
    private String img; // URL for image messages
    private String preview; // Preview image for links
    private String reply; // Message text for replies
    private String fileUrl; // URL for file attachments
    private String dividerText; // Text for divider messages
}
