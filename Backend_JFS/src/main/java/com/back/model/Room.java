package com.back.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idRoom;

    @Column(nullable = false, length = 20, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoomType type;

//    @Column(nullable = false, precision = 10, scale = 2)
    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Boolean available;
    
//    @OneToMany
//    @JoinColumn(name = "roomId", referencedColumnName = "idRoom")
//    private List<Reservation> reservations;

}
