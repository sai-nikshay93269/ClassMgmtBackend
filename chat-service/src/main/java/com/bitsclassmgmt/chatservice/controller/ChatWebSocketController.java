package com.bitsclassmgmt.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
@RequestMapping("/v1/chat-service/websocket")
@EnableWebSocket
@RequiredArgsConstructor
public class ChatWebSocketController implements WebSocketConfigurer {

    // Stores active chat rooms and their connected WebSocket sessions
    private static final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(), "/chat/{roomName}").setAllowedOrigins("*");
    }

    private static class ChatWebSocketHandler extends TextWebSocketHandler {

        @Override
        public void afterConnectionEstablished(WebSocketSession session) {
            String roomName = getRoomName(session);
            roomSessions.computeIfAbsent(roomName, k -> new CopyOnWriteArraySet<>()).add(session);
            System.out.println("✅ WebSocket connected for room: " + roomName);
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            String roomName = getRoomName(session);
            System.out.println("📩 Received in room " + roomName + ": " + message.getPayload());

            // Broadcast message to all users in the room
            for (WebSocketSession s : roomSessions.getOrDefault(roomName, Set.of())) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(message.getPayload()));
                }
            }
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) {
            System.err.println("⚠️ WebSocket error: " + exception.getMessage());
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
            String roomName = getRoomName(session);
            roomSessions.getOrDefault(roomName, Set.of()).remove(session);
            System.out.println("❌ WebSocket closed for room: " + roomName);
        }

        private String getRoomName(WebSocketSession session) {
            return session.getUri().getPath().split("/chat/")[1];
        }
    }
}
