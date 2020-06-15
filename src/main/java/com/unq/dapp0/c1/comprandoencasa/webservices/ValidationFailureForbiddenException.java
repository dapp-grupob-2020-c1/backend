package com.unq.dapp0.c1.comprandoencasa.webservices;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationFailureForbiddenException extends ResponseStatusException {
    public ValidationFailureForbiddenException() {
        super(HttpStatus.FORBIDDEN, "Incorrect email or password");
    }
}
