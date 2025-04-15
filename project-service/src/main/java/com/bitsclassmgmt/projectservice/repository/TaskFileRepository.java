package com.bitsclassmgmt.projectservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.projectservice.model.TaskFile;

public interface TaskFileRepository extends JpaRepository<TaskFile, String> {
	List<TaskFile> findByTaskId(String taskId);

}
