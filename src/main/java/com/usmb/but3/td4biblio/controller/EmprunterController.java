package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.service.EmprunterService;

import java.util.List;

@RestController
@RequestMapping("/biblio/emprunter")
@RequiredArgsConstructor
@Validated
public class EmprunterController {

    private final EmprunterService emprunterService;

    /** GET all emprunts */
    @GetMapping("/")
    public ResponseEntity<List<Emprunter>> getAllEmprunts() {
        return ResponseEntity.ok(emprunterService.getAllEmprunts());
    }

    /** GET by composite ID */
    @GetMapping("/{idDocument}/{carteEmprunteur}")
    public ResponseEntity<Emprunter> getEmpruntById(
            @PathVariable Integer idDocument,
            @PathVariable Integer carteEmprunteur) {

        EmprunterId id = new EmprunterId(idDocument, carteEmprunteur);
        Emprunter emprunt = emprunterService.getEmpruntById(id);

        return (emprunt != null)
                ? ResponseEntity.ok(emprunt)
                : ResponseEntity.notFound().build();
    }

    /** CREATE */
    @PostMapping("/")
    public ResponseEntity<Emprunter> createEmprunt(@RequestBody Emprunter emprunt) {
        return ResponseEntity.ok(emprunterService.saveEmprunt(emprunt));
    }

    /** UPDATE */
    @PutMapping("/{idDocument}/{carteEmprunteur}")
    public ResponseEntity<Emprunter> updateEmprunt(
            @PathVariable Integer idDocument,
            @PathVariable Integer carteEmprunteur,
            @RequestBody Emprunter emprunt) {

        EmprunterId id = new EmprunterId(idDocument, carteEmprunteur);

        Emprunter existing = emprunterService.getEmpruntById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // 🔥 IMPORTANT : on force les relations correctes
        Document document = emprunterService.getDocumentById(idDocument);
        Emprunteur emprunteur = emprunterService.getEmprunteurById(carteEmprunteur);

        if (document == null || emprunteur == null) {
            return ResponseEntity.badRequest().build();
        }

        emprunt.setIdDocument(document);
        emprunt.setCarteEmprunteur(emprunteur);

        return ResponseEntity.ok(emprunterService.updateEmprunt(emprunt));
    }

    /** DELETE */
    @DeleteMapping("/{idDocument}/{carteEmprunteur}")
    public ResponseEntity<Void> deleteEmprunt(
            @PathVariable Integer idDocument,
            @PathVariable Integer carteEmprunteur) {

        EmprunterId id = new EmprunterId(idDocument, carteEmprunteur);

        if (emprunterService.getEmpruntById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        emprunterService.deleteEmpruntById(id);
        return ResponseEntity.noContent().build();
    }
}