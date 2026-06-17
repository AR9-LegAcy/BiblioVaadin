package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.LivreRepo;
import com.usmb.but3.td4biblio.repository.StockerRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivreService {

    private final LivreRepo livreRepo;
    private final DocumentRepo documentRepo;
    private final StockerRepo stockerRepo;
    private final IsbnGeneratorService isbnGeneratorService;

    public List<Livre> getAllLivres() {
        return livreRepo.findAll(Sort.by(Sort.Direction.ASC, "titreLivre"));
    }

    public Livre getLivreById(Integer id) {
        return livreRepo.findById(id).orElse(null);
    }

    public Livre saveLivre(Livre livre, Bibliotheque bibliotheque) {

        // 1. créer Document
        Document doc = new Document();

        // 🔥 génération ISBN propre (format métier)
        doc.setCodeIsbn(isbnGeneratorService.generateNextIsbn());

        doc.setCodeEmpruntable(true);
        doc.setDateAcquisition(LocalDate.now());
        doc.setEtatDocument("NEUF");

        // type = LIVRE (id = 1)
        TypeDocument td = new TypeDocument();
        td.setIdTypeDocument(1);
        doc.setTypeDocument(td);

        Document savedDoc = documentRepo.save(doc);

        // 2. créer Livre lié
        livre.setDocument(savedDoc);
        livre.setCreatedAt(LocalDateTime.now());
        livre.setUpdatedAt(LocalDateTime.now());
 
        doc.setCreatedAt(LocalDateTime.now());
        doc.setUpdatedAt(LocalDateTime.now());
        Livre savedLivre = livreRepo.save(livre);

        // 3. STOCKER dans bibliothèque du bibliothécaire
        Stocker s = new Stocker();
        s.setIdBibliotheque(bibliotheque.getId());
        s.setIdDocument(savedDoc.getIdDocument());
        s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());
        stockerRepo.save(s);

        return savedLivre;
    }

    public Livre updateLivre(Livre livre) {
        livre.setUpdatedAt(LocalDateTime.now());
        return livreRepo.save(livre);
    }

    public void deleteLivreById(Integer id) {
        livreRepo.deleteById(id);
    }

    public List<Livre> getByTitreContainingIgnoreCase(String titre) {
        return livreRepo.findByTitreLivreContainingIgnoreCase(titre);
    }
}