package com.bitsclassmgmt.classesservice.request.classes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ClassesCreateRequest {
    @NotBlank(message = "Class name is required")
    private String name;
    private String description;
    @NotBlank(message = "User id is required")
    private String teacherId;
}
