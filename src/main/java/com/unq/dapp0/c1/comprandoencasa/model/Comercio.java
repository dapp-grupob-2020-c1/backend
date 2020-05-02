package com.unq.dapp0.c1.comprandoencasa.model;

import java.util.ArrayList;

public class Comercio {
    private ArrayList<Rubro> rubros;

    public Comercio(ArrayList<Rubro> rubros) {
        this.rubros = rubros;
    }

    public ArrayList<Rubro> getRubros() {
        return this.rubros;
    }

    public void addRubro(Rubro rubro) {
        this.rubros.add(rubro);
    }

    public void removeRubro(Rubro rubro) {
        this.rubros.remove(rubro);
    }
}
