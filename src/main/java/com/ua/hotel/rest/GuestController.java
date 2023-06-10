package com.ua.hotel.rest;

import com.ua.hotel.domain.Guest;
import com.ua.hotel.dto.GuestDto;
import com.ua.hotel.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GuestController {
    private final GuestService guestService;
    @GetMapping("/guests")
    public ResponseEntity<List<GuestDto>> findAll() {
        return ResponseEntity.ok(guestService.findAll());
    }
    @GetMapping("/guests/{id}")
    public ResponseEntity<Guest> findById(@PathVariable Long id) {
        return guestService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/guests/findGuestsByIds/{guestIds}")
    public ResponseEntity<List<GuestDto>> findGuestsByIds(@PathVariable List<Long> guestIds) {
        List<GuestDto> guests = guestService.findGuestsByIds(guestIds).stream()
                .map(GuestService::buildGuestDto)
                .collect(Collectors.toList());
        if (guests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(guests);
    }
    @GetMapping("/guests/email/{email}")
    public ResponseEntity<Guest> findByEmail(@PathVariable String email) {
        return guestService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/guests/lastName/{lastName}")
    public ResponseEntity<Guest> findByLastName(@PathVariable String lastName) {
        return guestService.findByLastName(lastName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/guests")
    public ResponseEntity<Void> save(@RequestBody Guest guest) {
        guestService.save(guest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/guests/{id}/rooms/{roomId}")
    public ResponseEntity<Void> update(@RequestBody Guest guest) {
        guestService.save(guest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @PutMapping("/guests/{id}/firstName")
    public ResponseEntity<String> updateFirstName(@PathVariable Long id, @RequestBody String firstName) {
        guestService.updateFirstName(id, firstName);

        return ResponseEntity.ok("Guest's first name updated successfully");
    }

    @PutMapping("/guests/{id}/lastName")
    public ResponseEntity<String> updateLastName(@PathVariable Long id, @RequestBody String lastName) {
        guestService.updateLastName(id, lastName);

        return ResponseEntity.ok("Guest's last name updated successfully");
    }
    public ResponseEntity<String> updateEmail(@PathVariable Long id, @RequestBody String email) {
        guestService.updateEmail(id, email);

        return ResponseEntity.ok("Guest's email updated successfully");
    }
    @PostMapping("/guests/{id}/rooms/{roomId}")
    public ResponseEntity<Void> addRoom(@PathVariable Long id, @PathVariable Long roomId) {
        guestService.addRoom(id, roomId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @DeleteMapping ("/guests/{id}")
    public void deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
    }
}
