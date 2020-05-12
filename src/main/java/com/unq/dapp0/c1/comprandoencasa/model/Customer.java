package com.unq.dapp0.c1.comprandoencasa.model;

import java.util.ArrayList;
import java.util.Optional;

public class Customer extends CECUser {
    private ArrayList<Location> locations;

    public Customer(String name, String password, String email) {
        super(name, password, email);
        this.locations = new ArrayList<>();
    }

    public void validate(String name, String password, String email) throws Exception {
        this.validate(name, password, email,new InvalidUserException() );
    }

    public ArrayList<Location> getLocations() {
        return this.locations;
    }

    public void addLocation(Location location) {
        if (!this.findLocation(location).isPresent()) {
            this.locations.add(location);
        } else {
            throw new LocationAlreadyPresentException(location);
        }
    }

    public void removeLocation(Location location) {
        this.findLocation(location).ifPresent(value -> this.locations.remove(value));
    }

    private Optional<Location> findLocation(Location location) {
        return this.locations.stream().filter(loc -> loc.getID().equals(location.getID())).findFirst();
    }
}
