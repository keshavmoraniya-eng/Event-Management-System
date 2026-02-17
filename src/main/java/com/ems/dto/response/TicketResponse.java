package com.ems.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketResponse {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private String ticketType;
    private String description;
    private BigDecimal price;
    private Integer availableQuantity;
    private Integer soldQuantity;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
