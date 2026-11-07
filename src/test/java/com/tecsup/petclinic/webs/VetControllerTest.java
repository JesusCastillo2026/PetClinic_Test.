package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.util.TObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integracion de VetController (sin mocks)
 *
 * @author Daniel Millones / Jesus Castillo / Luis Antonio Vasquez
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper om = new ObjectMapper();

    // ===== Integrante A — Listado y consulta =====

    @Test
    @DisplayName("Listar todos los veterinarios")
    public void testFindAllVets() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/vets"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(6))))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].lastName", is("Carter")))
                .andReturn();

        log.info("Vets listados (H2): {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Buscar veterinario existente")
    public void testFindVetOK() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/vets/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("James")))
                .andExpect(jsonPath("$.lastName", is("Carter")))
                .andReturn();

        log.info("Vet encontrado (H2): {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Buscar veterinario inexistente")
    public void testFindVetKO() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/vets/666"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        log.info("Vet 666 no encontrado (H2), status: {}", result.getResponse().getStatus());
    }

    // ===== Integrante B — Creacion y soft delete =====

    /**
     * POST /vets -> 201 Created.
     * Transactional revierte el insert para no alterar los conteos de C.
     *
     * @author Jesus Castillo
     */
    @Test
    @Transactional
    @DisplayName("Crear veterinario")
    public void testCreateVet() throws Exception {

        VetDTO newVet = TObjectCreator.newVetDTO();

        MvcResult result = this.mockMvc.perform(post("/vets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newVet)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(newVet.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(newVet.getLastName())))
                .andExpect(jsonPath("$.active", is(true)))
                .andReturn();

        log.info("Vet creado (H2): {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Desactivar veterinario")
    public void testDeactivateVet() throws Exception {
        MvcResult result = this.mockMvc.perform(put("/vets/1/deactivate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.active", is(false)))
                .andReturn();

        log.info("Vet desactivado (H2): {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Reactivar veterinario")
    public void testReactivateVet() throws Exception {
        MvcResult result = this.mockMvc.perform(put("/vets/6/reactivate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.active", is(true)))
                .andReturn();

        log.info("Vet reactivado (H2): {}", result.getResponse().getContentAsString());
    }

    // ===== Integrante C — Filtros y borrado =====

    /**
     * GET /vets?active=true -> solo activos (5).
     *
     * @author Luis Antonio Vasquez
     */
    @Test
    @DisplayName("Listar veterinarios activos")
    public void testFindActiveVets() throws Exception {

        MvcResult result = this.mockMvc.perform(get("/vets").param("active", "true"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].active", everyItem(is(true))))
                .andReturn();

        log.info("Vets activos (H2): {}", result.getResponse().getContentAsString());
    }

    /**
     * GET /vets?active=false -> solo inactivos (1).
     *
     * @author Luis Antonio Vasquez
     */
    @Test
    @DisplayName("Listar veterinarios inactivos")
    public void testFindInactiveVets() throws Exception {

        MvcResult result = this.mockMvc.perform(get("/vets").param("active", "false"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(6)))
                .andExpect(jsonPath("$[0].active", is(false)))
                .andReturn();

        log.info("Vets inactivos (H2): {}", result.getResponse().getContentAsString());
    }

    /**
     * DELETE /vets/1000 -> 404 Not Found.
     *
     * @author Luis Antonio Vasquez
     */
    @Test
    @DisplayName("Eliminar veterinario inexistente")
    public void testDeleteVetKO() throws Exception {

        MvcResult result = this.mockMvc.perform(delete("/vets/1000"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        log.info("Vet 1000 no eliminado (H2), status: {}", result.getResponse().getStatus());
    }
}