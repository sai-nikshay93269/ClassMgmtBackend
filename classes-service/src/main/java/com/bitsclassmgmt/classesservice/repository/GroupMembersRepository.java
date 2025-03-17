package com.bitsclassmgmt.classesservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.classesservice.model.ClassMembers;
import com.bitsclassmgmt.classesservice.model.GroupMembers;

public interface GroupMembersRepository extends JpaRepository<GroupMembers, String> {
	List<GroupMembers> findByGroupEntityId(String groupId);
}
