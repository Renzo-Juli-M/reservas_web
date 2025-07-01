package com.back.service.impl;

import com.back.exception.ModelNotFoundException;
import com.back.model.Mobility;
import com.back.repo.IMobilityRepo;
import com.back.service.IMobilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobilityServiceImpl extends CRUDImpl<Mobility, Integer> implements IMobilityService {

    private final IMobilityRepo repo;

    @Override
    protected IMobilityRepo getRepo() {
        return repo;
    }

    @Override
    @Transactional
    public Mobility updateAvailability(Integer id, boolean available) throws Exception {
        Mobility m = repo.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("No se encontró movilidad: " + id));
        repo.updateAvailability(id, available);
        return repo.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Error recargando movilidad"));
    }
}