package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.service.EcrireService;

import java.util.List;

@RestController
@RequestMapping("/biblio/ecrire")
@RequiredArgsConstructor
@Validated
public class EcrireController {

    private final EcrireService ecrireService;

    /* GET all ecrires */
    @GetMapping("/")
    public ResponseEntity<List<Ecrire>> getAllEcrires() {
        return ResponseEntity.ok(ecrireService.getAllEcrires());
    }

    /* GET by composite ID */
    @GetMapping("/{idAuteur}/{idDocument}")
    public ResponseEntity<Ecrire> getEcrireById(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idDocument) {

        EcrireId id = new EcrireId(idAuteur, idDocument);
        Ecrire ecrire = ecrireService.getEcrireById(id);

        return (ecrire != null)
                ? ResponseEntity.ok(ecrire)
                : ResponseEntity.notFound().build();
    }

    /* CREATE */
    @PostMapping("/")
    public ResponseEntity<Ecrire> createEcrire(@RequestBody Ecrire ecrire) {
        return ResponseEntity.ok(ecrireService.saveEcrire(ecrire));
    }

    /* UPDATE */
    @PutMapping("/{idAuteur}/{idDocument}")
    public ResponseEntity<Ecrire> updateEcrire(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idDocument,
            @RequestBody Ecrire ecrire) {

        EcrireId id = new EcrireId(idAuteur, idDocument);

        Ecrire existing = ecrireService.getEcrireById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        Auteur auteur = ecrireService.getAuteurById(idAuteur);
        Document document = ecrireService.getDocumentById(idDocument);

        if (auteur == null || document == null) {
            return ResponseEntity.badRequest().build();
        }

        ecrire.setIdAuteur(auteur);
        ecrire.setIdDocument(document);

        return ResponseEntity.ok(ecrireService.updateEcrire(ecrire));
    }

    /* DELETE */
    @DeleteMapping("/{idAuteur}/{idDocument}")
    public ResponseEntity<Void> deleteEcrire(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idDocument) {

        EcrireId id = new EcrireId(idAuteur, idDocument);

        if (ecrireService.getEcrireById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        ecrireService.deleteEcrireById(id);
        return ResponseEntity.noContent().build();
    }
}