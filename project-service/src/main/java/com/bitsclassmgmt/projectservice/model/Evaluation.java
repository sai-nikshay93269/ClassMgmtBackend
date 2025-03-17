package com.bitsclassmgmt.projectservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "evaluations")  
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Evaluation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false, foreignKey = @ForeignKey(name = "fk_evaluation_task"))
    private Task task; // Links evaluation to a Task entity

    @Column(nullable = false)
    private String evaluatorId; // Who evaluated the task

    @Column(nullable = false)
    private Integer score; // Score between 0-100

    @Column(columnDefinition = "TEXT")
    private String comments; // Optional feedback

    private String fileId; // Optional file reference (e.g., for uploaded evaluations)
}
