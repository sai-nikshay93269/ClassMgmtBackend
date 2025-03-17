package com.bitsclassmgmt.chatservice.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_files")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskFile extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_files_task"))
    private Task task; // Reference to the Task entity

    @Column(name = "file_id", nullable = false)
    private String fileId; // UUID of the file stored in File Storage Service
}
