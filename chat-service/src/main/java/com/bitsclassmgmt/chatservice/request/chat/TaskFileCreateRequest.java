package com.bitsclassmgmt.chatservice.request.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class TaskFileCreateRequest {
    @NotBlank(message = "File ID is required")
    private String fileId; // UUID from FilesMetadata in File Storage Service
}
