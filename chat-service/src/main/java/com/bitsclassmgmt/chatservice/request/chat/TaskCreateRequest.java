package com.bitsclassmgmt.chatservice.request.chat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TaskCreateRequest {

    @NotBlank(message = "Project ID is required")
    private String projectId;

    private String subProjectId; // Nullable if task is directly under a project

    @NotBlank(message = "Assigned user ID is required")
    private String assignedTo; // User ID of the student assigned

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotNull(message = "Task due date is required")
    private LocalDateTime dueDate;
}
