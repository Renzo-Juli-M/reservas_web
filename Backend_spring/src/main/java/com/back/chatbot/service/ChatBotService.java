package com.back.chatbot.service;

import com.back.chatbot.model.ChatSessionState;
import com.back.dto.ReservationDTO;
import com.back.dto.RoomDTO;
import com.back.model.Reservation;
import com.back.model.Room;
import com.back.service.IReservationService;
import com.back.service.IRoomService;
import com.back.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final IRoomService roomService;
    private final IReservationService reservationService;
    private final MapperUtil mapperUtil;

    private final Map<String, ChatSessionState> sessions = new HashMap<>();

    public record BotResponse(String message, RoomDTO roomReserved) {}

    public BotResponse processMessage(String input) {
        String sessionId = "demo-session";
        ChatSessionState state = sessions.getOrDefault(sessionId, new ChatSessionState());
        sessions.put(sessionId, state);

        input = input.trim().toLowerCase();

        // 🗣️ Paso 0: Esperar intención del usuario
        if (!state.isEsperandoNombre() && state.getCustomerName() == null) {
            if (input.contains("habitacion") || input.contains("reserva") || input.contains("quiero") || input.contains("me gustaría")) {
                state.setEsperandoNombre(true);
                return new BotResponse("¡Genial! ¿Podrías decirme tu nombre para comenzar?", null);
            } else {
                return new BotResponse("Hola 👋 Soy tu asistente. Si deseas ver habitaciones disponibles, solo dilo 😊", null);
            }
        }

        // 🙋 Paso 1: Guardar nombre del cliente y mostrar lista
        if (state.isEsperandoNombre() && state.getCustomerName() == null) {
            state.setCustomerName(input);
            state.setEsperandoNombre(false);

            try {
                List<Room> disponibles = roomService.findAll().stream()
                        .filter(Room::getAvailable)
                        .toList();

                if (disponibles.isEmpty()) {
                    return new BotResponse("Lo siento, no hay habitaciones disponibles por ahora. ¿Te gustaría hacer una consulta más tarde?", null);
                }

                state.setHabitacionesDisponibles(disponibles);

                String lista = IntStream.range(0, disponibles.size())
                        .mapToObj(i -> (i + 1) + ". " + disponibles.get(i).getNumber() + " - " + disponibles.get(i).getType() + " - S/. " + disponibles.get(i).getPrice())
                        .collect(Collectors.joining("<br>"));

                return new BotResponse("Gracias, " + input + ". Estas son las habitaciones disponibles:<br>" +
                        lista + "<br>Escribe el número de la habitación que deseas reservar (ej. 1, 2, 3...).", null);

            } catch (Exception e) {
                return new BotResponse("Hubo un problema al obtener las habitaciones. Por favor intenta más tarde.", null);
            }
        }

        // 🛏️ Paso 2: Seleccionar habitación
        if (state.getHabitacionSeleccionada() == null) {
            try {
                int index = Integer.parseInt(input) - 1;
                List<Room> disponibles = state.getHabitacionesDisponibles();

                if (index < 0 || index >= disponibles.size()) {
                    return new BotResponse("Número inválido. Elige uno entre 1 y " + disponibles.size() + ".", null);
                }

                Room seleccionada = disponibles.get(index);
                state.setHabitacionSeleccionada(seleccionada);
                return new BotResponse("Perfecto. ¿Cuál es la fecha de ingreso? (YYYY-MM-DD o DD/MM/YYYY)", null);

            } catch (NumberFormatException e) {
                return new BotResponse("Por favor ingresa un número válido de la lista (ej. 1, 2...).", null);
            }
        }

        // 🗓️ Paso 3: Fecha de ingreso
        if (state.getCheckIn() == null) {
            try {
                state.setCheckIn(parseFechaFlexible(input));
                return new BotResponse("¿Y la fecha de salida?", null);
            } catch (Exception e) {
                return new BotResponse("Formato incorrecto. Usa YYYY-MM-DD o DD/MM/YYYY.", null);
            }
        }

        // 🎉 Paso 4: Fecha de salida + crear reserva
        if (state.getCheckOut() == null) {
            try {
                state.setCheckOut(parseFechaFlexible(input));
                Room room = state.getHabitacionSeleccionada();

                ReservationDTO reserva = new ReservationDTO(
                        null,
                        state.getCustomerName(),
                        state.getCheckIn(),
                        state.getCheckOut(),
                        room.getIdRoom()
                );

                try {
                    reservationService.save(mapperUtil.map(reserva, Reservation.class, "reservationMapper"));
                    room.setAvailable(false);
                    roomService.update(room, room.getIdRoom());
                } catch (Exception ex) {
                    return new BotResponse("Error al guardar la reserva. Intenta nuevamente.", null);
                }

                RoomDTO dto = mapperUtil.map(room, RoomDTO.class, "roomMapper");
                state.setCompleted(true);

                return new BotResponse("✅ ¡Reserva confirmada para la habitación Nº " + room.getNumber() + "!", dto);

            } catch (Exception e) {
                return new BotResponse("Formato inválido. Usa YYYY-MM-DD o DD/MM/YYYY para la salida.", null);
            }
        }

        return new BotResponse("Tu reserva ya fue realizada. ¿Deseas hacer otra?", null);
    }

    private LocalDate parseFechaFlexible(String fecha) {
        try {
            return LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            DateTimeFormatter alt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(fecha, alt);
        }
    }
}
