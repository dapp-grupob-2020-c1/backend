package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Manager;
import com.unq.dapp0.c1.comprandoencasa.repositories.ManagerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository repository;

    public Manager save(Manager model) {
        return this.repository.save(model);
    }

    public Manager findById(Long id) {
        return this.repository.findById(id).get();
    }

    public List<Manager> findAll() {
        return this.repository.findAll();
    }
}
