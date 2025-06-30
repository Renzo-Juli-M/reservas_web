package com.back.service.impl;

import com.back.exception.ModelNotFoundException;
import com.back.model.Room;
import com.back.repo.*;
import com.back.service.IRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends CRUDImpl<Room, Integer>
        implements IRoomService {

    private final IRoomRepo repo;
    private final IReservationRepo reservationRepo;

    @Override
    protected IGenericRepo<Room, Integer> getRepo() {
        return repo;
    }

    /** Actualiza solo disponibilidad */
    @Override
    @Transactional
    public Room updateAvailability(Integer id, boolean available) throws Exception {
        Room room = repo.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Room not found: " + id));
        repo.updateRoomAvailability(id, available);
        return repo.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Error re-loading room"));
    }

    /** Métrica */
    @Override
    public Long countByEntrepreneur(Integer entrepreneurId) {
        return repo.countByEntrepreneurId(entrepreneurId);
    }
}