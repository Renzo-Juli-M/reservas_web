package com.back.service.impl;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.back.exception.ModelNotFoundException;
import com.back.model.Room;
import com.back.repo.IGenericRepo;
import com.back.repo.IReservationRepo;
import com.back.repo.IRoomRepo;
import com.back.service.IRoomService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends CRUDImpl<Room, Integer> implements IRoomService {

    private final IRoomRepo repo;
    private final IReservationRepo reservationRepo;

    @Override
    protected IGenericRepo<Room, Integer> getRepo() {
        return repo;
    }

    @Transactional
    @Modifying
    public Room updateAvailability(Integer id, boolean available) throws Exception {
        Room room = repo.findById(id)
            .orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + id));

        if (!room.getAvailable() && available) {
            reservationRepo.deleteByRoomIdAndCheckOutDateBefore(id, LocalDate.now());
        }

        repo.updateRoomAvailability(id, available);

        return repo.findById(id)
            .orElseThrow(() -> new ModelNotFoundException("Error updating room"));
    }
}