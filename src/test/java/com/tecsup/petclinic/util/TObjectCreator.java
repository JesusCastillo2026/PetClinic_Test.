package com.tecsup.petclinic.util;

import com.tecsup.petclinic.dtos.PetDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Creador de objetos de prueba para Vets y Pets
 */
public class TObjectCreator {

    // =====================================================
    // PETS (Metodos base del proyecto)
    // =====================================================

    public static PetDTO getPetDTO() {
        return new PetDTO(1, "Leo", 1, 1, LocalDate.parse("2000-09-07"));
    }

    public static List<Pet> getPetsForFindByName() {
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1, "Leo", 1, 1, LocalDate.parse("2000-09-07")));
        pets.add(new Pet(2, "Basil", 2, 2, LocalDate.parse("2002-08-06")));
        return pets;
    }

    public static List<Pet> getPetsForFindByTypeId() {
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1, "Leo", 1, 1, LocalDate.parse("2000-09-07")));
        return pets;
    }

    public static List<Pet> getPetsForFindByOwnerId() {
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1, "Leo", 1, 1, LocalDate.parse("2000-09-07")));
        return pets;
    }

    public static Pet newPet() {
        return new Pet(null, "Wysocki", 1, 1, LocalDate.parse("2020-05-15"));
    }

    public static Pet newPetCreated() {
        return new Pet(14, "Wysocki", 1, 1, LocalDate.parse("2020-05-15"));
    }

    public static Pet newPetForUpdate() {
        return new Pet(1, "Leo", 1, 1, LocalDate.parse("2000-09-07"));
    }

    public static Pet newPetCreatedForUpdate() {
        return new Pet(1, "Leo Updated", 1, 1, LocalDate.parse("2000-09-07"));
    }

    public static Pet newPetForDelete() {
        return new Pet(1, "Leo", 1, 1, LocalDate.parse("2000-09-07"));
    }

    public static Pet newPetCreatedForDelete() {
        return new Pet(1, "Leo", 1, 1, LocalDate.parse("2000-09-07"));
    }

    // =====================================================
    // VET — Integrante A: Daniel Millones
    // =====================================================

    public static VetDTO getVetDTO() {
        return new VetDTO(1, "James", "Carter", "james.carter@petclinic.com", "6085552368", true);
    }

    public static List<VetDTO> getAllVetsDTO() {
        List<VetDTO> vets = new ArrayList<>();
        vets.add(new VetDTO(1, "James", "Carter", "james.carter@petclinic.com", "6085552368", true));
        vets.add(new VetDTO(2, "Helen", "Leary", "helen.leary@petclinic.com", "6085554766", true));
        vets.add(new VetDTO(3, "Linda", "Douglas", "linda.douglas@petclinic.com", "6085550167", true));
        vets.add(new VetDTO(4, "Rafael", "Ortega", "rafael.ortega@petclinic.com", "6085550178", true));
        vets.add(new VetDTO(5, "Henry", "Stevens", "henry.stevens@petclinic.com", "6085550189", true));
        vets.add(new VetDTO(6, "Sharon", "Jenkins", "sharon.jenkins@petclinic.com", "6085556789", false));
        return vets;
    }

    // =====================================================
    // VET — Integrante B (Creacion y soft delete): Jesus Castillo
    // =====================================================

    /**
     * Veterinario nuevo, sin id (datos de entrada para POST).
     *
     * @author Jesus Castillo
     */
    public static VetDTO newVetDTO() {
        return VetDTO.builder()
                .firstName("Gregory")
                .lastName("House")
                .email("gregory.house@petclinic.com")
                .phone("6085557890")
                .build();
    }

    /**
     * Veterinario ya creado (respuesta esperada del POST).
     *
     * @author Jesus Castillo
     */
    public static VetDTO newVetDTOCreated() {
        VetDTO vet = newVetDTO();
        vet.setId(7);
        vet.setActive(true);
        return vet;
    }

    /**
     * Veterinario desactivado (respuesta esperada de deactivate).
     *
     * @author Jesus Castillo
     */
    public static VetDTO getVetDTODeactivated() {
        VetDTO vet = getVetDTO();
        vet.setActive(false);
        return vet;
    }

    /**
     * Vet 6 reactivado (respuesta esperada de reactivate).
     *
     * @author Jesus Castillo
     */
    public static VetDTO getVetDTOReactivated() {
        return new VetDTO(6, "Sharon", "Jenkins", "sharon.jenkins@petclinic.com", "6085556789", true);
    }

    // =====================================================
    // VET — Integrante C (Filtros y borrado): Luis Antonio Vasquez
    // =====================================================

    /**
     * Veterinarios activos de data.sql (5).
     *
     * @author Luis Antonio Vasquez
     */
    public static List<VetDTO> getActiveVetsDTO() {
        List<VetDTO> vets = new ArrayList<VetDTO>();
        for (VetDTO vet : getAllVetsDTO()) {
            if (vet.getActive()) {
                vets.add(vet);
            }
        }
        return vets;
    }

    /**
     * Veterinarios inactivos de data.sql (1).
     *
     * @author Luis Antonio Vasquez
     */
    public static List<VetDTO> getInactiveVetsDTO() {
        List<VetDTO> vets = new ArrayList<VetDTO>();
        for (VetDTO vet : getAllVetsDTO()) {
            if (!vet.getActive()) {
                vets.add(vet);
            }
        }
        return vets;
    }
}