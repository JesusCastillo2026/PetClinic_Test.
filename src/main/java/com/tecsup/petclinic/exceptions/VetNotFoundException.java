package com.tecsup.petclinic.exceptions;

public class VetNotFoundException extends RuntimeException {

    public VetNotFoundException(Integer id) {
        super("No se encontró el veterinario con ID " + id);
    }
}
