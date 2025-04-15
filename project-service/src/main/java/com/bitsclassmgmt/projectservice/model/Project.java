package com.bitsclassmgmt.projectservice.model;

import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity(name = "projects")  
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project extends BaseEntity {

    private String classId;  

    private String title; 

    @Column(columnDefinition = "TEXT")
    private String description; 


    private LocalDateTime dueDate;  
}
