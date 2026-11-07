package com.tecsup.petclinic.webs;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de VetController con el servicio mockeado:
 * solo se prueba la logica del controlador y el contrato JSON.
 *
 * @author Daniel Millones
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerMockitoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VetService vetService;   // servicio mockeado

    // ===== Integrante A — Listado y consulta =====

    /**
     * GET /vets -> 200 OK y la lista que devuelve el servicio mockeado.
     *
     * @author Daniel Millones
     */
    @Test
    @DisplayName("Listar todos los veterinarios (mock)")
    public void testFindAllVets() throws Exception {

        List<VetDTO> vetsTO = TObjectCreator.getAllVetsDTO();

        Mockito.when(vetService.findAll())
                .thenReturn(vetsTO);

        this.mockMvc.perform(get("/vets"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(vetsTO.size())))
                .andExpect(jsonPath("$[0].id", is(vetsTO.get(0).getId())))
                .andExpect(jsonPath("$[0].lastName", is(vetsTO.get(0).getLastName())));

        Mockito.verify(vetService).findAll();
    }

    /**
     * GET /vets/1 -> 200 OK con los datos del VetDTO de TObjectCreator.
     *
     * @author Daniel Millones
     */
    @Test
    @DisplayName("Buscar veterinario existente (mock)")
    public void testFindVetOK() throws Exception {

        VetDTO vetTO = TObjectCreator.getVetDTO();

        Mockito.when(vetService.findById(vetTO.getId()))
                .thenReturn(vetTO);

        this.mockMvc.perform(get("/vets/" + vetTO.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(vetTO.getId())))
                .andExpect(jsonPath("$.firstName", is(vetTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(vetTO.getLastName())));

        Mockito.verify(vetService).findById(vetTO.getId());
    }

    /**
     * GET /vets/666 -> 404 cuando el servicio lanza VetNotFoundException.
     *
     * @author Daniel Millones
     */
    @Test
    @DisplayName("Buscar veterinario inexistente (mock)")
    public void testFindVetKO() throws Exception {

        Integer ID_NOT_EXIST = 666;

        Mockito.when(vetService.findById(ID_NOT_EXIST))
                .thenThrow(new VetNotFoundException("Record not found...!"));

        this.mockMvc.perform(get("/vets/" + ID_NOT_EXIST))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}