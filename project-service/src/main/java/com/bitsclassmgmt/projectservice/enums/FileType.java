package com.bitsclassmgmt.projectservice.enums;

public enum FileType {
    IMAGE("image/png"),
    DOC("application/msword"),
    VIDEO("video/mp4"),
    ZIP("application/zip");

    private final String mimeType;

    FileType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
