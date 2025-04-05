package com.bitsclassmgmt.classesservice.model;

import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity(name = "groups")
@Table(name = "groups", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "class_id"})
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Groups extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false, foreignKey = @ForeignKey(name = "fk_group_class"))
    private Classes classEntity;

    @Column(nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "groupEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<GroupMembers> groupMembers;
}