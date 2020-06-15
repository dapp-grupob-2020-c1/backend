package com.unq.dapp0.c1.comprandoencasa.webservices;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FieldAlreadyExistsForbiddenException extends ResponseStatusException {
    public FieldAlreadyExistsForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
