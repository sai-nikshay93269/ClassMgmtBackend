package com.bitsclassmgmt.userservice.dto;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.bitsclassmgmt.userservice.enums.Active;
import com.bitsclassmgmt.userservice.enums.Role;
import com.bitsclassmgmt.userservice.model.UserDetails;

import lombok.Data;

@Data
public class UserDetailsDto {
    private String id;
    private String username;
    private Role role;
    private String email;
    private UserDetails userDetails;
}