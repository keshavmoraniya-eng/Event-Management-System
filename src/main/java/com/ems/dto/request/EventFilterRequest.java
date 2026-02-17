package com.ems.dto.request;

import com.ems.model.EventStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventFilterRequest {
    private String category;
    private EventStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String city;
    private Integer minCapacity;
    private Boolean isPublished;
}
