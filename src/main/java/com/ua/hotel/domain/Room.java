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
    private Integer roomNumber;
    @Column
    private Double price;
    @Column
    private Integer maxGuests;
    @Column
    private String roomType;
    @Column
    private Boolean isAvailable;

    @OneToMany(mappedBy = "room")
    private List<Guest> guests;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
