package com.bitsclassmgmt.projectservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.projectservice.model.Project;

public interface ProjectRepository extends JpaRepository<Project, String> {
	List<Project> findByClassId(String classId);
}
