package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Representa la ficha y la información de gestión de un comercio.
 */
public class Comercio {
    private ArrayList<Rubro> rubros;
    private String domicilio;
    private ArrayList<DayOfWeek> dias;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private ArrayList<MedioDePago> mediosDePago;
    private Integer distanciaDeEnvioEnKM;
    private final Encargado encargado;

    public Comercio(ArrayList<Rubro> rubros, String domicilio, ArrayList<DayOfWeek> dias, LocalTime horarioApertura, LocalTime horarioCierre, ArrayList<MedioDePago> mediosDePago, Integer distanciaDeEnvioEnKM, Encargado encargado) {
        this.rubros = rubros;
        this.domicilio = domicilio;
        this.dias = dias;
        this.horarioApertura = horarioApertura;
        this.horarioCierre = horarioCierre;
        this.mediosDePago = mediosDePago;
        this.distanciaDeEnvioEnKM = distanciaDeEnvioEnKM;
        this.encargado = encargado;
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

    public String getDomicilio() {
        return this.domicilio;
    }

    public ArrayList<DayOfWeek> getDias() {
        return this.dias;
    }

    public LocalTime getHorarioApertura() {
        return this.horarioApertura;
    }

    public LocalTime getHorarioCierre() {
        return this.horarioCierre;
    }

    public void removeDia(DayOfWeek dia) {
        this.dias.remove(dia);
    }

    public void addDia(DayOfWeek dia) {
        if (this.dias.contains(dia)){
            throw new DiaYaExistenteException(dia);
        }
        this.dias.add(dia);
    }

    public void setHorarioApertura(LocalTime nuevoHorario) {
        this.horarioApertura = nuevoHorario;
    }

    public void setHorarioCierre(LocalTime nuevoHorario) {
        this.horarioCierre = nuevoHorario;
    }

    public ArrayList<MedioDePago> getMediosDePago() {
        return this.mediosDePago;
    }

    public void addMedioDePago(MedioDePago medioDePago) {
        if (this.mediosDePago.contains(medioDePago)){
            throw new MedioDePagoYaExistenteException(medioDePago);
        }
        this.mediosDePago.add(medioDePago);
    }

    public void removeMedioDePago(MedioDePago medioDePago) {
        this.mediosDePago.remove(medioDePago);
    }

    public Integer getDistanciaDeEnvioEnKM() {
        return this.distanciaDeEnvioEnKM;
    }

    public void setDistanciaDeEnvioEnKM(Integer distancia) {
        this.distanciaDeEnvioEnKM = distancia;
    }

    public void validarEncargado(Encargado encargado) {
        this.encargado.validar(encargado);
    }
}
