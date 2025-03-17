package com.bitsclassmgmt.filestorage.dto;

import com.bitsclassmgmt.filestorage.enums.FileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDto {
    private String id;
    private FileType type;       // File content type (e.g., image/png, application/pdf)
    private String filePath;   // Path where the file is stored
}
