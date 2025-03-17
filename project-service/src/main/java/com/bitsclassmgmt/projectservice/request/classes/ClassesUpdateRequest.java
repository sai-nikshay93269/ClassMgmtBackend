package com.bitsclassmgmt.projectservice.request.classes;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ClassesUpdateRequest {
    @NotBlank(message = "Classes")
    private String id;
    private String name;
    private String description;
}
