package com.back.service.impl;

import com.back.model.Entrepreneur;
import com.back.repo.IEntrepreneurRepo;
import com.back.service.IEntrepreneurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntrepreneurServiceImpl extends CRUDImpl<Entrepreneur, Integer> implements IEntrepreneurService {

    private final IEntrepreneurRepo repo;

    @Override
    protected IEntrepreneurRepo getRepo() {
        return repo;
    }
}