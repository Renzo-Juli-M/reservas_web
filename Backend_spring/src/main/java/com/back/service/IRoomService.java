package com.back.service;

import com.back.model.Room;

public interface IRoomService extends ICRUD<Room, Integer> {
    Room updateAvailability(Integer id, boolean available) throws Exception;

    // Métrica
    Long countByEntrepreneur(Integer entrepreneurId);
}