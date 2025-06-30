package com.back.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idReservation;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(name = "entrepreneur_id")
    private Integer entrepreneurId;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    /** Precio total calculado: price * noches */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;
}