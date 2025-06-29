package com.back.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.back.dto.ReservationDTO;
import com.back.dto.RoomDTO;
import com.back.model.Reservation;
import com.back.model.Room;


@Configuration
public class MapperConfig {

    @Bean(name = "defaultMapper")
    public ModelMapper defaultMapper() {
        return new ModelMapper();
    }

    @Bean(name = "roomMapper")
    public ModelMapper roomMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Escritura: RoomDTO -> Room
        modelMapper.createTypeMap(RoomDTO.class, Room.class)
                .addMapping(RoomDTO::getIdRoom, Room::setIdRoom)
                .addMapping(RoomDTO::getNumber, Room::setNumber)
                .addMapping(RoomDTO::getType, Room::setType) // Mapea el enum directamente
                .addMapping(RoomDTO::getPrice, Room::setPrice)
                .addMapping(RoomDTO::getAvailable, Room::setAvailable);

        // Lectura: Room -> RoomDTO
        modelMapper.createTypeMap(Room.class, RoomDTO.class)
                .addMapping(Room::getIdRoom, RoomDTO::setIdRoom)
                .addMapping(Room::getNumber, RoomDTO::setNumber)
                .addMapping(Room::getType, RoomDTO::setType) // Enum directo
                .addMapping(Room::getPrice, RoomDTO::setPrice)
                .addMapping(Room::getAvailable, RoomDTO::setAvailable);

        return modelMapper;
    }

    
    @Bean(name = "reservationMapper")
    public ModelMapper reservationMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Escritura: ReservationDTO -> Reservation
        modelMapper.createTypeMap(ReservationDTO.class, Reservation.class)
                .addMapping(ReservationDTO::getIdReservation, Reservation::setIdReservation)
                .addMapping(ReservationDTO::getCustomerName, Reservation::setCustomerName)
                .addMapping(ReservationDTO::getCheckInDate, Reservation::setCheckInDate)
                .addMapping(ReservationDTO::getCheckOutDate, Reservation::setCheckOutDate)
                .addMapping(ReservationDTO::getRoomId, Reservation::setRoomId); // Ahora se asigna directamente

        // Lectura: Reservation -> ReservationDTO
        modelMapper.createTypeMap(Reservation.class, ReservationDTO.class)
                .addMapping(Reservation::getIdReservation, ReservationDTO::setIdReservation)
                .addMapping(Reservation::getCustomerName, ReservationDTO::setCustomerName)
                .addMapping(Reservation::getCheckInDate, ReservationDTO::setCheckInDate)
                .addMapping(Reservation::getCheckOutDate, ReservationDTO::setCheckOutDate)
                .addMapping(Reservation::getRoomId, ReservationDTO::setRoomId); // Directo

        return modelMapper;
    }


}
