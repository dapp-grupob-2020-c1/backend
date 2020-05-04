package com.unq.dapp0.c1.comprandoencasa.model;

/**
 * Representación del encargado de un comercio. El mismo es el único que puede
 * acceder y cambiar los datos del comercio.
 */
public class Encargado {
    /**
     * Valida que el encargado sea el mismo. Levanta una EncargadoNoValidoException
     * en el caso de que no sea el mismo.
     * Este método permite mantener valores como IDs y contraseñas privadas.
     * @param encargado un encargado a validar.
     */
    public void validar(Encargado encargado) {
    }
}
