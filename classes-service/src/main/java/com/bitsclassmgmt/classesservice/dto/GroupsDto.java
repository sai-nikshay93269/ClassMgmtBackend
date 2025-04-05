package com.bitsclassmgmt.classesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupsDto {
    private String id;
    private String name;
    private String classId;
    private Set<GroupMembersDto> groupMembers;
}
