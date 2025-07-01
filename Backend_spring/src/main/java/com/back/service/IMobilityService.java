package com.back.service;

import com.back.model.Mobility;

public interface IMobilityService extends ICRUD<Mobility, Integer> {
    Mobility updateAvailability(Integer id, boolean available) throws Exception;
}