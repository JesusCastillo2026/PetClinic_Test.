package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;

    @Override
    @Transactional
    public void deactivateVet(Integer id) {
        Vet vet = findVetOrThrow(id);
        vet.setActive(false);
        vetRepository.save(vet);
    }

    @Override
    @Transactional
    public void reactivateVet(Integer id) {
        Vet vet = findVetOrThrow(id);
        vet.setActive(true);
        vetRepository.save(vet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vet> findActiveVets() {
        return vetRepository.findByActiveTrue();
    }

    private Vet findVetOrThrow(Integer id) {
        return vetRepository.findById(id)
                .orElseThrow(() -> new VetNotFoundException(id));
    }
}
