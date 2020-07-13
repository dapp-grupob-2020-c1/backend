package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DiscountNotFoundException extends ResponseStatusException {
    public DiscountNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
