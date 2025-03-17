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

@Entity(name = "groupMembers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupMembers extends BaseEntity {
	@ManyToOne
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_group_member_class"))
    private Groups groupEntity;  // References Classes table

    private String studentId;  // This will store user_id from Auth Service
}
