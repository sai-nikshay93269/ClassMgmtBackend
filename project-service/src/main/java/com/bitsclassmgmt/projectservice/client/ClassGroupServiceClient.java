package com.bitsclassmgmt.projectservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bitsclassmgmt.projectservice.dto.ClassesDto;
import com.bitsclassmgmt.projectservice.dto.GroupsDto;


@FeignClient(name = "classes-service", path = "/v1/classes-service")
public interface ClassGroupServiceClient {
    @GetMapping("/classes/{id}")
    ResponseEntity<ClassesDto> getClassesById(@PathVariable String id);

    @GetMapping("/groups/{id}")
    ResponseEntity<GroupsDto> getGroupsById(@PathVariable String id);
}
