package com.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReservasPorFechaDTO {
    private LocalDate fecha;
    private Long total;
}