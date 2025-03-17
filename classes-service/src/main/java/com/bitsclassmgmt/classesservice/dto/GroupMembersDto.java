package com.bitsclassmgmt.classesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupMembersDto {
	private String id;  // ID of the class member
    private String groupId;  // Class ID (instead of the full Class entity)
    private String studentId;  // Student (user) ID

}
