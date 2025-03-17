package com.bitsclassmgmt.chatservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bitsclassmgmt.chatservice.dto.GroupsDto;

@FeignClient(name = "classes-service", path = "/v1/job-service/groups")
public interface GroupsServiceClient {
    @GetMapping("/{id}")
    ResponseEntity<GroupsDto> getGroupsById(@PathVariable String id);
}
