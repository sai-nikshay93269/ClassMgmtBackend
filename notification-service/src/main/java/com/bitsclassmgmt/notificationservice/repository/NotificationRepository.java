package com.bitsclassmgmt.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.notificationservice.model.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findAllByUserIdOrderByCreationTimestampDesc(String id);
}
