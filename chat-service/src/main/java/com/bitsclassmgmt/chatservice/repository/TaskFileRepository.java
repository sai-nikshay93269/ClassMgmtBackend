package com.bitsclassmgmt.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.chatservice.model.TaskFile;

public interface TaskFileRepository extends JpaRepository<TaskFile, String> {
    
}
