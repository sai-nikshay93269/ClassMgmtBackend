package com.bitsclassmgmt.classesservice.request.classes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ClassMembersCreateRequest {
	@NotBlank(message = "Class id is required")
    private String classId;
    @NotBlank(message = "Student id is required")
    private String studentId;
}
