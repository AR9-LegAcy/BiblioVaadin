package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.repository.DocumentRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepo documentRepo;

    public List<Document> getAllDocuments() {
        return documentRepo.findAll(Sort.by(Sort.Direction.ASC, "descriptionDocument"));
    }

    public Document getDocumentById(Integer id) {
        return documentRepo.findById(id).orElse(null);
    }

    public Document saveDocument(Document document) {
        if (document.getCreatedAt() == null) {
            document.setCreatedAt(LocalDateTime.now());
        }
        document.setUpdatedAt(LocalDateTime.now());
        return documentRepo.save(document);
    }

    public Document updateDocument(Document document) {
        document.setUpdatedAt(LocalDateTime.now());
        return documentRepo.save(document);
    }

    public void deleteDocumentById(Integer id) {
        documentRepo.deleteById(id);
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
