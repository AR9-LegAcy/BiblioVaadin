package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.service.TypeEvenementService;

import java.util.List;

/**
 * Controller for handling TypeEvenement-related HTTP requests.
 */
@RestController
@RequestMapping("/biblio/typeEvenement")
@RequiredArgsConstructor
@Validated
public class TypeEvenementController {

    private final TypeEvenementService typeEvenementService;

    /**
     * GET all typeEvenements.
     * URL: localhost:8080/biblio/typeEvenement/
     */
    @GetMapping("/")
    public ResponseEntity<List<TypeEvenement>> getAllTypeEvenements() {
        return ResponseEntity.ok().body(typeEvenementService.getAllTypeEvenements());
    }

    /**
     * GET typeEvenement by id.
     * URL: localhost:8080/biblio/typeEvenement/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeEvenement> getTypeEvenementById(@PathVariable("id") Integer id) {
        TypeEvenement typeEvenement = typeEvenementService.getTypeEvenementById(id);
        return typeEvenement != null ? ResponseEntity.ok().body(typeEvenement) : ResponseEntity.notFound().build();
    }

    /**
     * GET typeEvenements by nom.
     * URL: localhost:8080/biblio/typeEvenement/nom/{nom}
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<TypeEvenement>> getTypeEvenementsByNom(@PathVariable("nom") String nom) {
        return ResponseEntity.ok().body(typeEvenementService.getTypeEvenementsByNom(nom));
    }

    /**
     * GET typeEvenements by nom and prenom as Request Parameters.
     * URL: localhost:8080/biblio/typeEvenement/search?nom={nom}
     */
    @GetMapping("/search")
    public ResponseEntity<List<TypeEvenement>> getTypeEvenementsByNomS(@RequestParam(name = "nom") String nom) {
        return ResponseEntity.ok().body(typeEvenementService.getTypeEvenementsByNom(nom));
    }
    

    /**
     * GET typeEvenements by nom and prenom (--like--) as Request Parameters.
     * URL: localhost:8080/biblio/typeEvenement/searchLike?nom=Hug
     */
    @GetMapping("/searchLike")
    public ResponseEntity<List<TypeEvenement>> getTypeEvenementsByNomLike(
            @RequestParam(name = "nom") String nom) {
        return ResponseEntity.ok().body(typeEvenementService.getTypeEvenementsByNomLike(nom));
    }

    /**
     * POST a new TypeEvenement.
     * URL: localhost:8080/biblio/TypeEvenement/
     */
    @PostMapping("/")
    public ResponseEntity<TypeEvenement> saveTypeEvenement(@RequestBody TypeEvenement TypeEvenement) {
        return ResponseEntity.ok().body(typeEvenementService.saveTypeEvenement(TypeEvenement));
    }

    /**
     * PUT (update) an TypeEvenement.
     * URL: localhost:8080/biblio/TypeEvenement/
     */
    @PutMapping("/")
    public ResponseEntity<TypeEvenement> updateTypeEvenement(@RequestBody TypeEvenement TypeEvenement) {
        return ResponseEntity.ok().body(typeEvenementService.updateTypeEvenement(TypeEvenement));
    }

    /**
     * DELETE an TypeEvenement by id.
     * URL: localhost:8080/biblio/TypeEvenement/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTypeEvenementById(@PathVariable Integer id) {
        typeEvenementService.deleteTypeEvenementById(id);
        return ResponseEntity.ok().body("TypeEvenement deleted successfully");
    }
}