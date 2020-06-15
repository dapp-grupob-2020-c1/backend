package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ShopNotFoundException extends ResponseStatusException {
    public ShopNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, "Shop with id " + id + " does not exist");
    }
}
