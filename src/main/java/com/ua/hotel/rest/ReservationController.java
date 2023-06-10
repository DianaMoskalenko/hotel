package com.ua.hotel.rest;

import com.ua.hotel.domain.Guest;
import com.ua.hotel.domain.Reservation;
import com.ua.hotel.domain.Room;
import com.ua.hotel.dto.ReservationDto;
import com.ua.hotel.repository.GuestRepository;
import com.ua.hotel.repository.RoomRepository;
import com.ua.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReservationController {
    public final ReservationService reservationService;
    public final GuestRepository guestRepository;
    public final RoomRepository roomRepository;
    @PostMapping("/reservations")
    public void save(@RequestBody Reservation reservation) {
        reservationService.save(reservation);
    }
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }
    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationDto> findById(@PathVariable("id") Long id) {
        return reservationService.findById(id)
                .map(reservation -> ResponseEntity.ok(ReservationService.buildReservationDto(reservation)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/reservations/findByCheckinDate/{checkinDate}")
    public ResponseEntity<List<ReservationDto>> findByCheckinDate(@PathVariable LocalDate checkinDate) {
        List<ReservationDto> reservations = reservationService.findByCheckinDate(checkinDate).stream()
                .map(ReservationService::buildReservationDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }
    @PostMapping("/reservations/guestIds/{guestIds}")
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation, @PathVariable List<Long> guestIds) {
        List<Guest> guests = guestRepository.findAllById(guestIds);
        if (guests.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid guest IDs");
        }
        Room room = reservation.getRoom();
        reservation.setGuests(guests);

        Optional<Room> optionalRoom = roomRepository.findById(room.getId());
        if (optionalRoom.isPresent()) {
            Room foundRoom = optionalRoom.get();
            reservation.setRoom(foundRoom);

            reservationService.createReservation(reservation, guestIds, foundRoom.getId());
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/reservations/{reservationId}/guests/{guestIds}")
    public ResponseEntity<String> addGuestsToReservation(@PathVariable("reservationId") Long reservationId, @PathVariable List<Long> guestIds) {
        List<Guest> guests = guestRepository.findAllById(guestIds);
        if (guests.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid guest IDs");
        }

        Optional<Reservation> reservation = reservationService.findById(reservationId);
        if(reservation.isPresent()) {
            reservationService.addGuestsToReservation(reservationId, guestIds);
            return ResponseEntity.ok("Guests successfully added to the reservation");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/reservations/{reservationId}/deleteGuests/{guestIds}")
    public ResponseEntity<String> removeGuestsFromReservation(@PathVariable("reservationId") Long reservationId, @PathVariable List<Long> guestIds) {
        List<Guest> guests = guestRepository.findAllById(guestIds);
        if (guests.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid guest IDs");
        }

        Optional<Reservation> reservation = reservationService.findById(reservationId);
        if(reservation.isPresent()) {
            reservationService.removeGuestsFromReservation(reservationId, guestIds);
            return ResponseEntity.ok("Guests successfully removed from the reservation");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/reservations/moveGuest")
    public ResponseEntity<String> moveGuestToRoom(@RequestParam("guestId") Long guestId, @RequestParam("currentRoomId") Long currentRoomId, @RequestParam("newRoomId") Long newRoomId) {
        try {
            reservationService.moveGuestToRoom(guestId, currentRoomId, newRoomId);
            return ResponseEntity.ok("Guest successfully moved to the new room");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @DeleteMapping("/reservations/{reservationId}")
    public void deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }
}
