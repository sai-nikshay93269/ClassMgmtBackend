package com.bitsclassmgmt.classesservice.request.classes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class ClassMembersCreateRequest {

    @NotBlank(message = "Class id is required")
    private String classId;

    @NotEmpty(message = "At least one student id is required")
    private List<@NotBlank(message = "Student id cannot be blank") String> studentIds;
}
