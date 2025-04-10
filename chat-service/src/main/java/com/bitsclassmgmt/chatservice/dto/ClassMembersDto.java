package com.bitsclassmgmt.chatservice.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassMembersDto {
	private String id;  // ID of the class member
    private String classId;  // Class ID (instead of the full Class entity)
    private String studentId;  // Student (user) ID

}
