package com.bitsclassmgmt.chatservice.request.chat;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class VoiceChannelCreateRequest {
    private String classId; // Nullable
    private String groupId; // Nullable
}
