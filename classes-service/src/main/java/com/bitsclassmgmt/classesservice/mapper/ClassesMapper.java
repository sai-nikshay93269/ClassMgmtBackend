package com.bitsclassmgmt.classesservice.mapper;

import com.bitsclassmgmt.classesservice.dto.ClassMembersDto;
import com.bitsclassmgmt.classesservice.dto.ClassesDto;
import com.bitsclassmgmt.classesservice.model.ClassMembers;
import com.bitsclassmgmt.classesservice.model.Classes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassesMapper {

    public static ClassesDto toDto(Classes entity) {
        if (entity == null) return null;

        ClassesDto dto = new ClassesDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setTeacherId(entity.getTeacherId());

        if (entity.getMembers() != null) {
            Set<ClassMembersDto> memberDtos = entity.getMembers().stream()
                .map(ClassesMapper::mapMember)
                .collect(Collectors.toSet());
            dto.setMembers(memberDtos);
        }


        return dto;
    }

    private static ClassMembersDto mapMember(ClassMembers member) {
        ClassMembersDto dto = new ClassMembersDto();
        dto.setId(member.getId());
        dto.setClassId(member.getClassEntity().getId()); 
        dto.setStudentId(member.getStudentId());
        return dto;
    }


    public static List<ClassesDto> toDtoList(List<Classes> classList) {
        return classList.stream()
                .map(ClassesMapper::toDto)
                .collect(Collectors.toList());
    }
}
