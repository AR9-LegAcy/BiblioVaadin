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
    @GetMapping("/{idAuteur}/{idLivre}")
    public ResponseEntity<Ecrire> getEcrireById(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idLivre) {

        EcrireId id = new EcrireId(idAuteur, idLivre);
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
    @PutMapping("/{idAuteur}/{idLivre}")
    public ResponseEntity<Ecrire> updateEcrire(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idLivre,
            @RequestBody Ecrire ecrire) {

        EcrireId id = new EcrireId(idAuteur, idLivre);

        Ecrire existing = ecrireService.getEcrireById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        Auteur auteur = ecrireService.getAuteurById(idAuteur);
        Livre livre = ecrireService.getLivreById(idLivre);

        if (auteur == null || livre == null) {
            return ResponseEntity.badRequest().build();
        }

        ecrire.setIdAuteur(auteur);
        ecrire.setIdLivre(livre);

        return ResponseEntity.ok(ecrireService.updateEcrire(ecrire));
    }

    /* DELETE */
    @DeleteMapping("/{idAuteur}/{idLivre}")
    public ResponseEntity<Void> deleteEcrire(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idLivre) {

        EcrireId id = new EcrireId(idAuteur, idLivre);

        if (ecrireService.getEcrireById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        ecrireService.deleteEcrireById(id);
        return ResponseEntity.noContent().build();
    }
}