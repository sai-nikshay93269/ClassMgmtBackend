package com.bitsclassmgmt.projectservice.request.project;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ProjectUpdateRequest {

    @NotBlank(message = "Project ID is required")
    private String id;

    @NotBlank(message = "Project title is required")
    private String title;

    @NotBlank(message = "Project description is required")
    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    @NotBlank(message = "Class ID is required")
    private String classId;
}
