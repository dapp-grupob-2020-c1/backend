package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ComercioTests {

    @Test
    public void comercioPoseeElRubroDeAlimentosY(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        rubros.add(Rubro.AlimentosYBebidas);
        rubros.add(Rubro.Libreria);
        String domicilio = "Calle Test 123";
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

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
        String domicilio = "Calle Test 123";
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

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
        String domicilio = "Calle Test 123";
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getRubros().contains(Rubro.AlimentosYBebidas));
        assertTrue(comercio.getRubros().contains(Rubro.Libreria));

        comercio.removeRubro(Rubro.Libreria);
        assertFalse(comercio.getRubros().contains(Rubro.Libreria));
    }

    @Test
    public void comercioPoseeUnDomicilio(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertEquals(comercio.getDomicilio(), "Calle Test 123");
    }

    @Test
    public void comercioPoseeDiasDeAtencion(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.TUESDAY);
        dias.add(DayOfWeek.WEDNESDAY);
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getDias().contains(DayOfWeek.MONDAY));
        assertTrue(comercio.getDias().contains(DayOfWeek.TUESDAY));
        assertTrue(comercio.getDias().contains(DayOfWeek.WEDNESDAY));
        assertFalse(comercio.getDias().contains(DayOfWeek.THURSDAY));
        assertFalse(comercio.getDias().contains(DayOfWeek.FRIDAY));
        assertFalse(comercio.getDias().contains(DayOfWeek.SATURDAY));
        assertFalse(comercio.getDias().contains(DayOfWeek.SUNDAY));
    }

    @Test
    public void comercioPuedeQuitarUnDiaDeAtencion(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.TUESDAY);
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getDias().contains(DayOfWeek.MONDAY));
        assertTrue(comercio.getDias().contains(DayOfWeek.TUESDAY));

        comercio.removeDia(DayOfWeek.TUESDAY);

        assertFalse(comercio.getDias().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void comercioPuedeAgregarUnDiaDeAtencion(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getDias().contains(DayOfWeek.MONDAY));
        assertFalse(comercio.getDias().contains(DayOfWeek.TUESDAY));

        comercio.addDia(DayOfWeek.TUESDAY);

        assertTrue(comercio.getDias().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void comercioNoPuedeAgregarUnDiaDeAtencionQueYaPosee(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.TUESDAY);
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getDias().contains(DayOfWeek.MONDAY));
        assertTrue(comercio.getDias().contains(DayOfWeek.TUESDAY));

        Throwable exception = assertThrows(DiaYaExistenteException.class, ()->comercio.addDia(DayOfWeek.TUESDAY));

        assertEquals("El dia "+DayOfWeek.TUESDAY.name()+" ya esta presente",exception.getMessage());
        assertEquals(comercio.getDias().size(), 2);
    }

    @Test
    public void comercioPoseeHorariosDeAperturaYCierre(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertEquals(comercio.getHorarioApertura(), LocalTime.of(8, 0));
        assertEquals(comercio.getHorarioCierre(), LocalTime.of(16, 0));
    }

    @Test
    public void comercioPuedeCambiarSuHorarioDeApertura(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertEquals(comercio.getHorarioApertura(), LocalTime.of(8, 0));

        comercio.setHorarioApertura(LocalTime.of(17, 0));

        assertEquals(comercio.getHorarioApertura(), LocalTime.of(17, 0));
    }

    @Test
    public void comercioPuedeCambiarSuHorarioDeCierre(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);
        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertEquals(comercio.getHorarioCierre(), LocalTime.of(16, 0));

        comercio.setHorarioCierre(LocalTime.of(5, 0));

        assertEquals(comercio.getHorarioCierre(), LocalTime.of(5, 0));
    }

    @Test
    public void comercioPoseeMediosDePago(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);

        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();
        mediosDePago.add(MedioDePago.EFECTIVO);
        mediosDePago.add(MedioDePago.DEBITO);

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.EFECTIVO));
        assertTrue(comercio.getMediosDePago().contains(MedioDePago.DEBITO));
        assertFalse(comercio.getMediosDePago().contains(MedioDePago.MERCADOPAGO));
        assertFalse(comercio.getMediosDePago().contains(MedioDePago.CREDITO));
    }

    @Test
    public void comercioPuedeAgregarUnNuevoMedioDePago(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);

        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();
        mediosDePago.add(MedioDePago.EFECTIVO);

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.EFECTIVO));
        assertFalse(comercio.getMediosDePago().contains(MedioDePago.DEBITO));

        comercio.addMedioDePago(MedioDePago.DEBITO);

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.DEBITO));
    }

    @Test
    public void comercioPuedeQuitarUnMedioDePago(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);

        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();
        mediosDePago.add(MedioDePago.EFECTIVO);
        mediosDePago.add(MedioDePago.DEBITO);

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.EFECTIVO));
        assertTrue(comercio.getMediosDePago().contains(MedioDePago.DEBITO));

        comercio.removeMedioDePago(MedioDePago.DEBITO);

        assertFalse(comercio.getMediosDePago().contains(MedioDePago.DEBITO));
    }

    @Test
    public void comercioNoPuedeAgregarUnMedioDePagoYaExistente(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        String domicilio = "Calle Test 123";

        ArrayList<DayOfWeek> dias = new ArrayList<>();
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);

        ArrayList<MedioDePago> mediosDePago = new ArrayList<MedioDePago>();
        mediosDePago.add(MedioDePago.EFECTIVO);
        mediosDePago.add(MedioDePago.DEBITO);

        Comercio comercio = new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.EFECTIVO));
        assertTrue(comercio.getMediosDePago().contains(MedioDePago.DEBITO));

        Throwable exception = assertThrows(MedioDePagoYaExistenteException.class,()->comercio.addMedioDePago(MedioDePago.DEBITO));

        assertEquals("El medio de pago "+MedioDePago.DEBITO.name()+" ya existe", exception.getMessage());
        assertEquals(2, comercio.getMediosDePago().size());
    }
}
