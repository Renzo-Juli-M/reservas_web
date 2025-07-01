package com.back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobilityDTO {
    private Integer idMobility;

    @NotNull
    private String type;

    @NotNull
    private String plate;

    @NotNull
    private Integer capacity;

    @NotNull
    private Boolean available;
}