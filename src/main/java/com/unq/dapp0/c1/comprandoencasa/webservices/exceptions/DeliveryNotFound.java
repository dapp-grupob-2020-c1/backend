package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DeliveryNotFound extends ResponseStatusException {
    public DeliveryNotFound(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
