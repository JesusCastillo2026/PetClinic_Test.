package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion del servicio de veterinarios.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;

    @Override
    public VetDTO create(VetDTO vetDTO) {
        Vet vet = vetMapper.mapToEntity(vetDTO);
        vet.setActive(true);
        Vet newVet = vetRepository.save(vet);
        log.info("Vet creado: {}", newVet);
        return vetMapper.mapToDto(newVet);
    }

    @Override
    public VetDTO update(VetDTO vetDTO) throws VetNotFoundException {
        Vet existing = getVet(vetDTO.getId());
        existing.setFirstName(vetDTO.getFirstName());
        existing.setLastName(vetDTO.getLastName());
        existing.setEmail(vetDTO.getEmail());
        existing.setPhone(vetDTO.getPhone());
        Vet updatedVet = vetRepository.save(existing);
        log.info("Vet actualizado: {}", updatedVet);
        return vetMapper.mapToDto(updatedVet);
    }

    @Override
    @Transactional(readOnly = true)
    public VetDTO findById(Integer id) throws VetNotFoundException {
        Vet vet = getVet(id);
        log.info("Vet encontrado: {}", vet);
        return vetMapper.mapToDto(vet);
    }

    @Override
    public VetDTO deactivate(Integer id) throws VetNotFoundException {
        Vet vet = getVet(id);
        vet.setActive(false);
        log.info("Vet desactivado: {}", id);
        return vetMapper.mapToDto(vetRepository.save(vet));
    }

    @Override
    public VetDTO reactivate(Integer id) throws VetNotFoundException {
        Vet vet = getVet(id);
        vet.setActive(true);
        log.info("Vet reactivado: {}", id);
        return vetMapper.mapToDto(vetRepository.save(vet));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VetDTO> findActiveVets() {
        List<Vet> vets = vetRepository.findByActiveTrue();
        vets.forEach(vet -> log.info("{}", vet));
        return vetMapper.mapToDtoList(vets);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VetDTO> findInactiveVets() {
        List<Vet> vets = vetRepository.findByActiveFalse();
        vets.forEach(vet -> log.info("{}", vet));
        return vetMapper.mapToDtoList(vets);
    }

    /**
     * Busca la entidad por id o lanza excepcion si no existe.
     *
     * @param id identificador del veterinario
     * @return entidad Vet encontrada
     * @throws VetNotFoundException si no existe el veterinario
     */
    private Vet getVet(Integer id) throws VetNotFoundException {
        return vetRepository.findById(id)
                .orElseThrow(() -> new VetNotFoundException("Record not found...!"));
    }
}