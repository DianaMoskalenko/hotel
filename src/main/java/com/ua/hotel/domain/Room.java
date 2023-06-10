package com.ua.hotel.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int roomNumber;
    @Column
    private double price;
    @Column
    private int maxGuests;
    @Column
    private String roomType;
    @Column
    private boolean isAvailable;

    @OneToMany(mappedBy = "room")
    private List<Guest> guests;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
