package com.bitsclassmgmt.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bitsclassmgmt.gateway.filter.JwtAuthenticationFilter;

@Configuration
public class GatewayConfig {
    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/v1/user/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))

                .route("job-service", r -> r.path("/v1/classes-service/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://classes-service"))
                .route("job-service", r -> r.path("/v1/chat-service/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://chat-service"))
                .route("notification-se rvice", r -> r.path("/v1/notification/**")
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