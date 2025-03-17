package com.bitsclassmgmt.projectservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassesDto {
    private String id;
    private String name;
    private String description;
    private String teacherId;
}
