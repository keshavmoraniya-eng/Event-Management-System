package com.ems.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckInRequest {
    @NotBlank
    private String bookingReference;

    private String qrCode;
}
