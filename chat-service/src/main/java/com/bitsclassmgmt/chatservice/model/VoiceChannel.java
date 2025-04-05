package com.bitsclassmgmt.chatservice.model;

import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity(name = "voiceChannel")  
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoiceChannel extends BaseEntity {


    private String classId; // Nullable if it's a group or private call
    private String groupId; // Nullable if it's a class-wide or private call

    @Column(nullable = false)
    private boolean isActive = true;
}
