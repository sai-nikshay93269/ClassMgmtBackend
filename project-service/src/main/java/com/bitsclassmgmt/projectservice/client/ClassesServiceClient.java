package com.bitsclassmgmt.projectservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bitsclassmgmt.projectservice.dto.ClassesDto;

@FeignClient(name = "classes-service", path = "/v1/job-service/classes")
public interface ClassesServiceClient {
    @GetMapping("/{id}")
    ResponseEntity<ClassesDto> getClassesById(@PathVariable String id);
}
