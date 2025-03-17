package com.bitsclassmgmt.classesservice.request.classes;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassesUpdateRequest {

    @NotBlank(message = "Class ID is required")
    private String id;

    @NotBlank(message = "Class name is required")
    private String name;

    private String description;

    private String teacherId;
}
