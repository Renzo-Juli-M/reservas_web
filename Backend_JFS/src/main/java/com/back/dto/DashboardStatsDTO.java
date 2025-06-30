package com.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalReservations;
    private Double occupancyRate;     // porcentaje 0.0–100.0
    private BigDecimal totalRevenue;  // suma de totalPrice
    private Long totalRooms;
    private Long occupiedRooms;       // actives hoy
}