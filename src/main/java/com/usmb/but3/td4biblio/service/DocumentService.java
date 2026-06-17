package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.entity.StockerId;
import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmprunterRepo;
import com.usmb.but3.td4biblio.repository.StockerRepo;
import com.usmb.but3.td4biblio.security.SessionManager;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepo documentRepo;
    private final IsbnGeneratorService isbnGeneratorService;
    private final StockerRepo stockerRepo;
    private final EmprunterRepo emprunterRepo;


    public List<Document> getAllDocuments() {
        return documentRepo.findAll(Sort.by(Sort.Direction.ASC, "descriptionDocument"));
    }

    public Document getDocumentById(Integer id) {
        return documentRepo.findById(id).orElse(null);
    }

    public Document saveDocument(Document document, Bibliotheque bibliotheque) {

        boolean isNew = (document.getIdDocument() == null);

        if (isNew) {

            document.setCodeIsbn(isbnGeneratorService.generateNextIsbn());

            // 📅 date par défaut
            document.setDateAcquisition(LocalDate.now());

            // état par défaut si vide
            if (document.getEtatDocument() == null) {
                document.setEtatDocument("Neuf");
            }

            document.setCodeEmpruntable(true);

            // type auto (optionnel si déjà choisi dans UI)
            if (document.getTypeDocument() == null) {
                TypeDocument td = new TypeDocument();
                td.setIdTypeDocument(1); // ou "DOCUMENT GENERIQUE"
                document.setTypeDocument(td);
            }
        }
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepo.save(document);

        // 📦 stockage bibliothèque (comme Livre)
        Stocker s = new Stocker();
        s.setIdBibliotheque(bibliotheque.getId());
        s.setIdDocument(saved.getIdDocument());
        s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());

        stockerRepo.save(s);

        return saved;
    }

    public Document updateDocument(Document document) {
        document.setUpdatedAt(LocalDateTime.now());
        return documentRepo.save(document);
    }

    @Transactional
    public void deleteDocumentById(Integer id) {

        Document document = documentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable"));

        Integer documentId = document.getIdDocument();

        // 1. supprimer emprunts liés (si table existante)
        emprunterRepo.deleteByIdDocument_IdDocument(documentId);

        // 2. supprimer stocker (si tu stockes les documents en bibliothèque)
        Integer bibliothequeId = SessionManager.getBibliothecaire()
                .getBibliotheque()
                .getId();

        stockerRepo.deleteById(new StockerId(bibliothequeId, documentId));

        // 3. supprimer document
        documentRepo.delete(document);
    }

    public List<Document> getDocumentsByCodeIsbn(String codeIsbn) {
        return documentRepo.findByCodeIsbn(codeIsbn);
    }

    public List<Document> getDocumentsByDescription(String description) {
        return documentRepo.findByDescriptionDocumentContainingIgnoreCase(description);
    }

    public List<Document> getDocumentsByEmpruntable(Boolean empruntable) {
        return documentRepo.findByCodeEmpruntable(empruntable);
    }

    public List<Document> getDocumentsByEtat(String etat) {
        return documentRepo.findByEtatDocument(etat);
    }

    public List<Document> getDocumentsByDateAcquisitionBetween(LocalDate startDate, LocalDate endDate) {
        return documentRepo.findByDateAcquisitionBetween(startDate, endDate);
    }

    public List<Document> getDocumentsByFormatTaille(String formatTaille) {
        return documentRepo.findByFormatTaille(formatTaille);
    }

    public List<Document> getDocumentsByCodeEmplacement(String codeEmplacement) {
        return documentRepo.findByCodeEmplacement(codeEmplacement);
    }
}
