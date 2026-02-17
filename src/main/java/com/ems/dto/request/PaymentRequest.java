package com.ems.dto.request;

import com.ems.model.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull
    private Long bookingId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;

    private String paymentGateway;
    private String notes;

}
