package com.bitsclassmgmt.projectservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluationDto {
    private String id;
    private String taskId; // Reference to the task
    private String evaluatorId; // User who evaluated the task
    private Integer score; // Score between 0 and 100
    private String comments; // Feedback from the evaluator
    private String fileId; // Optional: Reference to a file in File Storage Service
    private LocalDateTime uploadedAt; // Timestamp of evaluation submission
}
