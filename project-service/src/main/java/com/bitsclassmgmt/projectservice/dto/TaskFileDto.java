package com.bitsclassmgmt.projectservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskFileDto {
    private String id;
    private String taskId; // Reference to the task
    private String fileId; // Reference to the file in File Storage Service
    private LocalDateTime uploadedAt;
}
