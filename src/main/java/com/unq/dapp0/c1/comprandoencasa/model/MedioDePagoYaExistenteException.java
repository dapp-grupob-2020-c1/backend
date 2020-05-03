package com.unq.dapp0.c1.comprandoencasa.model;

public class MedioDePagoYaExistenteException extends RuntimeException{
    public MedioDePagoYaExistenteException(MedioDePago medioDePago){
        super("El medio de pago "+medioDePago.name()+" ya existe");
    }
}
