package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

public class InvalidManagerException extends RuntimeException {
    public InvalidManagerException() {
        super("Invalid manager access");
    }
}
