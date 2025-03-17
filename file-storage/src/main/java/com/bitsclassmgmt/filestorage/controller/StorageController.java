package com.bitsclassmgmt.filestorage.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.filestorage.dto.FileResourceDto;
import com.bitsclassmgmt.filestorage.dto.FileResponseDto;
import com.bitsclassmgmt.filestorage.model.File;
import com.bitsclassmgmt.filestorage.service.StorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/file-storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponseDto> uploadFile(@RequestPart("file") MultipartFile file) {
        File savedFile = storageService.uploadFile(file);
        
        // Convert to DTO
        FileResponseDto responseDto = new FileResponseDto(savedFile.getId(), savedFile.getType(), savedFile.getFilePath());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/metadata/{id}")
    public ResponseEntity<FileResponseDto> getFileMetadata(@PathVariable String id) {
        File file = storageService.getFileMetadata(id);

        // Convert to DTO
        FileResponseDto responseDto = new FileResponseDto(file.getId(), file.getType(), file.getFilePath());

        return ResponseEntity.ok(responseDto);
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
        FileResourceDto fileResource = storageService.downloadFile(id);

        return ResponseEntity.ok()
        		.contentType(MediaType.parseMediaType(fileResource.getContentType().getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFileName() + "\"")
                .body(fileResource.getResource());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        storageService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}