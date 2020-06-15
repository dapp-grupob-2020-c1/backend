package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailFormatBadRequestException extends ResponseStatusException {
    public EmailFormatBadRequestException() {
        super(HttpStatus.BAD_REQUEST, "The email needs to follow the format: user@provider.com");
    }
}
