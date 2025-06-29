package com.back.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.back.model.Room;


public interface IRoomRepo extends IGenericRepo<Room, Integer> {
    @Modifying
    @Query("UPDATE Room r SET r.available = :available WHERE r.idRoom = :id")
    void updateRoomAvailability(@Param("id") Integer id, @Param("available") Boolean available);

    @Query("SELECT r FROM Room r WHERE r.available = true AND r.idRoom NOT IN " +
            "(SELECT DISTINCT res.roomId FROM Reservation res " +
            "WHERE (res.checkInDate <= :checkOut AND res.checkOutDate >= :checkIn))")
    List<Room> findAvailableRoomsInDateRange(
        @Param("checkIn") LocalDate checkIn, 
        @Param("checkOut") LocalDate checkOut
    );
}