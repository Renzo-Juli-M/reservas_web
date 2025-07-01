package com.back.repo;

import com.back.model.Mobility;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface IMobilityRepo extends IGenericRepo<Mobility, Integer> {

    @Modifying
    @Query("UPDATE Mobility m SET m.available = :available WHERE m.idMobility = :id")
    void updateAvailability(@Param("id") Integer id, @Param("available") Boolean available);
}