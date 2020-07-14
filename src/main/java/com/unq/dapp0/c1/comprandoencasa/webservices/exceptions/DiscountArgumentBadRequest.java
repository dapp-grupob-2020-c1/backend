package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DiscountArgumentBadRequest extends ResponseStatusException {
    public DiscountArgumentBadRequest(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
