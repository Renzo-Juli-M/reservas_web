package com.back.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
	private Integer idReservation;
	@NotNull
	private String customerName;
	@NotNull
	private LocalDate checkInDate;
	@NotNull
	private LocalDate checkOutDate;
	@NotNull
	private Integer roomId;
}
