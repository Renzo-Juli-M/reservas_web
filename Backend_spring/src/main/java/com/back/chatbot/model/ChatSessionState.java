package com.back.chatbot.model;

import com.back.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionState {
    private String customerName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private boolean completed;

    private List<Room> habitacionesDisponibles = new ArrayList<>();
    private Room habitacionSeleccionada;
}