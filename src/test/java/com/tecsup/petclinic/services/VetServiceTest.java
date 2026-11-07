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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de VetService con JUnit 5 y Mockito.
 *
 * @author Daniel Millones / Antonio / Castillo
 */
@ExtendWith(MockitoExtension.class)
class VetServiceTest {

    @Mock
    private VetRepository vetRepository;

    @Spy
    private VetMapper vetMapper = Mappers.getMapper(VetMapper.class);

    @InjectMocks
    private VetServiceImpl vetService;

    // ==========================================
    // PARTE DANIEL MILLONES: CRUD Básico
    // ==========================================

    @Test
    @DisplayName("Crear veterinario")
    void testCreateVet() {
        VetDTO nuevo = VetDTO.builder()
                .firstName("James")
                .lastName("Carter")
                .email("james.carter@petclinic.com")
                .phone("6085551234")
                .build();
        Vet guardado = new Vet(1, "James", "Carter", "james.carter@petclinic.com", "6085551234", true);
        when(vetRepository.save(any(Vet.class))).thenReturn(guardado);

        VetDTO result = vetService.create(nuevo);

        assertNotNull(result.getId());
        assertEquals("James", result.getFirstName());
        assertEquals("Carter", result.getLastName());
        assertEquals("james.carter@petclinic.com", result.getEmail());
        assertEquals("6085551234", result.getPhone());
        assertTrue(result.getActive());
        verify(vetRepository, times(1)).save(any(Vet.class));
    }

    @Test
    @DisplayName("Actualizar datos de veterinario")
    void testUpdateVet() throws VetNotFoundException {
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

        VetDTO result = vetService.update(cambios);

        assertEquals(1, result.getId());
        assertEquals("Helen", result.getFirstName());
        assertEquals("Leary", result.getLastName());
        assertEquals("helen.leary@petclinic.com", result.getEmail());
        assertEquals("6085552345", result.getPhone());
        verify(vetRepository, times(1)).findById(1);
        verify(vetRepository, times(1)).save(existente);
    }

    @Test
    @DisplayName("Buscar veterinario existente por id")
    void testFindVetById() throws VetNotFoundException {
        Vet vet = new Vet(1, "James", "Carter", "james.carter@petclinic.com", "6085551234", true);
        when(vetRepository.findById(1)).thenReturn(Optional.of(vet));

        VetDTO result = vetService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("James", result.getFirstName());
        assertEquals("james.carter@petclinic.com", result.getEmail());
        verify(vetRepository, times(1)).findById(1);
    }

    // ==========================================
    // PARTE ANTONIO (Integrante C): Casos de Error y Filtros
    // ==========================================

    @Test
    @DisplayName("1. Buscar un veterinario inexistente debe lanzar VetNotFoundException")
    void testFindVetById_NotFound() {
        Integer idInexistente = 999999;
        when(vetRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(VetNotFoundException.class, () -> {
            vetService.findById(idInexistente);
        });
        verify(vetRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("2. Desactivar un veterinario inexistente debe lanzar VetNotFoundException")
    void testDeactivateVet_NotFound() {
        Integer idInexistente = 999999;
        when(vetRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(VetNotFoundException.class, () -> {
            vetService.deactivate(idInexistente);
        });
        verify(vetRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("3. Buscar veterinarios inactivos debe devolver únicamente registros desactivados")
    void testFindInactiveVets() {
        Vet inactivo = new Vet(1, "James", "Carter", "james.carter@petclinic.com", "6085551234", false);
        when(vetRepository.findByActiveFalse()).thenReturn(List.of(inactivo));

        List<VetDTO> result = vetService.findInactiveVets();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertFalse(result.get(0).getActive());
        verify(vetRepository, times(1)).findByActiveFalse();
    }

    // ==========================================
    // PARTE CASTILLO: Activación lógica y filtros
    // ==========================================

    @Test
    @DisplayName("Desactivar veterinario sin eliminarlo")
    void testDeactivateVet() throws VetNotFoundException {
        Vet vet = new Vet(1, "Ana", "Rojas", "ana@example.com", "999111222", true);
        when(vetRepository.findById(1)).thenReturn(Optional.of(vet));
        when(vetRepository.save(vet)).thenReturn(vet);

        VetDTO result = vetService.deactivate(1);

        assertFalse(result.getActive());
        verify(vetRepository).findById(1);
        verify(vetRepository).save(vet);
        verify(vetRepository, never()).delete(vet);
        verifyNoMoreInteractions(vetRepository);
    }

    @Test
    @DisplayName("Reactivar veterinario")
    void testReactivateVet() throws VetNotFoundException {
        Vet vet = new Vet(2, "Luis", "Paz", "luis@example.com", "999333444", false);
        when(vetRepository.findById(2)).thenReturn(Optional.of(vet));
        when(vetRepository.save(vet)).thenReturn(vet);

        VetDTO result = vetService.reactivate(2);

        assertTrue(result.getActive());
        verify(vetRepository).findById(2);
        verify(vetRepository).save(vet);
        verify(vetRepository, never()).delete(vet);
        verifyNoMoreInteractions(vetRepository);
    }

    @Test
    @DisplayName("Listar veterinarios activos como DTO")
    void testFindActiveVets() {
        List<Vet> activeVets = List.of(
                new Vet(1, "Ana", "Rojas", "ana@example.com", "999111222", true),
                new Vet(3, "Eva", "Mora", "eva@example.com", "999555666", true)
        );
        when(vetRepository.findByActiveTrue()).thenReturn(activeVets);

        List<VetDTO> result = vetService.findActiveVets();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(VetDTO::getActive));
        assertEquals("Ana", result.get(0).getFirstName());
        verify(vetRepository).findByActiveTrue();
        verifyNoMoreInteractions(vetRepository);
    }
}
