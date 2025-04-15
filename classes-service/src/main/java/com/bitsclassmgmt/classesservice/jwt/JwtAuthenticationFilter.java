package com.bitsclassmgmt.classesservice.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization");

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                Claims claims = jwtUtil.getClaims(token.substring(7)); // Extract the token (remove "Bearer ")

                // You can extract the userId from claims if needed:
                String userId = claims.get("userId", String.class);

                // Create the authorities (roles) from the claims (assuming issuer is the role)
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(claims.getIssuer());
                
                // Set up the authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, Collections.singleton(authority));

                // Store the claims as the details (instead of WebAuthenticationDetails)
                authentication.setDetails(claims);  // Store the claims as the authentication details

                // Store authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Optionally, you can log or use the `userId` as required
                // System.out.println("User ID from token: " + userId);
            }

        } catch (Exception e) {
            // Log or handle the exception in a proper way
            System.out.println("JWT authentication failed: " + e.getMessage());
        }

        // Proceed with the filter chain (let the request continue)
        filterChain.doFilter(request, response);
    }
}
