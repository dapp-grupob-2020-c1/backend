package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ShoppingListNotFound extends ResponseStatusException {
    public ShoppingListNotFound(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
