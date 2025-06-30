package com.back.service.impl;

import com.back.dto.ReservasPorFechaDTO;
import com.back.exception.ModelNotFoundException;
import com.back.model.Reservation;
import com.back.model.Room;
import com.back.repo.*;
import com.back.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl
        extends CRUDImpl<Reservation, Integer>
        implements IReservationService {

    private final IReservationRepo repo;
    private final IRoomRepo roomRepo;
    private final IRoomService roomService;

    @Override
    protected IGenericRepo<Reservation, Integer> getRepo() {
        return repo;
    }
    @Override
    public List<ReservasPorFechaDTO> getReservasAgrupadasPorFecha(Integer entrepreneurId) {
        List<Object[]> raw = repo.countReservasPorFecha(entrepreneurId);

        return raw.stream()
                .map(obj -> new ReservasPorFechaDTO((LocalDate) obj[0], (Long) obj[1]))
                .toList();
    }

    @Override
    @Transactional
    public Reservation save(Reservation reservation) throws Exception {
        validateReservationDates(reservation);

        Room room = roomRepo.findById(reservation.getRoomId())
                .orElseThrow(() -> new ModelNotFoundException("Room not found"));

        List<Reservation> conflicts = repo.findByRoomIdAndDateRange(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Room already reserved for those dates");
        }

        // Calcular totalPrice = price * noches
        long nights = ChronoUnit.DAYS.between(
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        BigDecimal total = room.getPrice().multiply(BigDecimal.valueOf(nights));
        reservation.setTotalPrice(total);

        Reservation saved = repo.save(reservation);

        // Si la reserva comienza hoy o antes, marcar no disponible
        if (!reservation.getCheckInDate().isAfter(LocalDate.now())) {
            roomService.updateAvailability(room.getIdRoom(), false);
        }

        log.info("New reservation: room={}, customer={}, checkIn={}, checkOut={}",
                room.getNumber(),
                reservation.getCustomerName(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );

        return saved;
    }

    @Override
    @Transactional
    public Reservation update(Reservation reservation, Integer id) throws Exception {
        validateReservationDates(reservation);

        Reservation existing = repo.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Reservation not found"));

        List<Reservation> conflicts = repo.findByRoomIdAndDateRange(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        conflicts.removeIf(r -> r.getIdReservation().equals(id));
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Room already reserved for those dates");
        }

        // Recalcular precio si cambian fechas
        Room room = roomRepo.findById(reservation.getRoomId())
                .orElseThrow(() -> new ModelNotFoundException("Room not found"));
        long nights = ChronoUnit.DAYS.between(
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        reservation.setTotalPrice(room.getPrice().multiply(BigDecimal.valueOf(nights)));

        reservation.setIdReservation(id);
        Reservation updated = repo.save(reservation);

        log.info("Updated reservation {}: room={}, customer={}",
                id, reservation.getRoomId(), reservation.getCustomerName()
        );
        return updated;
    }

    @Override
    @Transactional
    public void delete(Integer id) throws Exception {
        Reservation res = repo.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Reservation not found"));
        repo.deleteById(id);

        // Si ya no hay reservas activas, liberar cuarto
        List<Reservation> active = repo.findByRoomIdAndDateRange(
                res.getRoomId(),
                LocalDate.now(),
                LocalDate.now().plusYears(1)
        );
        if (active.isEmpty()) {
            roomService.updateAvailability(res.getRoomId(), true);
        }

        log.info("Deleted reservation {} for room {}", id, res.getRoomId());
    }

    // Métricas para dashboard
    @Override
    public Long countByEntrepreneur(Integer entrepreneurId) {
        return repo.countByEntrepreneurId(entrepreneurId);
    }

    @Override
    public Long countActiveByEntrepreneur(Integer entrepreneurId) {
        return repo.countActiveByEntrepreneurId(entrepreneurId);
    }

    @Override
    public BigDecimal sumRevenueByEntrepreneur(Integer entrepreneurId) {
        return repo.sumRevenueByEntrepreneurId(entrepreneurId);
    }

    private void validateReservationDates(Reservation res) {
        if (res.getCheckOutDate().isBefore(res.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }
        if (res.getCheckInDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in cannot be past date");
        }
    }

    // Limpieza diaria y sincronización de availability
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void manageReservationsAndAvailability() {
        LocalDate today = LocalDate.now();

        // Borrar pasadas
        List<Reservation> past = repo.findByCheckOutDateBefore(today);
        repo.deleteAll(past);
        log.info("Deleted {} past reservations", past.size());

        // Revisar cada room
        roomRepo.findAll().forEach(room -> {
            boolean hasActive = repo.countActiveByEntrepreneurId(room.getEntrepreneurId()) > 0;
            try {
                roomService.updateAvailability(room.getIdRoom(), !hasActive);
            } catch (Exception e) {
                log.error("Error toggling availability for room {}", room.getIdRoom(), e);
            }
        });
    }
}