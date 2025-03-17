package com.bitsclassmgmt.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.projectservice.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, String> {
    
}
