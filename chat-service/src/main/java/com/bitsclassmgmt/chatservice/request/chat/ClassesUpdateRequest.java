package com.bitsclassmgmt.chatservice.request.chat;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ClassesUpdateRequest {
    @NotBlank(message = "Classes")
    private String id;
    private String name;
    private String description;
}
