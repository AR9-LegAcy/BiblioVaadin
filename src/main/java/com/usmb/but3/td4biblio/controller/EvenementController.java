package com.usmb.but3.td4biblio.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.service.EvenementService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for handling Evenement-related HTTP requests.
 */
@RestController
@RequestMapping("/biblio/evenement")
@RequiredArgsConstructor
@Validated
public class EvenementController {

    private final EvenementService evenementService;

    /**
     * GET all evenements.
     * URL: localhost:8080/biblio/evenement/
     */
    @GetMapping("/")
    public ResponseEntity<List<Evenement>> getAllEvenements() {
        return ResponseEntity.ok().body(evenementService.getAllEvenements());
    }

    /**
     * GET evenement by id.
     * URL: localhost:8080/biblio/evenement/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evenement> getEvenementById(@PathVariable("id") Integer id) {
        Evenement evenement = evenementService.getEvenementById(id);
        return evenement != null ? ResponseEntity.ok().body(evenement) : ResponseEntity.notFound().build();
    }

    /**
     * GET evenements by titre.
     * URL: localhost:8080/biblio/evenement/titre/{titre}
     */
    @GetMapping("/titre/{titre}")
    public ResponseEntity<List<Evenement>> getEvenementsByTitre(@PathVariable("titre") String titre) {
        return ResponseEntity.ok().body(evenementService.getEvenementsByTitre(titre));
    }

    /**
     * GET evenements by titre and dateDebut as Request Parameters.
     * URL: localhost:8080/biblio/evenement/search?titre={titre}&dateDebut={dateDebut}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Evenement>> getEvenementsByTitreLikeAndDateDebut(
            @RequestParam(name = "titre") String titre, 
            @RequestParam(name = "dateDebut") LocalDate dateDebut) {
        return ResponseEntity.ok().body(evenementService.getEvenementsByTitreLikeAndDateDebut(titre, dateDebut));
    }

    /**
     * GET evenements by titre and dateFin (--like--) as Request Parameters.
     * URL: localhost:8080/biblio/evenement/searchLike?titre=Hug&dateFin=10/10/2025
     */
    @GetMapping("/searchLike")
    public ResponseEntity<List<Evenement>> getEvenementsByTitreLikeAndDateFin(
            @RequestParam(name = "titre") String titre, 
            @RequestParam(name = "dateFin") LocalDate dateFin) {
        return ResponseEntity.ok().body(evenementService.getEvenementsByTitreLikeAndDateFin(titre, dateFin));
    }

    /**
     * POST a new evenement.
     * URL: localhost:8080/biblio/evenement/
     */
    @PostMapping("/")
    public ResponseEntity<Evenement> saveEvenement(@RequestBody Evenement evenement) {
        return ResponseEntity.ok().body(evenementService.saveEvenement(evenement));
    }

    /**
     * PUT (update) an evenement.
     * URL: localhost:8080/biblio/evenement/
     */
    @PutMapping("/")
    public ResponseEntity<Evenement> updateEvenement(@RequestBody Evenement evenement) {
        return ResponseEntity.ok().body(evenementService.updateEvenement(evenement));
    }

    /**
     * DELETE an evenement by id.
     * URL: localhost:8080/biblio/evenement/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvenementById(@PathVariable Integer id) {
        evenementService.deleteEvenementById(id);
        return ResponseEntity.ok().body("Evenement deleted successfully");
    }
}