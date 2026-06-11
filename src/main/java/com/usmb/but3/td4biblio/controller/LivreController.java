package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.LivreService;

import java.util.List;

/**
 * La classe Controller où sont traitées  toutes les requests de l'utilisateur et où les
 * réponses appropriées sont renvoyées.
 * Elle interagit avec la couche Service pour accéder aux données.
 */
@RestController
@RequestMapping("/biblio/livre")
@RequiredArgsConstructor
@Validated
public class LivreController {

    private final LivreService livreService;

    /**
     * GET all livres
     * URL: localhost:8080/biblio/livre/
     * @return List of all Livres
     */
    @GetMapping("/")
    public ResponseEntity<List<Livre>> getAllLivres(){
        return ResponseEntity.ok().body(livreService.getAllLivres());
    }

    /**
     * GET livre by id
     * URL: localhost:8080/biblio/livre/{id}
     * @param id - livre id (idDocument)
     * @return Livre with the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Livre> getLivreById(@PathVariable("id") Integer id) {
        Livre livre = livreService.getLivreById(id);
        if (livre == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(livre);
    }

    /**
     * POST a new livre
     * URL: localhost:8080/biblio/livre/
     * @param livre - Request body is a Livre entity
     * @return Saved Livre entity
     */
    @PostMapping("/")
    public ResponseEntity<Livre> saveLivre(@RequestBody Livre livre) {
        return ResponseEntity.ok().body(livreService.saveLivre(livre));
    }

    /**
     * PUT (update) a livre
     * URL: localhost:8080/biblio/livre/
     * @param livre - Livre entity to be updated
     * @return Updated Livre
     */
    @PutMapping("/")
    public ResponseEntity<Livre> updateLivre(@RequestBody Livre livre) {
        return ResponseEntity.ok().body(livreService.updateLivre(livre));
    }

    /**
     * DELETE a livre by id
     * URL: localhost:8080/biblio/livre/{id}
     * @param id - livre's id to be deleted
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLivreById(@PathVariable("id") Integer id) {
        livreService.deleteLivreById(id);
        return ResponseEntity.ok().body("Livre deleted successfully");
    }

    /**
     * GET livres by editeur id
     * URL: localhost:8080/biblio/livre/editeur/{idEditeur}
     */
    @GetMapping("/editeur/{idEditeur}")
    public ResponseEntity<List<Livre>> getLivresByIdEditeur(@PathVariable("idEditeur") Integer idEditeur) {
        return ResponseEntity.ok().body(livreService.getByIdEditeur(idEditeur));
    }

    /**
     * GET livres by type document id
     * URL: localhost:8080/biblio/livre/type/{idTypeDocument}
     */
    @GetMapping("/type/{idTypeDocument}")
    public ResponseEntity<List<Livre>> getLivresByIdTypeDocument(@PathVariable("idTypeDocument") Integer idTypeDocument) {
        return ResponseEntity.ok().body(livreService.getByIdTypeDocument(idTypeDocument));
    }

    /**
     * GET livres by titre (like) as Request Parameter
     * URL: localhost:8080/biblio/livre/search?titre=miséra
     */
    @GetMapping("/search")
    public ResponseEntity<List<Livre>> getLivresByTitreContaining(@RequestParam(name = "titre") String titre) {
        return ResponseEntity.ok().body(livreService.getByTitreContainingIgnoreCase(titre));
    }
}