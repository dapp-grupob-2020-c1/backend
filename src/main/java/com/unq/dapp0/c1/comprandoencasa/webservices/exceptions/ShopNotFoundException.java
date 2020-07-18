package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ShopNotFoundException extends ResponseStatusException {
    public ShopNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
