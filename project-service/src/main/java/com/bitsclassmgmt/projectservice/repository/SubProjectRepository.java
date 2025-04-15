package com.bitsclassmgmt.projectservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.projectservice.model.SubProject;

public interface SubProjectRepository extends JpaRepository<SubProject, String> {
	List<SubProject> findByProjectId(String projectId);
}
