package com.bitsclassmgmt.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.chatservice.model.Task;

public interface TaskRepository extends JpaRepository<Task, String> {
    
}
