package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class VetServiceTest {

    @Autowired
    private VetService vetService;

    // ==========================================
    // INTEGRANTE C: Casos de Error y Filtros
    // ==========================================

    @Test
    @DisplayName("1. Buscar un veterinario inexistente debe lanzar VetNotFoundException")
    public void testFindVetById_NotFound() {
        Integer idInexistente = 999999;

        // Verifica que al buscar un ID no registrado se lance la excepción
        assertThrows(VetNotFoundException.class, () -> {
            vetService.findById(idInexistente);
        });
    }

    @Test
    @DisplayName("2. Desactivar un veterinario inexistente debe lanzar VetNotFoundException")
    public void testDeactivateVet_NotFound() {
        Integer idInexistente = 999999;

        // Verifica que al intentar desactivar un ID no registrado se lance la excepción
        assertThrows(VetNotFoundException.class, () -> {
            vetService.deactivate(idInexistente);
        });
    }

    @Test
    @DisplayName("3. Buscar veterinarios inactivos debe devolver únicamente registros desactivados")
    public void testFindInactiveVets() throws Exception {
        // 1. Desactivamos un veterinario existente (por ejemplo ID 1) para tener al menos uno inactivo
        vetService.deactivate(1);

        // 2. Obtenemos la lista de veterinarios inactivos
        List<VetDTO> inactiveVets = vetService.findInactiveVets();

        // 3. Validaciones
        assertNotNull(inactiveVets, "La lista de inactivos no debe ser nula");
        assertFalse(inactiveVets.isEmpty(), "Debe haber al menos un veterinario inactivo");

        // 4. Confirmamos que el veterinario desactivado esté en la lista
        boolean contieneDesactivado = inactiveVets.stream()
                .anyMatch(v -> v.getId().equals(1));
        assertTrue(contieneDesactivado, "La lista de inactivos debe incluir el registro desactivado con ID 1");
    }
}