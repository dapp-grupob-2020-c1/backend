package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.Location;

import java.util.List;

public class LocationDTO {
    public Long customerId;
    public List<Location> locations;

    public LocationDTO(){}

    public LocationDTO(Long customerId, List<Location> locationList) {
        this.customerId = customerId;
        this.locations = locationList;
    }
}
