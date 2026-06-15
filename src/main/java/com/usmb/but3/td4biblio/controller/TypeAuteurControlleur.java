package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.service.TypeAuteurService;

import java.util.List;

/**
 * Controller for handling TypeAuteur-related HTTP requests.
 */
@RestController
@RequestMapping("/biblio/typeAuteur")
@RequiredArgsConstructor
@Validated
public class TypeAuteurControlleur {

    private final TypeAuteurService typeAuteurService;

    /**
     * GET all typeAuteurs.
     * URL: localhost:8080/biblio/typeAuteur/
     */
    @GetMapping("/")
    public ResponseEntity<List<TypeAuteur>> getAllTypeAuteurs() {
        return ResponseEntity.ok().body(typeAuteurService.getAllTypeAuteurs());
    }

    /**
     * GET typeAuteur by id.
     * URL: localhost:8080/biblio/typeAuteur/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeAuteur> getTypeAuteurById(@PathVariable("id") Integer id) {
        TypeAuteur typeAuteur = typeAuteurService.getTypeAuteurById(id);
        return typeAuteur != null ? ResponseEntity.ok().body(typeAuteur) : ResponseEntity.notFound().build();
    }

    /**
     * GET typeAuteurs by nom.
     * URL: localhost:8080/biblio/typeAuteur/nom/{nom}
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<TypeAuteur>> getTypeAuteursByNom(@PathVariable("nom") String nom) {
        return ResponseEntity.ok().body(typeAuteurService.getTypeAuteursByNom(nom));
    }

    /**
     * GET typeAuteurs by nom (--like--) as Request Parameters.
     * URL: localhost:8080/biblio/typeAuteur/searchLike?nom=Hug
     */
    @GetMapping("/searchLike")
    public ResponseEntity<List<TypeAuteur>> getTypeAuteursByNomLike(@PathVariable("nom") String nom) {
        return ResponseEntity.ok().body(typeAuteurService.getTypeAuteursByNomLike(nom));
    }

    /**
     * POST a new typeAuteur.
     * URL: localhost:8080/biblio/typeAuteur/
     */
    @PostMapping("/")
    public ResponseEntity<TypeAuteur> saveTypeAuteur(@RequestBody TypeAuteur typeAuteur) {
        return ResponseEntity.ok().body(typeAuteurService.saveTypeAuteur(typeAuteur));
    }

    /**
     * PUT (update) an typeAuteur.
     * URL: localhost:8080/biblio/typeAuteur/
     */
    @PutMapping("/")
    public ResponseEntity<TypeAuteur> updateAuteur(@RequestBody TypeAuteur typeAuteur) {
        return ResponseEntity.ok().body(typeAuteurService.updateTypeAuteur(typeAuteur));
    }

    /**
     * DELETE an typeAuteur by id.
     * URL: localhost:8080/biblio/typeAuteur/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTypeAuteurById(@PathVariable Integer id) {
        typeAuteurService.deleteTypeAuteurById(id);
        return ResponseEntity.ok().body("TypeAuteur deleted successfully");
    }
}