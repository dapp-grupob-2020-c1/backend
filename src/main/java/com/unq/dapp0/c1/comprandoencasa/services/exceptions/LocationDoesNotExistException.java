package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class LocationDoesNotExistException extends RuntimeException {
    public LocationDoesNotExistException(Long locationId) {
        super("Location with the id " + locationId + " does not exist");
    }
}
