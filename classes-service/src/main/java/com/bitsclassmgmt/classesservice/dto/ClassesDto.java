package com.bitsclassmgmt.classesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassesDto {
    private String id;
    private String name;
    private String description;
    private String teacherId;
    private Set<ClassMembersDto> members;
}
