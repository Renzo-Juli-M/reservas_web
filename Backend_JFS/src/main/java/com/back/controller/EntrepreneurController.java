package com.back.controller;

import com.back.dto.DashboardStatsDTO;
import com.back.dto.ReservasPorFechaDTO;
import com.back.dto.ReservationDTO;
import com.back.dto.RoomDTO;
import com.back.model.Reservation;
import com.back.model.Room;
import com.back.service.IReservationService;
import com.back.service.IRoomService;
import com.back.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/entrepreneur")
@RequiredArgsConstructor
public class EntrepreneurController {

    private final IRoomService roomService;
    private final IReservationService reservationService;
    private final MapperUtil mapperUtil;

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<RoomDTO>> getRoomsByEntrepreneur(
            @PathVariable("id") Integer entrepreneurId) throws Exception {
        List<Room> list = roomService.findAll().stream()
                .filter(r -> entrepreneurId.equals(r.getEntrepreneurId()))
                .toList();
        return ResponseEntity.ok(
                mapperUtil.mapList(list, RoomDTO.class, "roomMapper")
        );
    }
    @GetMapping("/{id}/reservas-diaria")
    public ResponseEntity<List<ReservasPorFechaDTO>> getReservasDiarias(
            @PathVariable Integer id) {

        List<ReservasPorFechaDTO> datos = reservationService.getReservasAgrupadasPorFecha(id);
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDTO>> getReservationsByEntrepreneur(
            @PathVariable("id") Integer entrepreneurId) throws Exception {
        List<Reservation> list = reservationService.findAll().stream()
                .filter(r -> entrepreneurId.equals(r.getEntrepreneurId()))
                .toList();
        return ResponseEntity.ok(
                mapperUtil.mapList(list, ReservationDTO.class, "reservationMapper")
        );
    }

    // —— Nuevo endpoint dashboard ——
    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(
            @PathVariable("id") Integer entrepreneurId) {

        Long totalRes   = reservationService.countByEntrepreneur(entrepreneurId);
        Long occupied   = reservationService.countActiveByEntrepreneur(entrepreneurId);
        BigDecimal rev  = reservationService.sumRevenueByEntrepreneur(entrepreneurId);
        Long totalRooms = roomService.countByEntrepreneur(entrepreneurId);

        double rate = totalRooms == 0
                ? 0
                : (occupied.doubleValue() / totalRooms.doubleValue()) * 100;

        DashboardStatsDTO stats = new DashboardStatsDTO(
                totalRes, rate, rev, totalRooms, occupied
        );
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/{id}/rooms")
    public ResponseEntity<Void> createRoom(
            @PathVariable("id") Integer entrepreneurId,
            @RequestBody RoomDTO dto) throws Exception {
        Room r = mapperUtil.map(dto, Room.class, "roomMapper");
        r.setEntrepreneurId(entrepreneurId);
        roomService.save(r);
        return ResponseEntity.ok().build();
    }

    // … otros endpoints de update/delete rooms …
}