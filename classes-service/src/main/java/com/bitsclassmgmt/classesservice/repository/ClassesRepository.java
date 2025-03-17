package com.bitsclassmgmt.classesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.classesservice.model.Classes;

public interface ClassesRepository extends JpaRepository<Classes, String> {
    
}
