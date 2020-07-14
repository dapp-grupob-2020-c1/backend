package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductNotFoundException extends ResponseStatusException {
    public ProductNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Product with id " + id + " does not exist");
    }

    public ProductNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
