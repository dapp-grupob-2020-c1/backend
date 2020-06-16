package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class FieldAlreadyExistsException extends RuntimeException{
    public FieldAlreadyExistsException(String field){
        super("The field " + field + " already exists");
    }
}
