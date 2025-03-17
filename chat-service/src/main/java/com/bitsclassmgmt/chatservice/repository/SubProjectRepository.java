package com.bitsclassmgmt.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.chatservice.model.SubProject;

public interface SubProjectRepository extends JpaRepository<SubProject, String> {
    
}
