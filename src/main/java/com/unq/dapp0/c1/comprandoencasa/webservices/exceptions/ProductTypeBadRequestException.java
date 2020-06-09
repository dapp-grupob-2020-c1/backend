package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductTypeBadRequestException extends ResponseStatusException {
    public ProductTypeBadRequestException() {
        super(HttpStatus.BAD_REQUEST, "Product type category not found");
    }
}
