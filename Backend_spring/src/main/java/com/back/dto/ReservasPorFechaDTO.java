package com.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReservasPorFechaDTO {
    private java.sql.Date fecha;
    private Long total;
}