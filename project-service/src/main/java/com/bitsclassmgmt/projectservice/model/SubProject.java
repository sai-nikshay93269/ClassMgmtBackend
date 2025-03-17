package com.bitsclassmgmt.projectservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "sub_projects")  
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubProject extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sub_project_project"))
    private Project project; 

    @Column(name = "group_id", nullable = true)
    private String groupId; 

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDateTime dueDate;
}
