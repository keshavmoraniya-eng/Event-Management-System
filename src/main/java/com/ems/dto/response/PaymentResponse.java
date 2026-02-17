package com.ems.dto.response;

import com.ems.model.PaymentMethod;
import com.ems.model.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private String bookingReference;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private String paymentGateway;
    private LocalDateTime paymentDate;
    private String notes;
    private LocalDateTime createdAt;
}
