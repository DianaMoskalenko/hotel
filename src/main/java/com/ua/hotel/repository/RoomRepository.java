package com.ua.hotel.repository;

import com.ua.hotel.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(int roomNumber);
    List<Room> findByIsAvailable(boolean isAvailable);
    List<Room> findByRoomType(String roomType);
    List<Room> findByMaxGuests(int maxGuests);
}
