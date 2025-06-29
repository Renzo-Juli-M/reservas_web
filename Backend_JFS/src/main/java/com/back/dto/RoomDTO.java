package com.back.dto;

import com.back.model.RoomType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Integer idRoom;
    @NotNull
    private String number;
    @NotNull
    private RoomType type;
    @NotNull
    private double price;
    @NotNull
    private Boolean available;
}
