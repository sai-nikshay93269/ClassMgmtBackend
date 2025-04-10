package com.bitsclassmgmt.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingGatewayFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();

        System.out.println("=== Gateway Forwarding ===");
        System.out.println("Path: " + request.getURI().getPath());
        System.out.println("Full URI: " + request.getURI());
        System.out.println("Headers: ");
        request.getHeaders().forEach((name, values) ->
            System.out.println(name + ": " + String.join(", ", values))
        );
        System.out.println("==========================");

        return chain.filter(exchange);
    }
}
