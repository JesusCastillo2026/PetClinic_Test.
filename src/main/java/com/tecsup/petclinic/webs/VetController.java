package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestion de veterinarios.
 */
@RestController
@RequestMapping("/vets")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    @PostMapping
    public ResponseEntity<VetDTO> create(@RequestBody VetDTO vetDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vetService.create(vetDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VetDTO> update(@PathVariable Integer id, @RequestBody VetDTO vetDTO) {
        try {
            vetDTO.setId(id);
            return ResponseEntity.ok(vetService.update(vetDTO));
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetDTO> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(vetService.findById(id));
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<VetDTO> deactivate(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(vetService.deactivate(id));
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<VetDTO> reactivate(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(vetService.reactivate(id));
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<VetDTO>> findActive() {
        return ResponseEntity.ok(vetService.findActiveVets());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<VetDTO>> findInactive() {
        return ResponseEntity.ok(vetService.findInactiveVets());
    }
}