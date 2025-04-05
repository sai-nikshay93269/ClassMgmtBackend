package com.bitsclassmgmt.classesservice.mapper;

import com.bitsclassmgmt.classesservice.dto.GroupMembersDto;
import com.bitsclassmgmt.classesservice.dto.GroupsDto;
import com.bitsclassmgmt.classesservice.model.GroupMembers;
import com.bitsclassmgmt.classesservice.model.Groups;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupsMapper {

    public static GroupsDto toDto(Groups entity) {
        if (entity == null) return null;

        GroupsDto dto = new GroupsDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setClassId(entity.getClassEntity().getId());

        // Map group members if present
        if (entity.getGroupMembers() != null) {
            Set<GroupMembersDto> membersDto = entity.getGroupMembers().stream()
                .map(GroupsMapper::mapMember) // Make sure this accepts GroupMembers
                .collect(Collectors.toSet());
            dto.setGroupMembers(membersDto); // Ensure this setter and field exist
        }

        return dto;
    }

    private static GroupMembersDto mapMember(GroupMembers member) {
        GroupMembersDto dto = new GroupMembersDto();
        dto.setId(member.getId());
        dto.setGroupId(member.getGroupEntity().getId());
        dto.setStudentId(member.getStudentId());
        return dto;
    }

    public static List<GroupsDto> toDtoList(List<Groups> groupList) {
        return groupList.stream()
                .map(GroupsMapper::toDto)
                .collect(Collectors.toList());
    }
}
