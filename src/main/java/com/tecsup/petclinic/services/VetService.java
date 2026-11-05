package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

import java.util.List;

/**
 * Servicio para la gestion de veterinarios.
 */
public interface VetService {

    /**
     * Crea un nuevo veterinario (activo por defecto).
     *
     * @param vetDTO datos del veterinario a crear
     * @return veterinario creado con su id
     */
    VetDTO create(VetDTO vetDTO);

    /**
     * Actualiza los datos de un veterinario existente.
     *
     * @param vetDTO datos actualizados (debe incluir el id)
     * @return veterinario actualizado
     * @throws VetNotFoundException si no existe el veterinario
     */
    VetDTO update(VetDTO vetDTO) throws VetNotFoundException;

    /**
     * Busca un veterinario por su id.
     *
     * @param id identificador del veterinario
     * @return veterinario encontrado
     * @throws VetNotFoundException si no existe el veterinario
     */
    VetDTO findById(Integer id) throws VetNotFoundException;

    /**
     * Desactiva un veterinario (borrado logico, no elimina el registro).
     *
     * @param id identificador del veterinario
     * @return veterinario con active = false
     * @throws VetNotFoundException si no existe el veterinario
     */
    VetDTO deactivate(Integer id) throws VetNotFoundException;

    /**
     * Reactiva un veterinario desactivado.
     *
     * @param id identificador del veterinario
     * @return veterinario con active = true
     * @throws VetNotFoundException si no existe el veterinario
     */
    VetDTO reactivate(Integer id) throws VetNotFoundException;

    /**
     * Lista los veterinarios activos.
     *
     * @return lista de veterinarios con active = true
     */
    List<VetDTO> findActiveVets();

    /**
     * Lista los veterinarios inactivos.
     *
     * @return lista de veterinarios con active = false
     */
    List<VetDTO> findInactiveVets();
}