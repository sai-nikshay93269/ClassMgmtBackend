package com.bitsclassmgmt.classesservice.model;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "classes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Classes extends BaseEntity {
    private String teacherId;
    private String name;
    private String description;

}
