package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LocationNotFoundException extends ResponseStatusException {
    public LocationNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, "Location with id " + id + " does not exist");
    }
}
