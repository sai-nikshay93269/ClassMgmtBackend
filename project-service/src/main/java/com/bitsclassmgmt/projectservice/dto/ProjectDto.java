package com.bitsclassmgmt.projectservice.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto {
    private String id;
    private String classId; 
    private String title;
    private String description;
    private LocalDateTime dueDate;
}
