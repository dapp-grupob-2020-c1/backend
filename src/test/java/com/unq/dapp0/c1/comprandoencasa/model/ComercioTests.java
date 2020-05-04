package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComercioTests {

    @Test
    public void comercioPoseeLosRubrosDeAlimentosYBebidasYLibreria(){
        ArrayList<Rubro> rubros = new ArrayList<>();
        rubros.add(Rubro.AlimentosYBebidas);
        rubros.add(Rubro.Libreria);

        Comercio comercio = ComercioFactory.unComercio().conRubros(rubros).build();

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

        Comercio comercio = ComercioFactory.unComercio().conRubros(rubros).build();

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

        Comercio comercio = ComercioFactory.unComercio().conRubros(rubros).build();

        assertTrue(comercio.getRubros().contains(Rubro.AlimentosYBebidas));
        assertTrue(comercio.getRubros().contains(Rubro.Libreria));

        comercio.removeRubro(Rubro.Libreria);
        assertFalse(comercio.getRubros().contains(Rubro.Libreria));
    }

    @Test
    public void comercioPoseeUnDomicilio(){
        String domicilio = "Calle Test 123";

        Comercio comercio = ComercioFactory.unComercio().conDomicilio(domicilio).build();

        assertEquals(comercio.getDomicilio(), "Calle Test 123");
    }

    @Test
    public void comercioPoseeDiasDeAtencion(){
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.TUESDAY);
        dias.add(DayOfWeek.WEDNESDAY);

        Comercio comercio = ComercioFactory.unComercio().conDias(dias).build();

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
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.TUESDAY);

        Comercio comercio = ComercioFactory.unComercio().conDias(dias).build();

        assertTrue(comercio.getDias().contains(DayOfWeek.MONDAY));
        assertTrue(comercio.getDias().contains(DayOfWeek.TUESDAY));

        comercio.removeDia(DayOfWeek.TUESDAY);

        assertFalse(comercio.getDias().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void comercioPuedeAgregarUnDiaDeAtencion(){
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);

        Comercio comercio = ComercioFactory.unComercio().conDias(dias).build();

        assertTrue(comercio.getDias().contains(DayOfWeek.MONDAY));
        assertFalse(comercio.getDias().contains(DayOfWeek.TUESDAY));

        comercio.addDia(DayOfWeek.TUESDAY);

        assertTrue(comercio.getDias().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void comercioNoPuedeAgregarUnDiaDeAtencionQueYaPosee(){
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.TUESDAY);

        Comercio comercio = ComercioFactory.unComercio().conDias(dias).build();

        assertTrue(comercio.getDias().contains(DayOfWeek.MONDAY));
        assertTrue(comercio.getDias().contains(DayOfWeek.TUESDAY));

        Throwable exception = assertThrows(DiaYaExistenteException.class, ()->comercio.addDia(DayOfWeek.TUESDAY));

        assertEquals("El dia "+DayOfWeek.TUESDAY.name()+" ya esta presente",exception.getMessage());
        assertEquals(comercio.getDias().size(), 2);
    }

    @Test
    public void comercioPoseeHorariosDeAperturaYCierre(){
        LocalTime horarioApertura = LocalTime.of(8,0);
        LocalTime horarioCierre = LocalTime.of(16, 0);

        Comercio comercio = ComercioFactory.unComercio()
                .conHorarioApertura(horarioApertura)
                .conHorarioCierre(horarioCierre)
                .build();

        assertEquals(comercio.getHorarioApertura(), LocalTime.of(8, 0));
        assertEquals(comercio.getHorarioCierre(), LocalTime.of(16, 0));
    }

    @Test
    public void comercioPuedeCambiarSuHorarioDeApertura(){
        LocalTime horarioApertura = LocalTime.of(8,0);

        Comercio comercio = ComercioFactory.unComercio()
                .conHorarioApertura(horarioApertura)
                .build();

        assertEquals(comercio.getHorarioApertura(), LocalTime.of(8, 0));

        comercio.setHorarioApertura(LocalTime.of(17, 0));

        assertEquals(comercio.getHorarioApertura(), LocalTime.of(17, 0));
    }

    @Test
    public void comercioPuedeCambiarSuHorarioDeCierre(){
        LocalTime horarioCierre = LocalTime.of(16, 0);

        Comercio comercio = ComercioFactory.unComercio()
                .conHorarioCierre(horarioCierre)
                .build();

        assertEquals(comercio.getHorarioCierre(), LocalTime.of(16, 0));

        comercio.setHorarioCierre(LocalTime.of(5, 0));

        assertEquals(comercio.getHorarioCierre(), LocalTime.of(5, 0));
    }

    @Test
    public void comercioPoseeMediosDePago(){
        ArrayList<MedioDePago> mediosDePago = new ArrayList<>();
        mediosDePago.add(MedioDePago.EFECTIVO);
        mediosDePago.add(MedioDePago.DEBITO);

        Comercio comercio = ComercioFactory.unComercio()
                .conMediosDePago(mediosDePago)
                .build();

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.EFECTIVO));
        assertTrue(comercio.getMediosDePago().contains(MedioDePago.DEBITO));
        assertFalse(comercio.getMediosDePago().contains(MedioDePago.MERCADOPAGO));
        assertFalse(comercio.getMediosDePago().contains(MedioDePago.CREDITO));
    }

    @Test
    public void comercioPuedeAgregarUnNuevoMedioDePago(){
        ArrayList<MedioDePago> mediosDePago = new ArrayList<>();
        mediosDePago.add(MedioDePago.EFECTIVO);

        Comercio comercio = ComercioFactory.unComercio()
                .conMediosDePago(mediosDePago)
                .build();

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.EFECTIVO));
        assertFalse(comercio.getMediosDePago().contains(MedioDePago.DEBITO));

        comercio.addMedioDePago(MedioDePago.DEBITO);

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.DEBITO));
    }

    @Test
    public void comercioPuedeQuitarUnMedioDePago(){
        ArrayList<MedioDePago> mediosDePago = new ArrayList<>();
        mediosDePago.add(MedioDePago.EFECTIVO);
        mediosDePago.add(MedioDePago.DEBITO);

        Comercio comercio = ComercioFactory.unComercio()
                .conMediosDePago(mediosDePago)
                .build();

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.EFECTIVO));
        assertTrue(comercio.getMediosDePago().contains(MedioDePago.DEBITO));

        comercio.removeMedioDePago(MedioDePago.DEBITO);

        assertFalse(comercio.getMediosDePago().contains(MedioDePago.DEBITO));
    }

    @Test
    public void comercioNoPuedeAgregarUnMedioDePagoYaExistente(){
        ArrayList<MedioDePago> mediosDePago = new ArrayList<>();
        mediosDePago.add(MedioDePago.EFECTIVO);
        mediosDePago.add(MedioDePago.DEBITO);

        Comercio comercio = ComercioFactory.unComercio()
                .conMediosDePago(mediosDePago)
                .build();

        assertTrue(comercio.getMediosDePago().contains(MedioDePago.EFECTIVO));
        assertTrue(comercio.getMediosDePago().contains(MedioDePago.DEBITO));

        Throwable exception = assertThrows(MedioDePagoYaExistenteException.class,()->comercio.addMedioDePago(MedioDePago.DEBITO));

        assertEquals("El medio de pago "+MedioDePago.DEBITO.name()+" ya existe", exception.getMessage());
        assertEquals(2, comercio.getMediosDePago().size());
    }
}

