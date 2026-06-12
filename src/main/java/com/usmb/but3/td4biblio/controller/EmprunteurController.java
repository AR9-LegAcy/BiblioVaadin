package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.service.EmprunteurService;

import java.util.List;


/**
 * Controller for handling Emprunteur-related HTTP requests.
 */
@RestController
@RequestMapping("/biblio/emprunteur")
@RequiredArgsConstructor
@Validated
public class EmprunteurController {
    private final EmprunteurService emprunteurService;

    @GetMapping("/")
    public ResponseEntity<List<Emprunteur>> getAllEmprunteurs() {
        return ResponseEntity.ok().body(emprunteurService.getAllEmprunteurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprunteur> getEmprunteurById(@PathVariable("id") Integer id) {
        Emprunteur emprunteur = emprunteurService.getEmprunteurById(id);
        return emprunteur != null ? ResponseEntity.ok().body(emprunteur) : ResponseEntity.notFound().build();
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Emprunteur>> getEmprunteursByNom(@PathVariable("nom") String nom) {
        return ResponseEntity.ok().body(emprunteurService.getEmprunteursByNom(nom));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Emprunteur>> getEmprunteursByNomAndPrenom(
            @RequestParam(name = "nom") String nom, 
            @RequestParam(name = "prenom") String prenom) {
        return ResponseEntity.ok().body(emprunteurService.getEmprunteursByNomAndPrenom(nom, prenom));
    }

    @GetMapping("/searchLike")
    public ResponseEntity<List<Emprunteur>> getEmprunteursByNomLikeAndPrenomLike(
            @RequestParam(name = "nom") String nom,
            @RequestParam(name = "prenom") String prenom) {
        return ResponseEntity.ok().body(emprunteurService.getEmprunteursByNomLikeAndPrenomLike(nom, prenom));
    }
    
    
    @PostMapping("/")
    public ResponseEntity<Emprunteur> createEmprunteur(@RequestBody Emprunteur emprunteur) {
        Emprunteur savedEmprunteur = emprunteurService.saveEmprunteur(emprunteur);
        return ResponseEntity.ok().body(savedEmprunteur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Emprunteur> updateEmprunteur(@PathVariable("id") Integer id, @RequestBody Emprunteur emprunteur) {
        Emprunteur existingEmprunteur = emprunteurService.getEmprunteurById(id);
        if (existingEmprunteur == null) {
            return ResponseEntity.notFound().build();
        }
        emprunteur.setCarteEmprunteur(id); // Ensure the ID is set for update
        Emprunteur updatedEmprunteur = emprunteurService.updateEmprunteur(emprunteur);
        return ResponseEntity.ok().body(updatedEmprunteur);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmprunteur(@PathVariable("id") Integer id) {
        Emprunteur existingEmprunteur = emprunteurService.getEmprunteurById(id);
        if (existingEmprunteur == null) {
            return ResponseEntity.notFound().build();
        }
        emprunteurService.deleteEmprunteurById(id);
        return ResponseEntity.noContent().build();
    }
}
