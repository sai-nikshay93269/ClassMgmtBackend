package com.bitsclassmgmt.projectservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.projectservice.model.Task;

public interface TaskRepository extends JpaRepository<Task, String> {
	List<Task> findByProjectId(String projectId);
	List<Task> findBySubProjectId(String projectId);
}
