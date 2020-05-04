package com.unq.dapp0.c1.comprandoencasa.model;

public class InvalidManagerException extends RuntimeException{
    public InvalidManagerException(){
        super("Invalid manager access");
    }
}
