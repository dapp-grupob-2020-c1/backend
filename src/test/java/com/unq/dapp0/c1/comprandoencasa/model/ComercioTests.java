package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComercioTests {
    @Test
    public void comercioPoseeElRubroDeAlimentos(){
        Comercio comercio = new Comercio(Rubro.Alimentos);
        assertEquals(comercio.rubro, Rubro.Alimentos);
    }
}
