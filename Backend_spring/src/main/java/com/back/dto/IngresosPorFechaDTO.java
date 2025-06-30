package com.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
public class IngresosPorFechaDTO {
    private Date fecha;
    private BigDecimal ingresoTotal;
}