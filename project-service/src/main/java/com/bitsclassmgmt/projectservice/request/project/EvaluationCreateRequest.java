package com.bitsclassmgmt.projectservice.request.project;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EvaluationCreateRequest {

    @NotBlank(message = "Task ID is required")
    private String taskId; // The task being evaluated

    @NotBlank(message = "Evaluator ID is required")
    private String evaluatorId; // User who evaluates the task

    @Min(value = 0, message = "Score must be between 0 and 100")
    @Max(value = 100, message = "Score must be between 0 and 100")
    @NotNull(message = "Score is required")
    private Integer score; // Evaluation score

    private String comments; // Optional evaluation feedback

    private String fileId; // Optional: File reference in File Storage Service

}
