package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ComercioTests {

    @Test
    public void comercioPoseeElRubroDeAlimentosY(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        rubros.add(Rubro.AlimentosYBebidas);
        rubros.add(Rubro.Libreria);
        Comercio comercio = new Comercio(rubros);

        assertTrue(comercio.getRubros().contains(Rubro.AlimentosYBebidas));
        assertTrue(comercio.getRubros().contains(Rubro.Libreria));

        assertFalse(comercio.getRubros().contains(Rubro.ElectronicaYElectrodomesticos));
        assertFalse(comercio.getRubros().contains(Rubro.Entretenimiento));
        assertFalse(comercio.getRubros().contains(Rubro.Servicios));
        assertFalse(comercio.getRubros().contains(Rubro.Hogar));
        assertFalse(comercio.getRubros().contains(Rubro.AnimalesYMascotas));
        assertFalse(comercio.getRubros().contains(Rubro.VehiculosYAccesorios));
        assertFalse(comercio.getRubros().contains(Rubro.Ropa));
        assertFalse(comercio.getRubros().contains(Rubro.Farmacia));
    }

    @Test
    public void comercioPuedeAgregarUnRubroASuListaDeRubros(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        rubros.add(Rubro.AlimentosYBebidas);
        Comercio comercio = new Comercio(rubros);

        assertTrue(comercio.getRubros().contains(Rubro.AlimentosYBebidas));
        assertFalse(comercio.getRubros().contains(Rubro.Libreria));

        comercio.addRubro(Rubro.Libreria);
        assertTrue(comercio.getRubros().contains(Rubro.Libreria));
    }

    @Test
    public void comercioPuedeQuitarUnRubroDeSuListaDeRubros(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        rubros.add(Rubro.AlimentosYBebidas);
        rubros.add(Rubro.Libreria);
        Comercio comercio = new Comercio(rubros);

        assertTrue(comercio.getRubros().contains(Rubro.AlimentosYBebidas));
        assertTrue(comercio.getRubros().contains(Rubro.Libreria));

        comercio.removeRubro(Rubro.Libreria);
        assertFalse(comercio.getRubros().contains(Rubro.Libreria));
    }
}
