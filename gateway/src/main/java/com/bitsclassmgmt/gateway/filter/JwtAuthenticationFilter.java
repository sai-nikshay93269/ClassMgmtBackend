package com.bitsclassmgmt.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.bitsclassmgmt.gateway.util.JwtUtil;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> publicEndpoints = List.of(
                "/v1/auth/login",
                "/v1/auth/register",
                "/eureka",
                "/ws/**"  // <-- Add this to skip JWT for WebSocket
        );

        Predicate<ServerHttpRequest> isSecured = r -> publicEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().startsWith(uri));

        // Skip filtering for WebSocket upgrade requests
        boolean isWebSocket = "websocket".equalsIgnoreCase(request.getHeaders().getUpgrade());

        if (isSecured.test(request)) {
            if (isWebSocketPath(request)) {
                return chain.filter(exchange); // Skip WebSocket JWT
            }

            String token = extractToken(request);
            if (token == null || token.isEmpty()) {
                return onError(exchange);
            }

            try {
                jwtUtil.validateToken(token);
            } catch (Exception e) {
                return onError(exchange);
            }
        }


        return chain.filter(exchange);
    }

    private boolean isWebSocketPath(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return path.startsWith("/ws/chat"); // Any WS endpoint you want to allow without JWT
    }

    
    private String extractToken(ServerHttpRequest request) {
        // 1. Try from Authorization header
        List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
        if (!authHeaders.isEmpty()) {
            String bearer = authHeaders.get(0);
            if (bearer.startsWith("Bearer ")) {
                return bearer.substring(7);
            }
        }

        // 2. Try from query param (for WebSocket connections)
        return request.getQueryParams().getFirst("token");
    }


    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}