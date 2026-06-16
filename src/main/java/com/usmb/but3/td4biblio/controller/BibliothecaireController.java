package com.usmb.but3.td4biblio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.service.BibliothecaireService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/biblio/bibliothecaire")
@RequiredArgsConstructor
@Validated
public class BibliothecaireController {
    private final BibliothecaireService bibliothecaireService;

    @GetMapping("/")
    public ResponseEntity<List<Bibliothecaire>> getAllBibliothecaires() {
        return ResponseEntity.ok().body(bibliothecaireService.getAllBibliothecaires());
    }

    @GetMapping("/id")
    public ResponseEntity<Bibliothecaire> getBibliothecaireByPseudo(@PathVariable("pseudo") String pseudo) {
        Bibliothecaire bibliothecaire = bibliothecaireService.getBibliothecaireByPseudo(pseudo);
        return bibliothecaire != null ? ResponseEntity.ok().body(bibliothecaire) : ResponseEntity.notFound().build();
    }
    
   @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Bibliothecaire>> getBibliothecaireByNom(@PathVariable("nom") String nom) {
        return ResponseEntity.ok().body(bibliothecaireService.getBibliothecairesByNom(nom));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Bibliothecaire>> getBibliothecairesByNomAndPrenom(
            @RequestParam(name = "nom") String nom, 
            @RequestParam(name = "prenom") String prenom) {
        return ResponseEntity.ok().body(bibliothecaireService.getBibliothecairesByNomAndPrenom(nom, prenom));
    }

    @GetMapping("/searchLike")
    public ResponseEntity<List<Bibliothecaire>> getBibliothecairesByNomLikeAndPrenomLike(
            @RequestParam(name = "nom") String nom, 
            @RequestParam(name = "prenom") String prenom) {
        return ResponseEntity.ok().body(bibliothecaireService.getBibliothecairesByNomLikeAndPrenomLike(nom, prenom));
    }

    @PostMapping("/")
    public ResponseEntity<Bibliothecaire> saveBibliothecaire(@RequestBody Bibliothecaire bibliothecaire) {
        return ResponseEntity.ok().body(bibliothecaireService.saveBibliothecaire(bibliothecaire));
    }

    @PutMapping("/")
    public ResponseEntity<Bibliothecaire> updateBibliothecaire(@RequestBody Bibliothecaire bibliothecaire) {
        return ResponseEntity.ok().body(bibliothecaireService.updateBibliothecaire(bibliothecaire));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBibliothecaireById(@PathVariable String pseudo) {
        bibliothecaireService.deleteBibliothecaireByPseudo(pseudo);
        return ResponseEntity.ok().body("Bibliothécaire deleted successfully");
    }
}
