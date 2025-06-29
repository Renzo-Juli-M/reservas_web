package com.back.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.back.model.Reservation;

import jakarta.transaction.Transactional;

@Repository
public interface IReservationRepo extends IGenericRepo<Reservation, Integer> {

	@Modifying
	@Transactional
	@Query("DELETE FROM Reservation r WHERE r.roomId = :roomId AND r.checkOutDate < :checkOutDate")
	void deleteByRoomIdAndCheckOutDateBefore(Integer roomId, LocalDate checkOutDate);

	@Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND "
			+ "((r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate))")
	List<Reservation> findByRoomIdAndDateRange(@Param("roomId") Integer roomId,
			@Param("checkInDate") LocalDate checkInDate, @Param("checkOutDate") LocalDate checkOutDate);

    List<Reservation> findByCheckOutDateBefore(LocalDate date);

}
