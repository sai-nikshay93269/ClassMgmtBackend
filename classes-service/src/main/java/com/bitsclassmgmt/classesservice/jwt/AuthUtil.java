package com.bitsclassmgmt.classesservice.jwt;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.Claims;

public class AuthUtil {

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Claims) {
            Claims claims = (Claims) authentication.getDetails();

            // ✅ Print all claims for debugging
            System.out.println("JWT Claims:");
            for (String key : claims.keySet()) {
                System.out.println(key + " : " + claims.get(key));
            }

            // ✅ Try to retrieve the userId
            return claims.get("userId", String.class);
        }

        System.out.println("Authentication is null or details not of type Claims");
        return null;
    }


    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().iterator().next().getAuthority(); // claims.getIssuer()
    }
}
