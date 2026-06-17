package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.LivreService;
import java.util.List;

@RestController
@RequestMapping("/biblio/livre")
@RequiredArgsConstructor
@Validated
public class LivreController {

    private final LivreService livreService;

    @GetMapping("/")
    public ResponseEntity<List<Livre>> getAllLivres() {
        return ResponseEntity.ok(livreService.getAllLivres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livre> getLivreById(@PathVariable Integer id) {
        Livre livre = livreService.getLivreById(id);
        return ResponseEntity.ok(livre);
    }

    @PostMapping("/")
    public ResponseEntity<Livre> saveLivre(@RequestBody Livre livre,
            @RequestParam Integer idBibliotheque) {

        Bibliotheque bib = new Bibliotheque();
        bib.setId(idBibliotheque);

        return ResponseEntity.ok(
                livreService.saveLivre(livre, bib));
    }

    @PutMapping("/")
    public ResponseEntity<Livre> updateLivre(@RequestBody Livre livre) {
        return ResponseEntity.ok(livreService.updateLivre(livre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLivreById(@PathVariable Integer id) {
        livreService.deleteLivreById(id);
        return ResponseEntity.ok("Livre deleted successfully");
    }
}