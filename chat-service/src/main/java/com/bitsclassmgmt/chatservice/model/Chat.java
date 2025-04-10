package com.bitsclassmgmt.chatservice.model;

import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "chats")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Chat extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String classId;
    private String groupId;

    @Column(nullable = false)
    private String senderId;

    private String receiverId;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean hasAttachment;

    @CreationTimestamp
    private LocalDateTime timestamp;

    // New properties to support different message types
    private String type;
    private String subtype;
    private String img;
    private String preview;
    private String reply;
    private String fileUrl;
    private String dividerText;
}
