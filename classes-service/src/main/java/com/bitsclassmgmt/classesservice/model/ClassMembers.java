package com.bitsclassmgmt.classesservice.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "classMembers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassMembers extends BaseEntity {
	@ManyToOne
    @JoinColumn(name = "class_id", nullable = false, foreignKey = @ForeignKey(name = "fk_class_member_class"))
    private Classes classEntity;  // References Classes table

    private String studentId;  // This will store user_id from Auth Service
}
