package com.bitsclassmgmt.projectservice.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubProjectDto {
    private String id;
    private String projectId; // Updated to reference the correct foreign key
    private String groupId; // Added for optional group linkage
    private String title;
    private LocalDateTime dueDate;
    private String description;
}
