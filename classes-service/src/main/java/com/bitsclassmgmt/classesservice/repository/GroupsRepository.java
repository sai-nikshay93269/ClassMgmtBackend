package com.bitsclassmgmt.classesservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.classesservice.model.Groups;

public interface GroupsRepository extends JpaRepository<Groups, String> {
    List<Groups> findByClassEntityId(String classId);

    // ✅ New method to check if group name exists in a class
    boolean existsByNameAndClassEntity_Id(String name, String classId);
}
