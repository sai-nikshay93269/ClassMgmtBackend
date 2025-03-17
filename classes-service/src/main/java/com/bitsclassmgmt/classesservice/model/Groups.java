package com.bitsclassmgmt.classesservice.model;

import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity(name = "groups") 
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Groups extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false, foreignKey = @ForeignKey(name = "fk_group_class"))
    private Classes classEntity;  

    @Column(nullable = false, length = 255)
    private String name; 

}
