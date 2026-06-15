package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.service.EditeurService;

import java.util.List;

/**
 * Controller for handling Editeur-related HTTP requests.
 */
@RestController
@RequestMapping("/biblio/editeur")
@RequiredArgsConstructor
@Validated
public class EditeurController {

    private final EditeurService editeurService;

    /**
     * GET all éditeurs.
     * URL: localhost:8080/biblio/editeur/
     */
    @GetMapping("/")
    public ResponseEntity<List<Editeur>> getAllEditeurs() {
        return ResponseEntity.ok().body(editeurService.getAllEditeurs());
    }

    /**
     * GET éditeur by id.
     * URL: localhost:8080/biblio/editeur/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Editeur> getEditeurById(@PathVariable("id") Integer id) {
        Editeur editeur = editeurService.getEditeurById(id);
        return editeur != null ? ResponseEntity.ok().body(editeur) : ResponseEntity.notFound().build();
    }

    /**
     * GET éditeurs by nom.
     * URL: localhost:8080/biblio/editeur/nom/{nom}
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Editeur>> getEditeursByNom(@PathVariable("nom") String nom) {
        return ResponseEntity.ok().body(editeurService.getEditeursByNom(nom));
    }

    /**
     * POST a new éditeur.
     * URL: localhost:8080/biblio/editeur/
     */
    @PostMapping("/")
    public ResponseEntity<Editeur> saveEditeur(@RequestBody Editeur editeur) {
        return ResponseEntity.ok().body(editeurService.saveEditeur(editeur));
    }

    /**
     * PUT (update) an éditeur.
     * URL: localhost:8080/biblio/editeur/
     */
    @PutMapping("/")
    public ResponseEntity<Editeur> updateEditeur(@RequestBody Editeur editeur) {
        return ResponseEntity.ok().body(editeurService.updateEditeur(editeur));
    }

    /**
     * DELETE an éditeur by id.
     * URL: localhost:8080/biblio/editeur/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEditeurById(@PathVariable Integer id) {
        editeurService.deleteEditeurById(id);
        return ResponseEntity.ok().body("Editeur deleted successfully");
    }
}