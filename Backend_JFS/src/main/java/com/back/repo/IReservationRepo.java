package com.back.repo;

import com.back.model.Reservation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReservationRepo extends IGenericRepo<Reservation, Integer> {

	@Modifying
	@Transactional
	@Query("DELETE FROM Reservation r " +
			"WHERE r.roomId = :roomId AND r.checkOutDate < :checkOutDate")
	void deleteByRoomIdAndCheckOutDateBefore(@Param("roomId") Integer roomId,
											 @Param("checkOutDate") LocalDate checkOutDate);

	@Query("SELECT r FROM Reservation r " +
			"WHERE r.roomId = :roomId " +
			"  AND r.checkInDate <= :checkOutDate " +
			"  AND r.checkOutDate >= :checkInDate")
	List<Reservation> findByRoomIdAndDateRange(@Param("roomId") Integer roomId,
											   @Param("checkInDate") LocalDate checkInDate,
											   @Param("checkOutDate") LocalDate checkOutDate);

	List<Reservation> findByCheckOutDateBefore(LocalDate date);

	// —— MÉTRICAS DASHBOARD ——

	@Query("SELECT COUNT(r) FROM Reservation r WHERE r.entrepreneurId = :eid")
	Long countByEntrepreneurId(@Param("eid") Integer eid);

	@Query(value = "SELECT r.check_in_date, COUNT(*) " +
			"FROM app_back.reservation r " +
			"WHERE r.entrepreneur_id = :eid " +
			"GROUP BY r.check_in_date " +
			"ORDER BY r.check_in_date ASC",
			nativeQuery = true)
	List<Object[]> countReservasPorFecha(@Param("eid") Integer eid);

	@Query(value = "SELECT r.check_in_date, SUM(r.total_price) " +
			"FROM app_back.reservation r " +
			"WHERE r.entrepreneur_id = :eid " +
			"GROUP BY r.check_in_date " +
			"ORDER BY r.check_in_date ASC",
			nativeQuery = true)
	List<Object[]> sumIngresosPorFecha(@Param("eid") Integer eid);

	@Query("SELECT COUNT(r) FROM Reservation r " +
			"WHERE r.entrepreneurId = :eid " +
			"  AND CURRENT_DATE BETWEEN r.checkInDate AND r.checkOutDate")
	Long countActiveByEntrepreneurId(@Param("eid") Integer eid);

	@Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r " +
			"WHERE r.entrepreneurId = :eid")
	BigDecimal sumRevenueByEntrepreneurId(@Param("eid") Integer eid);
}