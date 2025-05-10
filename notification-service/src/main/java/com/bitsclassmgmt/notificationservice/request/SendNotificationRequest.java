package com.bitsclassmgmt.notificationservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SendNotificationRequest {
    private String userId;
    private String classId;
    private String message;
}
