package com.back.controller;

import com.back.dto.*;
import com.back.model.*;
import com.back.service.*;
import com.back.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/entrepreneur")
@RequiredArgsConstructor
public class EntrepreneurController {

    private final IEntrepreneurService entrepreneurService;
    private final IRoomService roomService;
    private final IReservationService reservationService;
    private final IFoodService foodService;
    private final IMobilityService mobilityService;
    private final MapperUtil mapperUtil;

    // ——— EMPRENDEDORES ———

    @PostMapping
    public ResponseEntity<Entrepreneur> createEntrepreneur(@RequestBody Entrepreneur e) throws Exception {
        Entrepreneur saved = entrepreneurService.save(e);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Entrepreneur>> listEntrepreneurs() throws Exception {
        return ResponseEntity.ok(entrepreneurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrepreneur> findEntrepreneur(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(entrepreneurService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrepreneur> updateEntrepreneur(@PathVariable Integer id, @RequestBody Entrepreneur e) throws Exception {
        Entrepreneur updated = entrepreneurService.update(e, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrepreneur(@PathVariable Integer id) throws Exception {
        entrepreneurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ——— HABITACIONES ———

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<RoomDTO>> getRoomsByEntrepreneur(@PathVariable Integer id) throws Exception {
        List<Room> list = roomService.findAll().stream()
                .filter(r -> id.equals(r.getEntrepreneurId()))
                .toList();
        return ResponseEntity.ok(mapperUtil.mapList(list, RoomDTO.class, "roomMapper"));
    }

    @PostMapping("/{id}/rooms")
    public ResponseEntity<Void> createRoom(@PathVariable Integer id, @RequestBody RoomDTO dto) throws Exception {
        Room r = mapperUtil.map(dto, Room.class, "roomMapper");
        r.setEntrepreneurId(id);
        roomService.save(r);
        return ResponseEntity.ok().build();
    }

    // ——— DASHBOARD ———

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(@PathVariable Integer id) {
        Long totalRes   = reservationService.countByEntrepreneur(id);
        Long occupied   = reservationService.countActiveByEntrepreneur(id);
        BigDecimal rev  = reservationService.sumRevenueByEntrepreneur(id);
        Long totalRooms = roomService.countByEntrepreneur(id);

        double rate = totalRooms == 0 ? 0 : (occupied.doubleValue() / totalRooms.doubleValue()) * 100;
        DashboardStatsDTO stats = new DashboardStatsDTO(totalRes, rate, rev, totalRooms, occupied);

        return ResponseEntity.ok(stats);
    }

    // ——— RESERVAS ———

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDTO>> getReservationsByEntrepreneur(@PathVariable Integer id) throws Exception {
        List<Reservation> list = reservationService.findAll().stream()
                .filter(r -> id.equals(r.getEntrepreneurId()))
                .toList();
        return ResponseEntity.ok(mapperUtil.mapList(list, ReservationDTO.class, "reservationMapper"));
    }

    @GetMapping("/{id}/ingresos-diaria")
    public ResponseEntity<List<IngresosPorFechaDTO>> getIngresosDiarios(@PathVariable Integer id) {
        return ResponseEntity.ok(reservationService.getIngresosAgrupados(id));
    }

    @GetMapping("/{id}/reservas-diaria")
    public ResponseEntity<List<ReservasPorFechaDTO>> getReservasDiarias(@PathVariable Integer id) {
        return ResponseEntity.ok(reservationService.getReservasAgrupadasPorFecha(id));
    }

    // ——— ALIMENTOS ———

    @GetMapping("/{id}/foods")
    public ResponseEntity<List<FoodDTO>> getFoodsByEntrepreneur(@PathVariable Integer id) throws Exception {
        List<Food> list = foodService.findAll().stream()
                .filter(f -> id.equals(f.getEntrepreneur().getIdEntrepreneur()))
                .toList();
        return ResponseEntity.ok(mapperUtil.mapList(list, FoodDTO.class, "foodMapper"));
    }

    @PostMapping("/{id}/foods")
    public ResponseEntity<Void> createFood(@PathVariable Integer id, @RequestBody FoodDTO dto) throws Exception {
        Food food = mapperUtil.map(dto, Food.class, "foodMapper");
        Entrepreneur e = new Entrepreneur();
        e.setIdEntrepreneur(id);
        food.setEntrepreneur(e);
        foodService.save(food);
        return ResponseEntity.ok().build();
    }

    // ——— MOVILIDAD ———

    @GetMapping("/{id}/mobility")
    public ResponseEntity<List<MobilityDTO>> getMobilityByEntrepreneur(@PathVariable Integer id) throws Exception {
        List<Mobility> list = mobilityService.findAll().stream()
                .filter(m -> id.equals(m.getEntrepreneurId()))
                .toList();
        return ResponseEntity.ok(mapperUtil.mapList(list, MobilityDTO.class, "mobilityMapper"));
    }

    @PostMapping("/{id}/mobility")
    public ResponseEntity<Void> createMobility(@PathVariable Integer id, @RequestBody MobilityDTO dto) throws Exception {
        Mobility mobility = mapperUtil.map(dto, Mobility.class, "mobilityMapper");
        mobility.setEntrepreneurId(id);
        mobilityService.save(mobility);
        return ResponseEntity.ok().build();
    }
}