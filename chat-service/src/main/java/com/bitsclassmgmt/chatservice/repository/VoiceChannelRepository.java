package com.bitsclassmgmt.chatservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.chatservice.model.Chat;
import com.bitsclassmgmt.chatservice.model.VoiceChannel;

public interface VoiceChannelRepository extends JpaRepository<VoiceChannel, String> {
	List<VoiceChannel> findByClassId(String classId);
    List<VoiceChannel> findByGroupId(String groupId);
    List<VoiceChannel> findByIsActiveTrue();
}
