package com.bitsclassmgmt.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bitsclassmgmt.gateway.filter.JwtAuthenticationFilter;
import com.bitsclassmgmt.gateway.filter.LoggingGatewayFilter;

@Configuration
public class GatewayConfig {
    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

               .route("user-service-asdasdsa", r -> r.path("/v1/user/**")
                        .filters(f -> f.filter(filter).filter(new LoggingGatewayFilter()))
                        .uri("lb://user-service"))

                .route("classes-service", r -> r.path("/v1/classes-service/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://classes-service"))

                // Optional: fallback for chat-service REST endpoints
                .route("chat-service", r -> r.path("/v1/chat-service/chat/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://chat-service"))

                .route("notification-service", r -> r.path("/v1/notification/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://notification-service"))

                .route("auth-service", r -> r.path("/v1/auth/**")
                        .uri("lb://auth-service"))

                .route("file-storage", r -> r.path("/v1/file-storage/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://file-storage"))

                .build();
    }
}
