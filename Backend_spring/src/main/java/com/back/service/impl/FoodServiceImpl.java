package com.back.service.impl;

import com.back.exception.ModelNotFoundException;
import com.back.model.Food;
import com.back.repo.IFoodRepo;
import com.back.service.IFoodService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl extends CRUDImpl<Food, Integer> implements IFoodService {

    private final IFoodRepo repo;

    @Override
    protected IFoodRepo getRepo() {
        return repo;
    }

    @Override
    @Transactional
    public Food updateAvailability(Integer id, boolean available) throws Exception {
        Food food = repo.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Food not found: " + id));
        repo.updateFoodAvailability(id, available);
        return repo.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Error re-loading food"));
    }
}