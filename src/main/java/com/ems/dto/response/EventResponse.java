package com.ems.dto.response;

import com.ems.model.EventStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String category;
    private EventStatus status;
    private Integer maxAttendees;
    private Integer currentAttendees;
    private String imageUrl;
    private Boolean isPublished;
    private String venueName;
    private String venueAddress;
    private String organizerName;
    private LocalDateTime createdAt;
}
