package com.bitsclassmgmt.classesservice.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Entity(name = "classMembers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassMembers extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false, foreignKey = @ForeignKey(name = "fk_class_member_class"))
    @JsonBackReference
    private Classes classEntity;

    private String studentId;  // user_id from Auth Service
}
