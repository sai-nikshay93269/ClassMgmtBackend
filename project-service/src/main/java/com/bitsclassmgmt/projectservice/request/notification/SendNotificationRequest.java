package com.bitsclassmgmt.projectservice.request.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendNotificationRequest {
    private String userId;
    private String classId;
    private String message;
}
