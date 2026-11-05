package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.repositories.VetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VetServiceTest {

    @Mock
    private VetRepository vetRepository;

    @InjectMocks
    private VetServiceImpl vetService;

    @Test
    void testDeactivateVet() {
        Vet vet = new Vet(1, "Ana", "Rojas", "ana@example.com", "999111222", true);
        when(vetRepository.findById(1)).thenReturn(Optional.of(vet));

        vetService.deactivateVet(1);

        assertFalse(vet.isActive());
        verify(vetRepository).findById(1);
        verify(vetRepository).save(vet);
        verify(vetRepository, never()).delete(vet);
        verifyNoMoreInteractions(vetRepository);
    }

    @Test
    void testReactivateVet() {
        Vet vet = new Vet(2, "Luis", "Paz", "luis@example.com", "999333444", false);
        when(vetRepository.findById(2)).thenReturn(Optional.of(vet));

        vetService.reactivateVet(2);

        assertTrue(vet.isActive());
        verify(vetRepository).findById(2);
        verify(vetRepository).save(vet);
        verify(vetRepository, never()).delete(vet);
        verifyNoMoreInteractions(vetRepository);
    }

    @Test
    void testFindActiveVets() {
        List<Vet> activeVets = List.of(
                new Vet(1, "Ana", "Rojas", "ana@example.com", "999111222", true),
                new Vet(3, "Eva", "Mora", "eva@example.com", "999555666", true)
        );
        when(vetRepository.findByActiveTrue()).thenReturn(activeVets);

        List<Vet> result = vetService.findActiveVets();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Vet::isActive));
        verify(vetRepository).findByActiveTrue();
        verifyNoMoreInteractions(vetRepository);
    }
}
