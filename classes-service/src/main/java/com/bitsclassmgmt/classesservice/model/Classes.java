package com.bitsclassmgmt.classesservice.model;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Entity(name = "classes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Classes extends BaseEntity {

    private String teacherId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ClassMembers> members;
}