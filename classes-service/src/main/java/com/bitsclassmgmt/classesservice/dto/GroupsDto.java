package com.bitsclassmgmt.classesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupsDto {
    private String id;
    private String name;
    private String classId; 
}
