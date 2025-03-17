package com.bitsclassmgmt.chatservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto {
    private String id;
    private String projectId;
    private String subProjectId; // Nullable if the task is directly under a project
    private String assignedTo; // User ID of the student assigned
    private String title;
    private String description;
    private String status; // ENUM: Pending, In Progress, Completed
    private LocalDateTime dueDate;
}