class ComercioFactory{
    public static ComercioFactory unComercio(){
        return new ComercioFactory();
    }

    private ArrayList<Rubro> rubros;
    private String domicilio;
    private ArrayList<DayOfWeek> dias;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private ArrayList<MedioDePago> mediosDePago;

    public ComercioFactory(){
        this.rubros = new ArrayList<>();
        this.domicilio = "";
        this.dias = new ArrayList<>();
        this.horarioApertura = LocalTime.of(8,0);
        this.horarioCierre = LocalTime.of(17,0);
        this.mediosDePago = new ArrayList<>();
    }

    public ComercioFactory conRubros(ArrayList<Rubro> rubros){
        this.rubros = rubros;
        return this;
    }
    public ComercioFactory conDomicilio(String domicilio){
        this.domicilio = domicilio;
        return this;
    }
    public ComercioFactory conDias(ArrayList<DayOfWeek> dias){
        this.dias = dias;
        return this;
    }
    public ComercioFactory conHorarioApertura(LocalTime horarioApertura){
        this.horarioApertura = horarioApertura;
        return this;
    }
    public ComercioFactory conHorarioCierre(LocalTime horarioCierre){
        this.horarioCierre = horarioCierre;
        return this;
    }
    public ComercioFactory conMediosDePago(ArrayList<MedioDePago> mediosDePago){
        this.mediosDePago = mediosDePago;
        return this;
    }

    public Comercio build(){
        return new Comercio(rubros, domicilio, dias, horarioApertura, horarioCierre, mediosDePago);
    }
}
