package com.ems.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VenueResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String description;
    private Integer capacity;
    private String contactNumber;
    private String contactEmail;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
