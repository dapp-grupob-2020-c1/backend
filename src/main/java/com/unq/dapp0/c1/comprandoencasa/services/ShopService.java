package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShopRepository;

import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository repository;

    public Shop save(Shop model) {
        return this.repository.save(model);
    }

    public Shop findShopById(Long id) {
        Optional<Shop> result = this.repository.findById(id);
        if (result.isPresent()){
            return result.get();
        } else {
            throw new ShopDoesntExistException(id);
        }
    }

}
