package com.back.service;

import com.back.model.Food;

public interface IFoodService extends ICRUD<Food, Integer> {
    Food updateAvailability(Integer id, boolean available) throws Exception;
}