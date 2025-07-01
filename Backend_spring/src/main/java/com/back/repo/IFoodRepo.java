package com.back.repo;

import com.back.model.Food;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface IFoodRepo extends IGenericRepo<Food, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Food f SET f.available = :available WHERE f.idFood = :id")
    void updateFoodAvailability(@Param("id") Integer id, @Param("available") Boolean available);
}