package com.tecsup.petclinic.util;

import com.tecsup.petclinic.dtos.PetDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Pet;

import java.util.ArrayList;
import java.util.List;

public class TObjectCreator {

    public static Pet getPet() {
        return new Pet(1,"Leo",1,1, null);
    }

    public static Pet newPet() {
        Pet newEntity = new Pet();
        newEntity.setName("Punky");
        newEntity.setTypeId(1);
        newEntity.setOwnerId(1);
        return newEntity;
    }

    public static Pet newPetCreated() {
        Pet pet = newPet();
        pet.setId(1000);
        return pet;
    }

    public static PetDTO newPetDTO() {
        PetDTO newDTO = new PetDTO();
        newDTO.setName("Punky");
        newDTO.setTypeId(1);
        newDTO.setOwnerId(1);
        return newDTO;
    }

    public static PetDTO newPetDTOCreated() {
        PetDTO petDTO = newPetDTO();
        petDTO.setId(1000);
        return petDTO;
    }


    public static Pet newPetForUpdate() {
        return new Pet(0,"Bear",1,1,null);
    }

    public static Pet newPetCreatedForUpdate() {
        Pet pet = newPetForUpdate();
        pet.setId(4000);
        return pet;
    }

    public static Pet newPetForDelete() {
        return new Pet(0,"Bird",1,1, null);
    }

    public static Pet newPetCreatedForDelete() {
        Pet pet = newPetForDelete();
        pet.setId(2000);
        return pet;
    }

    public static List<Pet> getPetsForFindByName() {
        List<Pet> pets  = new ArrayList<Pet>();
        pets.add(new Pet(1,"Leo",1,1, null));
        return pets;
    }

    public static List<Pet> getPetsForFindByTypeId() {
        List<Pet> pets  = new ArrayList<Pet>();
        pets.add(new Pet(9,"Lucky",5,7, null));
        pets.add(new Pet(11,"Freddy",5,9, null));
        return pets;
    }

    public static List<Pet> getPetsForFindByOwnerId() {
        List<Pet> pets  = new ArrayList<Pet>();
        pets.add(new Pet(12,"Lucky",2,10, null));
        pets.add(new Pet(13,"Sly",1,10, null));
        return pets;
    }

    // =====================================================
    // Vet — Datos para pruebas de VetController
    // =====================================================

    /**
     * Veterinario existente con id 1 (mismo dato que data.sql).
     *
     * @author Daniel Millones
     */
    public static VetDTO getVetDTO() {
        return VetDTO.builder()
                .id(1)
                .firstName("James")
                .lastName("Carter")
                .email("james.carter@petclinic.com")
                .phone("6085551234")
                .active(true)
                .build();
    }

    /**
     * Lista de veterinarios (mismos datos que data.sql).
     *
     * @author Daniel Millones
     */
    public static List<VetDTO> getAllVetsDTO() {
        List<VetDTO> vets = new ArrayList<VetDTO>();
        vets.add(new VetDTO(1, "James", "Carter", "james.carter@petclinic.com", "6085551234", true));
        vets.add(new VetDTO(2, "Helen", "Leary", "helen.leary@petclinic.com", "6085552345", true));
        vets.add(new VetDTO(3, "Linda", "Douglas", "linda.douglas@petclinic.com", "6085553456", true));
        vets.add(new VetDTO(4, "Rafael", "Ortega", "rafael.ortega@petclinic.com", "6085554567", true));
        vets.add(new VetDTO(5, "Henry", "Stevens", "henry.stevens@petclinic.com", "6085555678", true));
        vets.add(new VetDTO(6, "Sharon", "Jenkins", "sharon.jenkins@petclinic.com", "6085556789", false));
        return vets;
    }
}