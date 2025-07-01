package com.back.config;

import com.back.dto.FoodDTO;
import com.back.model.Food;
import com.back.dto.ReservationDTO;
import com.back.dto.RoomDTO;
import com.back.model.Reservation;
import com.back.model.Room;
import com.back.dto.MobilityDTO;
import com.back.model.Mobility;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean(name = "defaultMapper")
    public ModelMapper defaultMapper() {
        return new ModelMapper();
    }

    @Bean(name = "roomMapper")
    public ModelMapper roomMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(RoomDTO.class, Room.class)
                .addMapping(RoomDTO::getIdRoom, Room::setIdRoom)
                .addMapping(RoomDTO::getNumber, Room::setNumber)
                .addMapping(RoomDTO::getType, Room::setType)
                .addMapping(RoomDTO::getPrice, Room::setPrice)
                .addMapping(RoomDTO::getAvailable, Room::setAvailable);

        modelMapper.createTypeMap(Room.class, RoomDTO.class)
                .addMapping(Room::getIdRoom, RoomDTO::setIdRoom)
                .addMapping(Room::getNumber, RoomDTO::setNumber)
                .addMapping(Room::getType, RoomDTO::setType)
                .addMapping(Room::getPrice, RoomDTO::setPrice)
                .addMapping(Room::getAvailable, RoomDTO::setAvailable);

        return modelMapper;
    }

    @Bean(name = "reservationMapper")
    public ModelMapper reservationMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(ReservationDTO.class, Reservation.class)
                .addMapping(ReservationDTO::getIdReservation, Reservation::setIdReservation)
                .addMapping(ReservationDTO::getCustomerName, Reservation::setCustomerName)
                .addMapping(ReservationDTO::getCheckInDate, Reservation::setCheckInDate)
                .addMapping(ReservationDTO::getCheckOutDate, Reservation::setCheckOutDate)
                .addMapping(ReservationDTO::getRoomId, Reservation::setRoomId);

        modelMapper.createTypeMap(Reservation.class, ReservationDTO.class)
                .addMapping(Reservation::getIdReservation, ReservationDTO::setIdReservation)
                .addMapping(Reservation::getCustomerName, ReservationDTO::setCustomerName)
                .addMapping(Reservation::getCheckInDate, ReservationDTO::setCheckInDate)
                .addMapping(Reservation::getCheckOutDate, ReservationDTO::setCheckOutDate)
                .addMapping(Reservation::getRoomId, ReservationDTO::setRoomId);

        return modelMapper;
    }

    @Bean(name = "foodMapper")
    public ModelMapper foodMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Food.class, FoodDTO.class)
                .addMapping(Food::getIdFood, FoodDTO::setIdFood)
                .addMapping(src -> src.getEntrepreneur().getIdEntrepreneur(), FoodDTO::setEntrepreneurId);

        modelMapper.createTypeMap(FoodDTO.class, Food.class)
                .addMapping(FoodDTO::getIdFood, Food::setIdFood)
                .addMapping(FoodDTO::getName, Food::setName)
                .addMapping(FoodDTO::getPrice, Food::setPrice)
                .addMapping(FoodDTO::getAvailable, Food::setAvailable);

        return modelMapper;
    }


    @Bean(name = "mobilityMapper")
    public ModelMapper mobilityMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(MobilityDTO.class, Mobility.class)
                .addMapping(MobilityDTO::getIdMobility, Mobility::setIdMobility)
                .addMapping(MobilityDTO::getType, Mobility::setType)
                .addMapping(MobilityDTO::getPlate, Mobility::setPlate)
                .addMapping(MobilityDTO::getCapacity, Mobility::setCapacity)
                .addMapping(MobilityDTO::getAvailable, Mobility::setAvailable);

        modelMapper.createTypeMap(Mobility.class, MobilityDTO.class)
                .addMapping(Mobility::getIdMobility, MobilityDTO::setIdMobility)
                .addMapping(Mobility::getType, MobilityDTO::setType)
                .addMapping(Mobility::getPlate, MobilityDTO::setPlate)
                .addMapping(Mobility::getCapacity, MobilityDTO::setCapacity)
                .addMapping(Mobility::getAvailable, MobilityDTO::setAvailable);

        return modelMapper;
    }
}