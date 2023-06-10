package com.ua.hotel.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomDto {
    private Long id;

    private int number;
    private double price;
    private int maxGuests;
    private boolean isAvailable;
}
