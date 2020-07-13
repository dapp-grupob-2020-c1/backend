package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;

public class LocationAlreadyPresentException extends RuntimeException {
    public LocationAlreadyPresentException(Location location) {
        super("Location " + location.getId() + " already exists");
    }
}
