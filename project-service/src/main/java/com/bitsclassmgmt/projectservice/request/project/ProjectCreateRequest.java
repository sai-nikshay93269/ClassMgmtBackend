package com.bitsclassmgmt.projectservice.request.project;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProjectCreateRequest {
    @NotBlank(message = "Project title is required")
    private String title;

    @NotBlank(message = "Project description is required")
    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    @NotBlank(message = "Class ID is required")
    private String classId;
}
