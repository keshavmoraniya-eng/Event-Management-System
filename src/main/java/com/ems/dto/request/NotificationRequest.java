package com.ems.dto.request;

import com.ems.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotNull
    private Long userId;

    @NotBlank
    private String subject;

    @NotNull
    private NotificationType type;

    @NotBlank
    private String message;
}
