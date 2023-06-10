package com.ua.hotel.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuestDto {
    private Long id;

    private String firstName;
    private String lastName;
    private String age;
    private String email;
    private String phone;
}
