package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.repositories.LocationRepository;

import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository repository;

    public Location save(Location model) {
        return this.repository.save(model);
    }

    public Location findById(Long id) {
        Optional<Location> result = this.repository.findById(id);
        if (result.isPresent()){
            return result.get();
        } else {
            throw new LocationDoesNotExistException(id);
        }
    }

    public void delete(Location location) {
        this.repository.delete(location);
    }
}
