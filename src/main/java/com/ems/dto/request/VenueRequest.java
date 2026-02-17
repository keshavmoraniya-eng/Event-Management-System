package com.ems.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VenueRequest {
    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 500)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 20)
    private String zipCode;

    @Size(max = 100)
    private String country;

    private String description;
    private Integer capacity;

    @Size(max = 15)
    private String contactNumber;

    @Size(max = 100)
    private String contactEmail;

    private String imageUrl;
    private Boolean isActive;
}
