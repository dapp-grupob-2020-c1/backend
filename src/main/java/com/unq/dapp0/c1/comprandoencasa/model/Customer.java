package com.unq.dapp0.c1.comprandoencasa.model;

import java.util.ArrayList;
import java.util.Optional;

public class Customer {
    private final String name;
    private final String password;
    private final String email;
    private ArrayList<Location> locations;

    public Customer(String name, String password, String email) {
        this.checkEmailFormat(email);

        this.name = name;
        this.password = password;
        this.email = email;
        this.locations = new ArrayList<>();
    }

    private void checkEmailFormat(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new InvalidEmailFormatException();
        }
    }

    public void validate(String name, String password, String email) {
        if (!this.name.equals(name) && !this.email.equals(email) && !this.password.equals(password)) {
            throw new InvalidUserException();
        }
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
