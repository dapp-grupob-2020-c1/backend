package com.unq.dapp0.c1.comprandoencasa.model;

public class LocationAlreadyPresentException extends RuntimeException {
    public LocationAlreadyPresentException(Location location) {
        super("Location "+location.getID()+" already exists");
    }
}
