package com.bitsclassmgmt.chatservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bitsclassmgmt.chatservice.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "projects")  
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "subproject_id", nullable = true, foreignKey = @ForeignKey(name = "fk_task_sub_project"))
    private SubProject subProject; // Nullable if task is directly under a project
    
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_project"))
    private Chat project; // Direct project association

    
    private String assignedTo; // UUID of the assigned student
    
    
    private String title;


    private String description;
    

    private LocalDateTime dueDate;  
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
