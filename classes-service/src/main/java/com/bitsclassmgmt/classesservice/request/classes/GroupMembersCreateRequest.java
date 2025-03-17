package com.bitsclassmgmt.classesservice.request.classes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GroupMembersCreateRequest {
	@NotBlank(message = "Group id is required")
    private String groupId;
    @NotBlank(message = "Student id is required")
    private String studentId;
}
