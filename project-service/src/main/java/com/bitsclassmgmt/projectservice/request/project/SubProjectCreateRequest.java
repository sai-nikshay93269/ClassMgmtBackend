package com.bitsclassmgmt.projectservice.request.project;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SubProjectCreateRequest {

    @NotBlank(message = "Project ID is required")
    private String projectId; 

    private String groupId;

    @NotBlank(message = "Project title is required")
    private String title;

    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;
}
