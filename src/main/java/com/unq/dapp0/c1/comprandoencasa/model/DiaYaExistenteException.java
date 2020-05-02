package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.DayOfWeek;

public class DiaYaExistenteException extends RuntimeException {
    public DiaYaExistenteException(DayOfWeek dia){
        super("El dia "+dia.name()+" ya esta presente");
    }
}
