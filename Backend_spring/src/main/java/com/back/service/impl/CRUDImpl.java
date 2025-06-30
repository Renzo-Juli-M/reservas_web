package com.back.service.impl;

import java.util.List;

import com.back.exception.ModelNotFoundException;
import com.back.model.Reservation;
import com.back.repo.IGenericRepo;
import com.back.service.ICRUD;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {

    protected abstract IGenericRepo<T, ID> getRepo();

    @Override
    public T save(T t) throws Exception {
        if (t instanceof Reservation) {
            Reservation reservation = (Reservation) t;
            if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate())) {
                throw new IllegalArgumentException("La fecha de salida no puede ser anterior a la fecha de entrada.");
            }
        }

        return getRepo().save(t);
    }


    @Override
    public T update(T t, ID id) throws Exception {
        getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + id));
        
        if (t instanceof Reservation) {
            Reservation reservation = (Reservation) t;
            if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate())) {
                throw new IllegalArgumentException("La fecha de salida no puede ser anterior a la fecha de entrada.");
            }
        }

        return getRepo().save(t);
    }


    @Override
    public List<T> findAll() throws Exception {
        return getRepo().findAll();
    }

    @Override
    public T findById(ID id) throws Exception {
        return getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + id));
    }

    @Override
    public void delete(ID id) throws Exception {
        getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + id));
        getRepo().deleteById(id);
    }
}
