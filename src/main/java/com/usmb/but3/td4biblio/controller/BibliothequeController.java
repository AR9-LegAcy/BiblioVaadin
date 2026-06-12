package com.usmb.but3.td4biblio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.service.BibliothequeService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for handling Bibliotheque-related HTTP requests.
 */
@RestController
@RequestMapping("/biblio/bibliotheque")
@RequiredArgsConstructor
@Validated
public class BibliothequeController {

    private final BibliothequeService bibliothequeService;

    /**
     * GET all bibliotheques.
     * URL: localhost:8080/biblio/bibliotheque/
     */
    @GetMapping("/")
    public ResponseEntity<List<Bibliotheque>> getAllBibliotheques() {
        return ResponseEntity.ok().body(bibliothequeService.getAllBibliotheques());
    }

    /**
     * GET bibliotheque by id.
     * URL: localhost:8080/biblio/bibliotheque/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bibliotheque> getBibliothequeById(@PathVariable("id") Integer id) {
        Bibliotheque bibliotheque = bibliothequeService.getBibliothequeById(id);
        return bibliotheque != null ? ResponseEntity.ok().body(bibliotheque) : ResponseEntity.notFound().build();
    }

    /**
     * GET bibliotheques by nom.
     * URL: localhost:8080/biblio/bibliotheque/nom/{nom}
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Bibliotheque>> getBibliothequesByNom(@PathVariable("nom") String nom) {
        return ResponseEntity.ok().body(bibliothequeService.getBibliothequesByNom(nom));
    }

    /**
     * GET bibliotheques by nom and prenom as Request Parameters.
     * URL: localhost:8080/biblio/bibliotheque/search?nom={nom}&prenom={prenom}
     */
    /*
    @GetMapping("/search")
    public ResponseEntity<List<Bibliotheque>> getBibliothequesByNomAndAdresseRue(
            @RequestParam(name = "nom") String nom, 
            @RequestParam(name = "prenom") String adresseRue) {
        return ResponseEntity.ok().body(bibliothequeService.getBibliothequesByNomAndAdresseRue(nom, adresseRue));
    }
    */

    /**
     * GET bibliotheques by nom and prenom (--like--) as Request Parameters.
     * URL: localhost:8080/biblio/bibliotheque/searchLike?nom=Hug&prenom=Vict
     */
    /* 
    @GetMapping("/searchLike")
    public ResponseEntity<List<Bibliotheque>> getBibliothequesByNomLikeAndAdresseVille(
            @RequestParam(name = "nom") String nom, 
            @RequestParam(name = "prenom") String adresseVille) {
        return ResponseEntity.ok().body(bibliothequeService.getBibliothequesByNomLikeAndAdresseVille(nom, adresseVille));
    }
    */

    /**
     * POST a new bibliotheque.
     * URL: localhost:8080/biblio/bibliotheque/
     */
    @PostMapping("/")
    public ResponseEntity<Bibliotheque> saveBibliotheque(@RequestBody Bibliotheque bibliotheque) {
        return ResponseEntity.ok().body(bibliothequeService.saveBibliotheque(bibliotheque));
    }

    /**
     * PUT (update) an bibliotheque.
     * URL: localhost:8080/biblio/bibliotheque/
     */
    @PutMapping("/")
    public ResponseEntity<Bibliotheque> updateBibliotheque(@RequestBody Bibliotheque bibliotheque) {
        return ResponseEntity.ok().body(bibliothequeService.updateBibliotheque(bibliotheque));
    }

    /**
     * DELETE an bibliotheque by id.
     * URL: localhost:8080/biblio/bibliotheque/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBibliothequeById(@PathVariable Integer id) {
        bibliothequeService.deleteBibliothequeById(id);
        return ResponseEntity.ok().body("Bibliotheque deleted successfully");
    }
}