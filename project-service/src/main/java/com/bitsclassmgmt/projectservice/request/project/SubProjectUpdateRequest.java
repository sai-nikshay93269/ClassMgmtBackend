package com.bitsclassmgmt.projectservice.request.project;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SubProjectUpdateRequest {

    @NotBlank(message = "Subproject ID is required")
    private String id;

    @NotBlank(message = "Project ID is required")
    private String projectId;

    private String groupId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;
}
