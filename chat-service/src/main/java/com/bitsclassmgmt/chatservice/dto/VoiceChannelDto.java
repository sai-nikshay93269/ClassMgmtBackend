package com.bitsclassmgmt.chatservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VoiceChannelDto {
    private UUID id;
    private String classId;
    private String groupId;
    private LocalDateTime createdAt;
    private boolean isActive;
}
