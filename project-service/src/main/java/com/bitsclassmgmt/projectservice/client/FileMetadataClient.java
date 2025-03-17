package com.bitsclassmgmt.projectservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bitsclassmgmt.projectservice.dto.FileMetadataDto;

@FeignClient(name = "file-storage", path = "/v1/file-storage/metadata")
public interface FileMetadataClient {
    @GetMapping("/{id}")
    ResponseEntity<FileMetadataDto> getFileMetadataById(@PathVariable String id);
}
