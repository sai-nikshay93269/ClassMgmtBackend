package com.bitsclassmgmt.classesservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bitsclassmgmt.classesservice.model.ClassMembers;
import com.bitsclassmgmt.classesservice.model.GroupMembers;

public interface GroupMembersRepository extends JpaRepository<GroupMembers, String> {
	List<GroupMembers> findByGroupEntityId(String groupId);
	
	List<GroupMembers> findByStudentIdAndGroupEntity_ClassEntity_Id(String studentId, String classId);



}
