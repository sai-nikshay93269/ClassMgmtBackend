package com.bitsclassmgmt.classesservice.request.classes;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GroupMembersCreateRequest {
	@NotBlank(message = "Group id is required")
    private String groupId;
    @NotEmpty(message = "Student IDs list cannot be empty")
    private List<String> studentIds;
}
