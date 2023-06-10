package com.ua.hotel.rest;

import com.ua.hotel.domain.Guest;
import com.ua.hotel.domain.Room;
import com.ua.hotel.dto.RoomDto;
import com.ua.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RoomController {
    public final RoomService roomService;
   @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomDto> findById(@PathVariable Long id) {
        return roomService.findById(id)
                .map(room -> ResponseEntity.ok(RoomService.buildRoomDto(room)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rooms/number/{roomNumber}")
    public ResponseEntity<RoomDto> findByRoomNumber(@PathVariable int roomNumber) {
        return roomService.findByRoomNumber(roomNumber)
                .map(room -> ResponseEntity.ok(RoomService.buildRoomDto(room)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rooms/available")
    public ResponseEntity<List<RoomDto>> getAvailableRooms() {
        List<RoomDto> availableRoomDtos = roomService.getAvailableRooms().stream()
                .map(RoomService::buildRoomDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(availableRoomDtos);
    }

    @GetMapping("/rooms/unavailable")
    public ResponseEntity<List<RoomDto>> getUnavailableRooms() {
        List<RoomDto> unavailableRoomDtos = roomService.getUnavailableRooms().stream()
                .map(RoomService::buildRoomDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(unavailableRoomDtos);
    }

    @GetMapping("/rooms/findByType/{roomType}")
    public ResponseEntity<List<RoomDto>> getRoomsByType(@PathVariable String roomType) {
        List<RoomDto> roomDtos = roomService.findByRoomType(roomType).stream()
                .map(RoomService::buildRoomDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roomDtos);
    }

    @GetMapping("/rooms/findByMaxNumber/{maxNumberOfGuests}")
    public ResponseEntity<List<RoomDto>> getRoomsByMaxNumberOfGuests(@PathVariable int maxGuests) {
        List<RoomDto> roomDtos = roomService.findByMaxGuests(maxGuests).stream()
                .map(RoomService::buildRoomDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roomDtos);
    }

    @PostMapping("/rooms")
    public ResponseEntity<Void> save(@RequestBody Room room) {
        roomService.saveRoom(room);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("rooms/{roomId}/guests")
    public ResponseEntity<String> addNewGuestToRoom(@PathVariable Long roomId, @RequestBody Guest guest) {
        Optional<Room> room = roomService.findById(roomId);
        if (room.isPresent()) {
            roomService.addGuestToRoom(room, guest);
            return ResponseEntity.ok("Guest added to room successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("rooms/{roomId}/guests/addExisting")
    public ResponseEntity<String> addExistingGuestsToRoom(@PathVariable Long roomId, @RequestParam List<Long> guestIds) {
        roomService.addExistingGuestsToRoom(roomId, guestIds);
        return ResponseEntity.ok("Existing guests added to room successfully");
    }

    @PutMapping("/rooms/{id}/roomNumber")
    public ResponseEntity<String> updateRoomNumber(@PathVariable Long id, @RequestBody int roomNumber) {
        roomService.updateRoomNumber(id, roomNumber);

        return ResponseEntity.ok("Room's number updated successfully");
    }

    @PutMapping("/rooms/{id}/roomType")
    public ResponseEntity<String> updateRoomType(@PathVariable Long id, @RequestBody String roomType) {
        roomService.updateRoomType(id, roomType);

        return ResponseEntity.ok("Room type updated successfully");
    }

    @PutMapping("/rooms/{id}/pricePerNight")
    public ResponseEntity<String> updatePricePerNight(@PathVariable Long id, @RequestBody double pricePerNight) {
        roomService.updatePricePerNight(id, pricePerNight);

        return ResponseEntity.ok("Price per night updated successfully");
    }

    @PutMapping("/rooms/{id}/maxNumberOfGuests")
    public ResponseEntity<String> updateMaxNumberOfGuests(@PathVariable Long id, @RequestBody int maxNumberOfGuests) {
        roomService.updateMaxNumberOfGuests(id, maxNumberOfGuests);

        return ResponseEntity.ok("Max number of guests updated successfully");
    }

    @PutMapping("/rooms/{id}/isAvailable")
    public ResponseEntity<String> updateIsAvailable(@PathVariable Long id, @RequestBody boolean isAvailable) {
        roomService.updateIsAvailable(id, isAvailable);

        return ResponseEntity.ok("Room status updated successfully");
    }

    @DeleteMapping("/rooms/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

}
