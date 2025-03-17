package com.bitsclassmgmt.classesservice.request.classes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GroupsCreateRequest {
    @NotBlank(message = "Group name is required")
    private String name;
    @NotBlank(message = "Class id is required")
    private String classId;
}