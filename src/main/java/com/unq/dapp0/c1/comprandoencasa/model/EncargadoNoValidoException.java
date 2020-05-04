package com.unq.dapp0.c1.comprandoencasa.model;

public class EncargadoNoValidoException extends RuntimeException{
    public EncargadoNoValidoException(){
        super("El encargado no es válido");
    }
}
