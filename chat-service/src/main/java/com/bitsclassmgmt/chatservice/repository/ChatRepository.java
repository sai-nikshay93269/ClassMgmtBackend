package com.bitsclassmgmt.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.bitsclassmgmt.chatservice.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, String> {
	@Query("SELECT c FROM chats c " +
		       "WHERE (:groupId IS NULL OR c.groupId = :groupId) " +
		       "AND (:classId IS NULL OR c.classId = :classId) " +
		       "ORDER BY c.timestamp DESC")
		List<Chat> findByGroupIdOrClassId(@Param("groupId") String groupId,
		                                  @Param("classId") String classId,
		                                  Pageable pageable);
}
