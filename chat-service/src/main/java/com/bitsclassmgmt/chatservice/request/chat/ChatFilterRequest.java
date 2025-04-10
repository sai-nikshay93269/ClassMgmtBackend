package com.bitsclassmgmt.chatservice.request.chat;

import lombok.Data;

@Data
public class ChatFilterRequest {
    private String groupId;
    private String classId;
    private int limit = 20;
    private int offset = 0;
}
