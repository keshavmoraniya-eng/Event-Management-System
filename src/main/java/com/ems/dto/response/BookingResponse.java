package com.ems.dto.response;

import com.ems.model.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long id;
    private String bookingReference;
    private Integer quantity;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private String qrCode;
    private Boolean checkedIn;
    private String eventTitle;
    private LocalDateTime eventStartDateTime;
    private String ticketType;
    private LocalDateTime createdAt;
}
