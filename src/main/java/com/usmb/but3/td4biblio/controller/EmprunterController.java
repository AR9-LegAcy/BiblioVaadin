package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.service.EmprunterService;

import java.util.List;

/**
 * Controller for handling Emprunter-related HTTP requests.
 */
@RestController
@RequestMapping("/biblio/emprunter")
@RequiredArgsConstructor
@Validated
public class EmprunterController {
    private final EmprunterService emprunterService;

    @GetMapping("/")
    public ResponseEntity<List<Emprunter>> getAllEmprunts() {
        return ResponseEntity.ok().body(emprunterService.getAllEmprunts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprunter> getEmpruntById(@PathVariable("id") Integer id) {
        Emprunter emprunt = emprunterService.getEmpruntById(id);
        return emprunt != null ? ResponseEntity.ok().body(emprunt) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<Emprunter> createEmprunt(@RequestBody Emprunter emprunt) {
        Emprunter savedEmprunt = emprunterService.saveEmprunt(emprunt);
        return ResponseEntity.ok().body(savedEmprunt);
    }   

    @PutMapping("/{id}")
    public ResponseEntity<Emprunter> updateEmprunt(@PathVariable("id") Integer id, @RequestBody Emprunter emprunt) {
        Emprunter existingEmprunt = emprunterService.getEmpruntById(id);
        if (existingEmprunt == null) {
            return ResponseEntity.notFound().build();
        }
        emprunt.setIdDocument(id); // Ensure the ID is set for update
        Emprunter updatedEmprunt = emprunterService.updateEmprunt(emprunt);
        return ResponseEntity.ok().body(updatedEmprunt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmprunt(@PathVariable("id") Integer id) {
        Emprunter existingEmprunt = emprunterService.getEmpruntById(id);
        if (existingEmprunt == null) {
            return ResponseEntity.notFound().build();
        }
        emprunterService.deleteEmpruntById(id);
        return ResponseEntity.noContent().build();
    }
}
