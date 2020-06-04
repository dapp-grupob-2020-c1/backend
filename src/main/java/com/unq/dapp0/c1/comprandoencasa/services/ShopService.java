package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShopRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    @Autowired
    private ShopRepository repository;

    public Shop save(Shop model) {
        return this.repository.save(model);
    }

    public Shop findById(Long id) {
        return this.repository.findById(id).get();
    }

    public List<Shop> findAll() {
        return this.repository.findAll();
    }
}
