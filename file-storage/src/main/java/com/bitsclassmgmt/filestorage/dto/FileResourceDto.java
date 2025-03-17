package com.bitsclassmgmt.filestorage.dto;

import com.bitsclassmgmt.filestorage.enums.FileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class FileResourceDto {
    private String fileName;   // Original file name
    private FileType contentType; // MIME type (e.g., image/png, application/pdf)
    private Resource resource; // Spring Resource for efficient streaming
}
