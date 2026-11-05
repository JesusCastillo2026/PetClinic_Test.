package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;

import java.util.List;

public interface VetService {

    void deactivateVet(Integer id);

    void reactivateVet(Integer id);

    List<Vet> findActiveVets();
}
