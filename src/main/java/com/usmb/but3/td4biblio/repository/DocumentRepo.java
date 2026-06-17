package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usmb.but3.td4biblio.entity.Document;

public interface DocumentRepo extends JpaRepository<Document, Integer> {
    List<Document> findByCodeIsbn(String codeIsbn);
    List<Document> findByDescriptionDocumentContainingIgnoreCase(String description);
    List<Document> findByCodeEmpruntable(Boolean empruntable);
    List<Document> findByEtatDocument(String etat);
    List<Document> findByDateAcquisitionBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
    List<Document> findByFormatTaille(String formatTaille);
    List<Document> findByCodeEmplacement(String codeEmplacement);
    @Query("SELECT d.codeIsbn FROM Document d WHERE d.codeIsbn IS NOT NULL ORDER BY d.idDocument DESC LIMIT 1")
    String findLastIsbn();
}
