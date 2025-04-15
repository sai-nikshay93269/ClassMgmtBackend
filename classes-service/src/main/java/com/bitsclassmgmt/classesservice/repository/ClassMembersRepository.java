package com.bitsclassmgmt.classesservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.classesservice.model.ClassMembers;

public interface ClassMembersRepository extends JpaRepository<ClassMembers, String> {
	List<ClassMembers> findByClassEntityId(String classId);
	List<ClassMembers> findByStudentId(String studentId);

}
