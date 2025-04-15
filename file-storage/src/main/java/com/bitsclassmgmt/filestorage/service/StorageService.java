package com.bitsclassmgmt.filestorage.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bitsclassmgmt.filestorage.dto.FileResourceDto;
import com.bitsclassmgmt.filestorage.enums.FileType;
import com.bitsclassmgmt.filestorage.exc.GenericErrorResponse;
import com.bitsclassmgmt.filestorage.model.File;
import com.bitsclassmgmt.filestorage.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final FileRepository fileRepository;
    private String FOLDER_PATH;

    @PostConstruct
    public void init() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        FOLDER_PATH = currentWorkingDirectory + "/file-storage/src/main/resources/attachments";

        java.io.File targetFolder = new java.io.File(FOLDER_PATH);
        if (!targetFolder.exists() && !targetFolder.mkdirs()) {
            throw GenericErrorResponse.builder()
                    .message("Unable to create storage directories")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public File uploadFile(MultipartFile file) {
        // Validate the file type before proceeding
        if (!isValidFileType(file)) {
            throw new IllegalArgumentException("Invalid file type. Only PDF, DOC, DOCX, ZIP, or Image files are allowed.");
        }

        // Generate a unique file name (UUID)
        String uuid = UUID.randomUUID().toString();
        
        // Get the correct file extension based on content type
        String fileExtension = getFileExtension(file);
        String filePath = FOLDER_PATH + "/" + uuid + fileExtension;

        // Try saving the file to the disk
        try {
            file.transferTo(new java.io.File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Unable to save file to storage", e);
        }

        // Determine the file type for database storage (e.g., for later retrieval)
        FileType fileType = determineFileType(file.getContentType());

        // Save file metadata to the database
        File savedFile = fileRepository.save(File.builder()
                .id(uuid)
                .type(fileType)
                .filePath(filePath)
                .build());

        return savedFile;
    }

    // Method to validate the file type
    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();

        // Check for valid content types (PDF, DOC, DOCX, ZIP, Image)
        return contentType != null && (
                contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                contentType.equals("application/zip") ||
                contentType.startsWith("image/") // For image types like image/jpeg, image/png, etc.
        );
    }

    // Method to get the file extension based on the file's content type
    private String getFileExtension(MultipartFile file) {
        String contentType = file.getContentType();

        switch (contentType) {
            case "application/pdf":
                return ".pdf";
            case "application/msword":
                return ".doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return ".docx";
            case "application/zip":
                return ".zip";
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            default:
                return ""; // Return empty if file type is invalid (shouldn't reach here due to validation)
        }
    }



    public File getFileMetadata(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));
    }

    public FileResourceDto downloadFile(String id) {
        File fileEntity = findFileById(id);
        Path filePath = Paths.get(fileEntity.getFilePath());

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new IOException("File not found or unreadable");
            }

            return new FileResourceDto(filePath.getFileName().toString(), 
                    fileEntity.getType(), resource);
        } catch (IOException e) {
            throw GenericErrorResponse.builder()
                    .message("Unable to read file from storage")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public void deleteFile(String id) {
        File fileEntity = findFileById(id);
        java.io.File file = new java.io.File(fileEntity.getFilePath());

        if (file.delete()) {
            fileRepository.deleteById(id);
        } else {
            throw GenericErrorResponse.builder()
                    .message("Unable to delete file from storage")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    protected File findFileById(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> GenericErrorResponse.builder()
                        .message("File not found")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }
    
    private FileType determineFileType(String contentType) {
        if (contentType == null) {
            return null; // You can throw an exception here if null is not acceptable
        }

        // Check for image files
        if (contentType.startsWith("image/")) {
            return FileType.IMAGE;
        }

        // Check for document files (PDF, DOC, DOCX)
        else if (contentType.equals("application/pdf") ||
                 contentType.equals("application/msword") ||
                 contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            return FileType.DOC;
        }

        // Check for video files
        else if (contentType.startsWith("video/")) {
            return FileType.VIDEO;
        }

        // Check for ZIP files
        else if (contentType.equals("application/zip") ||
                 contentType.equals("application/x-zip-compressed")) {
            return FileType.ZIP;
        }

        // If file type is unsupported
        throw new IllegalArgumentException("Unsupported file type: " + contentType);
    }



}
