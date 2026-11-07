package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetService;
import com.tecsup.petclinic.util.TObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de VetController con servicio mockeado
 *
 * @author Daniel Millones / Jesus Castillo / Luis Antonio Vasquez
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerMockitoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VetService vetService;

    private static final ObjectMapper om = new ObjectMapper();

    // ===== Integrante A — Listado y consulta =====

    @Test
    @DisplayName("Listar todos los veterinarios (mock)")
    public void testFindAllVets() throws Exception {
        List<VetDTO> vetsTO = TObjectCreator.getAllVetsDTO();

        Mockito.when(vetService.findAll()).thenReturn(vetsTO);

        MvcResult result = this.mockMvc.perform(get("/vets"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(vetsTO.size())))
                .andExpect(jsonPath("$[0].id", is(vetsTO.get(0).getId())))
                .andExpect(jsonPath("$[0].lastName", is(vetsTO.get(0).getLastName())))
                .andReturn();

        log.info("Vets listados (mock): {}", result.getResponse().getContentAsString());
        Mockito.verify(vetService).findAll();
    }

    @Test
    @DisplayName("Buscar veterinario existente (mock)")
    public void testFindVetOK() throws Exception {
        VetDTO vetTO = TObjectCreator.getVetDTO();

        Mockito.when(vetService.findById(vetTO.getId())).thenReturn(vetTO);

        MvcResult result = this.mockMvc.perform(get("/vets/" + vetTO.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(vetTO.getId())))
                .andExpect(jsonPath("$.firstName", is(vetTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(vetTO.getLastName())))
                .andReturn();

        log.info("Vet encontrado (mock): {}", result.getResponse().getContentAsString());
        Mockito.verify(vetService).findById(vetTO.getId());
    }

    @Test
    @DisplayName("Buscar veterinario inexistente (mock)")
    public void testFindVetKO() throws Exception {
        Integer ID_NOT_EXIST = 666;

        Mockito.when(vetService.findById(ID_NOT_EXIST))
                .thenThrow(new VetNotFoundException("Record not found...!"));

        MvcResult result = this.mockMvc.perform(get("/vets/" + ID_NOT_EXIST))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        log.info("Vet 666 no encontrado (mock), status: {}", result.getResponse().getStatus());
    }

    // ===== Integrante B — Creacion y soft delete =====

    @Test
    @DisplayName("Crear veterinario (mock)")
    public void testCreateVet() throws Exception {

        VetDTO newVet = TObjectCreator.newVetDTO();
        VetDTO createdVet = TObjectCreator.newVetDTOCreated();
        log.info("Vet esperado (mock): {}", createdVet);

        Mockito.when(vetService.create(any(VetDTO.class)))
                .thenReturn(createdVet);

        MvcResult result = this.mockMvc.perform(post("/vets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newVet)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(createdVet.getId())))
                .andExpect(jsonPath("$.lastName", is(createdVet.getLastName())))
                .andExpect(jsonPath("$.active", is(true)))
                .andReturn();

        log.info("Vet creado (mock): {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Desactivar veterinario (mock)")
    public void testDeactivateVet() throws Exception {

        VetDTO deactivated = TObjectCreator.getVetDTODeactivated();
        log.info("Vet esperado (mock): {}", deactivated);

        Mockito.when(vetService.deactivate(deactivated.getId()))
                .thenReturn(deactivated);

        MvcResult result = this.mockMvc.perform(put("/vets/" + deactivated.getId() + "/deactivate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active", is(false)))
                .andReturn();

        log.info("Vet desactivado (mock): {}", result.getResponse().getContentAsString());
        Mockito.verify(vetService).deactivate(deactivated.getId());
    }

    @Test
    @DisplayName("Reactivar veterinario 6 (mock)")
    public void testReactivateVet() throws Exception {

        VetDTO reactivated = TObjectCreator.getVetDTOReactivated();
        log.info("Vet esperado (mock): {}", reactivated);

        Mockito.when(vetService.reactivate(6))
                .thenReturn(reactivated);

        MvcResult result = this.mockMvc.perform(put("/vets/6/reactivate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.active", is(true)))
                .andReturn();

        log.info("Vet reactivado (mock): {}", result.getResponse().getContentAsString());
        Mockito.verify(vetService).reactivate(6);
    }

    // ===== Integrante C — Filtros y borrado =====

    @Test
    @DisplayName("Listar veterinarios activos (mock)")
    public void testFindActiveVets() throws Exception {

        List<VetDTO> activeVets = TObjectCreator.getActiveVetsDTO();
        log.info("Vets activos esperados (mock): {}", activeVets.size());

        Mockito.when(vetService.findActiveVets())
                .thenReturn(activeVets);

        MvcResult result = this.mockMvc.perform(get("/vets").param("active", "true"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(activeVets.size())))
                .andReturn();

        log.info("Vets activos (mock): {}", result.getResponse().getContentAsString());
        Mockito.verify(vetService).findActiveVets();
    }

    @Test
    @DisplayName("Listar veterinarios inactivos (mock)")
    public void testFindInactiveVets() throws Exception {

        List<VetDTO> inactiveVets = TObjectCreator.getInactiveVetsDTO();
        log.info("Vets inactivos esperados (mock): {}", inactiveVets.size());

        Mockito.when(vetService.findInactiveVets())
                .thenReturn(inactiveVets);

        MvcResult result = this.mockMvc.perform(get("/vets").param("active", "false"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(inactiveVets.size())))
                .andExpect(jsonPath("$[0].active", is(false)))
                .andReturn();

        log.info("Vets inactivos (mock): {}", result.getResponse().getContentAsString());
        Mockito.verify(vetService).findInactiveVets();
    }

    @Test
    @DisplayName("Eliminar veterinario inexistente (mock)")
    public void testDeleteVetKO() throws Exception {

        Integer ID_NOT_EXIST = 1000;
        log.info("Mock lanza VetNotFoundException al eliminar id {}", ID_NOT_EXIST);

        Mockito.doThrow(new VetNotFoundException("Record not found...!"))
                .when(vetService).delete(ID_NOT_EXIST);

        MvcResult result = this.mockMvc.perform(delete("/vets/" + ID_NOT_EXIST))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        log.info("Vet {} no eliminado (mock), status: {}", ID_NOT_EXIST, result.getResponse().getStatus());
    }
}