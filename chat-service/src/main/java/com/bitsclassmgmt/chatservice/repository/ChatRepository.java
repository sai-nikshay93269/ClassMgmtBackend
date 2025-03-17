package com.bitsclassmgmt.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.chatservice.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, String> {
    
}
