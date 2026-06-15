package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.service.ClasserService;

import java.util.List;

@RestController
@RequestMapping("/biblio/classer")
@RequiredArgsConstructor
@Validated
public class ClasserController {

    private final ClasserService classerService;

    /* GET all classements */
    @GetMapping("/")
    public ResponseEntity<List<Classer>> getAllClassements() {
        return ResponseEntity.ok(classerService.getAllClassements());
    }

    /* GET by composite ID */
    @GetMapping("/{idAuteur}/{idTypeAuteur}")
    public ResponseEntity<Classer> getClassementById(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idTypeAuteur) {

        ClasserId id = new ClasserId(idAuteur, idTypeAuteur);
        Classer classement = classerService.getClassementById(id);

        return (classement != null)
                ? ResponseEntity.ok(classement)
                : ResponseEntity.notFound().build();
    }

    /* CREATE */
    @PostMapping("/")
    public ResponseEntity<Classer> createClassement(@RequestBody Classer classer) {
        return ResponseEntity.ok(classerService.saveClassement(classer));
    }

    /* UPDATE */
    @PutMapping("/{idAuteur}/{idTypeAuteur}")
    public ResponseEntity<Classer> updateClassement(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idTypeAuteur,
            @RequestBody Classer classer) {

        ClasserId id = new ClasserId(idAuteur, idTypeAuteur);

        Classer existing = classerService.getClassementById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        Auteur auteur = classerService.getAuteurById(idAuteur);
        TypeAuteur typeAuteur = classerService.getTypeAuteurById(idTypeAuteur);

        if (auteur == null || typeAuteur == null) {
            return ResponseEntity.badRequest().build();
        }

        classer.setIdAuteur(auteur);
        classer.setIdTypeAuteur(typeAuteur);

        return ResponseEntity.ok(classerService.updateClassement(classer));
    }

    /* DELETE */
    @DeleteMapping("/{idAuteur}/{idTypeAuteur}")
    public ResponseEntity<Void> deleteClassement(
            @PathVariable Integer idAuteur,
            @PathVariable Integer idTypeAuteur) {

        ClasserId id = new ClasserId(idAuteur, idTypeAuteur);

        if (classerService.getClassementById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        classerService.deleteClassementById(id);
        return ResponseEntity.noContent().build();
    }
}