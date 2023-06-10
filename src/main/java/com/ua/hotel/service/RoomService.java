package com.ua.hotel.service;

import com.ua.hotel.domain.Guest;
import com.ua.hotel.domain.Reservation;
import com.ua.hotel.domain.Room;
import com.ua.hotel.dto.RoomDto;
import com.ua.hotel.repository.GuestRepository;
import com.ua.hotel.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    public void save(Room room) {
        roomRepository.save(room);
    }
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    public List<RoomDto> findAll() {
        return roomRepository.findAll().stream()
                .map(RoomService::buildRoomDto)
                .collect(Collectors.toList());
    }

    public static RoomDto buildRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .number(room.getRoomNumber())
                .price(room.getPrice())
                .maxGuests(room.getMaxGuests())
                .isAvailable(room.isAvailable())
                .build();
    }
    public Optional<Room> findByRoomNumber(int roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByIsAvailable(true);
    }

    public List<Room> getUnavailableRooms() {
        return roomRepository.findByIsAvailable(false);
    }

    public List<Room> findByRoomType(String roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    public List<Room> findByMaxGuests(int maxGuests) {
        return roomRepository.findByMaxGuests(maxGuests);
    }
    public boolean isRoomAvailable(Long roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            List<Reservation> reservations = room.getReservations();
            for (Reservation reservation : reservations) {
                LocalDate existingCheckinDate = reservation.getCheckinDate();
                LocalDate existingCheckoutDate = reservation.getCheckoutDate();
                if (checkinDate.isBefore(existingCheckoutDate) && checkoutDate.isAfter(existingCheckinDate)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    public void updateRoomNumber(Long roomId, int roomNumber) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        room.setRoomNumber(roomNumber);

        roomRepository.save(room);
    }

    public void updateRoomType(Long roomId, String roomType) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        room.setRoomType(roomType);

        roomRepository.save(room);
    }

    public void updatePricePerNight(Long roomId, double pricePerNight) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        room.setPrice(pricePerNight);

        roomRepository.save(room);
    }

    public void updateMaxNumberOfGuests(Long roomId, int maxGuests) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        room.setMaxGuests(maxGuests);

        roomRepository.save(room);
    }

    public void updateIsAvailable(Long roomId, boolean isAvailable) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        room.setIsAvailable(isAvailable);
        roomRepository.save(room);
    }

    public void addGuestToRoom(Optional<Room> room, Guest guest) {
        room.ifPresent(r -> {
            guest.setRoom(r);
            guestRepository.save(guest);
            roomRepository.save(r);
            r.getGuests().add(guest);
        });
    }

    public void addExistingGuestsToRoom(Long roomId, List<Long> guestIds) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isPresent()) {
            List<Guest> guests = guestRepository.findAllById(guestIds);
            guests.forEach(guest -> {
                guest.setRoom(room.get());
                guestRepository.save(guest);
                room.get().getGuests().add(guest);
            });
            roomRepository.save(room.get());
        } else {
            throw new EntityNotFoundException("Room not found");
        }
    }

    public void deleteRoom(Long id) {
        Room roomToDelete = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        roomRepository.delete(roomToDelete);
    }

}
