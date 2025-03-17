package com.bitsclassmgmt.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.projectservice.model.Task;

public interface TaskRepository extends JpaRepository<Task, String> {
    
}
