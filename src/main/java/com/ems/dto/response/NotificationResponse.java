package com.ems.dto.response;

import com.ems.model.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String username;
    private String subject;
    private String message;
    private NotificationType type;
    private Boolean isRead;
    private Boolean isSent;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
