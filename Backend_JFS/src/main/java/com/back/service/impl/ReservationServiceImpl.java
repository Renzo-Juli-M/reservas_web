package com.back.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.back.exception.ModelNotFoundException;
import com.back.model.Reservation;
import com.back.model.Room;
import com.back.repo.IGenericRepo;
import com.back.repo.IReservationRepo;
import com.back.repo.IRoomRepo;
import com.back.service.IReservationService;
import com.back.service.IRoomService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl extends CRUDImpl<Reservation, Integer> implements IReservationService {

    private final IReservationRepo repo;
    private final IRoomRepo roomRepo;
    private final IRoomService roomService;

    @Override
    protected IGenericRepo<Reservation, Integer> getRepo() {
        return repo;
    }
    
    @Override
    @Transactional
    public Reservation save(Reservation reservation) throws Exception {
        validateReservationDates(reservation);

        Room room = roomRepo.findById(reservation.getRoomId())
                .orElseThrow(() -> new ModelNotFoundException("Room not found"));

        List<Reservation> conflictingReservations = repo.findByRoomIdAndDateRange(
            reservation.getRoomId(), 
            reservation.getCheckInDate(), 
            reservation.getCheckOutDate()
        );

        if (!conflictingReservations.isEmpty()) {
            throw new IllegalArgumentException("Room is already reserved for the selected dates");
        }

        Reservation savedReservation = getRepo().save(reservation);

        // Solo marcar como no disponible si la reserva es desde hoy en adelante
        if (!reservation.getCheckInDate().isAfter(LocalDate.now())) {
            roomService.updateAvailability(room.getIdRoom(), false);
        }

        log.info("New reservation created - Room: {}, Customer: {}, Check-in: {}, Check-out: {}", 
            room.getNumber(), 
            reservation.getCustomerName(), 
            reservation.getCheckInDate(), 
            reservation.getCheckOutDate()
        );

        return getRepo().save(reservation);
    }

    @Override
    @Transactional
    public Reservation update(Reservation reservation, Integer id) throws Exception {
        Reservation existingReservation = repo.findById(id)
            .orElseThrow(() -> new ModelNotFoundException("Reservation not found"));

        validateReservationDates(reservation);

        List<Reservation> conflictingReservations = repo.findByRoomIdAndDateRange(
            reservation.getRoomId(), 
            reservation.getCheckInDate(), 
            reservation.getCheckOutDate()
        );

        // Filter out the current reservation from conflicts
        conflictingReservations.removeIf(r -> r.getIdReservation().equals(id));

        if (!conflictingReservations.isEmpty()) {
            throw new IllegalArgumentException("Room is already reserved for the selected dates");
        }

        // 4. Update reservation
        reservation.setIdReservation(id);
        Reservation updatedReservation = repo.save(reservation);

        // 5. Log update details
        log.info("Reservation updated - ID: {}, Room: {}, Customer: {}, Check-in: {}, Check-out: {}", 
            id, 
            reservation.getRoomId(), 
            reservation.getCustomerName(), 
            reservation.getCheckInDate(), 
            reservation.getCheckOutDate()
        );

        return updatedReservation;
    }

    @Override
    @Transactional
    public void delete(Integer id) throws Exception {
        // 1. Find the reservation
        Reservation reservation = repo.findById(id)
            .orElseThrow(() -> new ModelNotFoundException("Reservation not found"));

        // 2. Delete the reservation
        repo.deleteById(id);

        // 3. Check if the room has any other active reservations
        List<Reservation> roomReservations = repo.findByRoomIdAndDateRange(
            reservation.getRoomId(), 
            LocalDate.now(), 
            LocalDate.now().plusYears(1)
        );

        // 4. Update room availability if no active reservations
        if (roomReservations.isEmpty()) {
            roomService.updateAvailability(reservation.getRoomId(), true);
        }

        // 5. Log deletion
        log.info("Reservation deleted - ID: {}, Room: {}, Customer: {}", 
            id, 
            reservation.getRoomId(), 
            reservation.getCustomerName()
        );
    }

    /**
     * Validate reservation dates
     * @param reservation Reservation to validate
     * @throws IllegalArgumentException if dates are invalid
     */
    private void validateReservationDates(Reservation reservation) {
        // Validate check-out date is after check-in date
        if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        // Optional: Prevent reservations in the past
        if (reservation.getCheckInDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void manageReservationsAndAvailability() {
        LocalDate today = LocalDate.now();

        List<Reservation> pastReservations = repo.findByCheckOutDateBefore(today);
        repo.deleteAll(pastReservations);

        log.info("Deleted {} past reservations", pastReservations.size());

        List<Room> rooms = roomRepo.findAll();
        for (Room room : rooms) {
            List<Reservation> activeReservations = repo.findByRoomIdAndDateRange(
                room.getIdRoom(), 
                today, 
                today.plusDays(1)
            );

            boolean hasActiveReservation = activeReservations.stream()
                .anyMatch(reservation -> 
                    !reservation.getCheckOutDate().isBefore(today) && 
                    !reservation.getCheckInDate().isAfter(today)
                );

            if (hasActiveReservation && room.getAvailable()) {
                try {
                    roomService.updateAvailability(room.getIdRoom(), false);
                    log.info("Room {} marked as unavailable due to active reservation", room.getNumber());
                } catch (Exception e) {
                    log.error("Error updating room availability for room {}", room.getIdRoom(), e);
                }
            } else if (!hasActiveReservation && !room.getAvailable()) {
                try {
                    roomService.updateAvailability(room.getIdRoom(), true);
                    log.info("Room {} marked as available", room.getNumber());
                } catch (Exception e) {
                    log.error("Error updating room availability for room {}", room.getIdRoom(), e);
                }
            }
        }
    }
}