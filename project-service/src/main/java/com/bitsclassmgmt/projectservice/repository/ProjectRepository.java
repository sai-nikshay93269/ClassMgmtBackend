package com.bitsclassmgmt.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.projectservice.model.Project;

public interface ProjectRepository extends JpaRepository<Project, String> {
    
}
