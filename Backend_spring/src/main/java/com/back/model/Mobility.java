package com.back.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Mobility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idMobility;

    @Column(nullable = false, length = 20)
    private String type; // e.g., "CAR", "BUS"

    @Column(nullable = false, length = 15)
    private String plate;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Boolean available;

    @Column(nullable = false)
    private Integer entrepreneurId; // ✅ Este campo es obligatorio

}