package com.ua.hotel.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
@Builder
public class ReservationDto {
    private Long id;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;
    private List<GuestDto> guests;
    private RoomDto room;
}
