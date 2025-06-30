package com.back.service;

import com.back.dto.ReservasPorFechaDTO;
import com.back.model.Reservation;
import java.math.BigDecimal;
import java.util.List;

public interface IReservationService extends ICRUD<Reservation, Integer> {

    // Métricas para dashboard
    Long countByEntrepreneur(Integer entrepreneurId);
    Long countActiveByEntrepreneur(Integer entrepreneurId);
    BigDecimal sumRevenueByEntrepreneur(Integer entrepreneurId);
    List<ReservasPorFechaDTO> getReservasAgrupadasPorFecha(Integer entrepreneurId);
}