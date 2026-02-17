package com.ems.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketRequest {
    @NotNull
    private Long eventId;

    @NotBlank
    private String ticketType;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer availableQuantity;

    @NotBlank
    private String description;

    private Boolean isActive;
}
