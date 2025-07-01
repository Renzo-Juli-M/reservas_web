package com.back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {
    private Integer idFood;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Boolean available;

    @NotNull
    private Integer entrepreneurId;
}