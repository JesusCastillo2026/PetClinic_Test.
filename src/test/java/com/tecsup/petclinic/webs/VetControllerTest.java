package com.tecsup.petclinic.webs;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integracion de VetController (sin mocks):
 * Controller -> Service -> Repository -> H2 (datos de data.sql).
 *
 * @author Daniel Millones
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ===== Integrante A — Listado y consulta =====

    /**
     * GET /vets -> 200 OK y lista JSON con los vets de data.sql.
     *
     * @author Daniel Millones
     */
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

    /**
     * GET /vets/1 -> 200 OK y lastName = "Carter".
     *
     * @author Daniel Millones
     */
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

    /**
     * GET /vets/666 -> 404 Not Found.
     *
     * @author Daniel Millones
     */
    @Test
    @DisplayName("Buscar veterinario inexistente")
    public void testFindVetKO() throws Exception {

        MvcResult result = this.mockMvc.perform(get("/vets/666"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        log.info("Vet 666 no encontrado (H2), status: {}", result.getResponse().getStatus());
    }
}