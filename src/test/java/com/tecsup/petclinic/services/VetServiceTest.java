package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.repositories.VetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de VetService con JUnit 5 y Mockito.
 *
 * @author Daniel Millones
 */
@ExtendWith(MockitoExtension.class)
class VetServiceTest {

    @Mock
    private VetRepository vetRepository;

    @Spy
    private VetMapper vetMapper = Mappers.getMapper(VetMapper.class);

    @InjectMocks
    private VetServiceImpl vetService;

    // ===== CRUD básico =====

    /**
     * Verifica que se cree un veterinario activo.
     *
     * @author Daniel Millones
     */
    @Test
    @DisplayName("Crear veterinario")
    void testCreateVet() {
        // Arrange
        VetDTO nuevo = VetDTO.builder()
                .firstName("James")
                .lastName("Carter")
                .email("james.carter@petclinic.com")
                .phone("6085551234")
                .build();
        Vet guardado = new Vet(1, "James", "Carter", "james.carter@petclinic.com", "6085551234", true);
        when(vetRepository.save(any(Vet.class))).thenReturn(guardado);

        // Act
        VetDTO result = vetService.create(nuevo);

        // Assert
        assertNotNull(result.getId());
        assertEquals("James", result.getFirstName());
        assertEquals("Carter", result.getLastName());
        assertEquals("james.carter@petclinic.com", result.getEmail());
        assertEquals("6085551234", result.getPhone());
        assertTrue(result.getActive());
        verify(vetRepository, times(1)).save(any(Vet.class));
    }

    /**
     * Verifica que se actualicen los datos de un veterinario existente.
     *
     * @author Daniel Millones
     */
    @Test
    @DisplayName("Actualizar datos de veterinario")
    void testUpdateVet() throws VetNotFoundException {
        // Arrange
        Vet existente = new Vet(1, "James", "Carter", "james.carter@petclinic.com", "6085551234", true);
        VetDTO cambios = VetDTO.builder()
                .id(1)
                .firstName("Helen")
                .lastName("Leary")
                .email("helen.leary@petclinic.com")
                .phone("6085552345")
                .build();
        when(vetRepository.findById(1)).thenReturn(Optional.of(existente));
        when(vetRepository.save(any(Vet.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        VetDTO result = vetService.update(cambios);

        // Assert
        assertEquals(1, result.getId());
        assertEquals("Helen", result.getFirstName());
        assertEquals("Leary", result.getLastName());
        assertEquals("helen.leary@petclinic.com", result.getEmail());
        assertEquals("6085552345", result.getPhone());
        verify(vetRepository, times(1)).findById(1);
        verify(vetRepository, times(1)).save(existente);
    }

    /**
     * Verifica que se encuentre un veterinario existente por su id.
     *
     * @author Daniel Millones
     */
    @Test
    @DisplayName("Buscar veterinario existente por id")
    void testFindVetById() throws VetNotFoundException {
        // Arrange
        Vet vet = new Vet(1, "James", "Carter", "james.carter@petclinic.com", "6085551234", true);
        when(vetRepository.findById(1)).thenReturn(Optional.of(vet));

        // Act
        VetDTO result = vetService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("James", result.getFirstName());
        assertEquals("james.carter@petclinic.com", result.getEmail());
        verify(vetRepository, times(1)).findById(1);
    }
}