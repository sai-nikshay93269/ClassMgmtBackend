package com.bitsclassmgmt.authservice.dto;

import com.bitsclassmgmt.authservice.enums.Role;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String username;
    private String password;
    private Role role;
}
