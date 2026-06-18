package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.entity.StockerId;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmprunterRepo;
import com.usmb.but3.td4biblio.repository.StockerRepo;
import com.usmb.but3.td4biblio.security.SessionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceMockTest {

    @Mock
    private Bibliotheque bibliotheque;

    @Mock
    private IsbnGeneratorService isbnGeneratorService;

    @Mock
    private EmprunterRepo emprunterRepo;

    @Mock
    private StockerRepo stockerRepo;

    @Mock
    private DocumentRepo documentRepo;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void testGetAllDocuments() {
        // Arrange
        Document doc1 = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        Document doc2 = new Document(2, "image2.gif", "Notre-Dame de Paris", "PDF", LocalDate.of(2019, 5, 20), "B2", "978-2-07-032722-6", true, "Très bon", null, null, null, null, null);
        List<Document> documents = List.of(doc1, doc2);

        when(documentRepo.findAll(any(Sort.class))).thenReturn(documents);

        // Act
        List<Document> result = documentService.getAllDocuments();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(doc1, doc2);
        verify(documentRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetDocumentById_Found() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        when(documentRepo.findById(1)).thenReturn(Optional.of(doc));

        // Act
        Document result = documentService.getDocumentById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdDocument()).isEqualTo(1);
        assertThat(result.getDescriptionDocument()).isEqualTo("Les Misérables");
        assertThat(result.getCodeIsbn()).isEqualTo("978-2-07-036822-8");
        verify(documentRepo, times(1)).findById(1);
    }

    @Test
    void testGetDocumentById_NotFound() {
        // Arrange
        when(documentRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Document result = documentService.getDocumentById(999);

        // Assert
        assertThat(result).isNull();
        verify(documentRepo, times(1)).findById(999);
    }

    @Test
    void testSaveDocument() {

        Document doc = new Document();
        doc.setDescriptionDocument("L'Aleph");
        doc.setFormatTaille("PDF");
        doc.setCodeEmpruntable(true);
        doc.setEtatDocument("Bon");
        doc.setCodeEmplacement("C3");
        doc.setDateAcquisition(LocalDate.of(2021, 3, 10));

        Bibliotheque bib = new Bibliotheque();
        bib.setId(1);

        // mock ISBN
        when(isbnGeneratorService.generateNextIsbn())
                .thenReturn("978-0-000-00000-1");

        // mock Document save
        Document savedDoc = new Document(
                1, "img", "L'Aleph", "PDF",
                LocalDate.of(2021, 3, 10), "C3",
                "978-0-000-00000-1", true, "Bon",
                LocalDateTime.now(), LocalDateTime.now(),
                null, null, null
        );

        when(documentRepo.save(any(Document.class)))
                .thenReturn(savedDoc);

        // mock Stocker save (IMPORTANT)
        when(stockerRepo.save(any(Stocker.class)))
                .thenReturn(new Stocker());

        Document result = documentService.saveDocument(doc, bib);

        assertThat(result).isNotNull();
        assertThat(result.getIdDocument()).isEqualTo(1);

        verify(isbnGeneratorService, times(1)).generateNextIsbn();
        verify(documentRepo, times(1)).save(any(Document.class));
        verify(stockerRepo, times(1)).save(any(Stocker.class));
    }

    @Test
    void testUpdateDocument() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        
        Document updatedDoc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Usé", LocalDateTime.now(), LocalDateTime.now(), null, null, null);

        when(documentRepo.save(doc)).thenReturn(updatedDoc);

        // Act
        Document result = documentService.updateDocument(doc);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEtatDocument()).isEqualTo("Usé");
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(documentRepo, times(1)).save(doc);
    }

    @Test
    void testDeleteDocumentById() {
    
        Document doc = new Document();
        doc.setIdDocument(1);
    
        when(documentRepo.findById(1))
                .thenReturn(Optional.of(doc));
    
        try (MockedStatic<SessionManager> sessionMock =
                     Mockito.mockStatic(SessionManager.class)) {
                    
            Bibliotheque bibliotheque = new Bibliotheque();
            bibliotheque.setId(1);
                    
            Bibliothecaire bibliothecaire = new Bibliothecaire();
            bibliothecaire.setBibliotheque(bibliotheque);
                    
            sessionMock.when(SessionManager::getBibliothecaire)
                    .thenReturn(bibliothecaire);
                    
            documentService.deleteDocumentById(1);
                    
            verify(emprunterRepo).deleteByIdDocument_IdDocument(1);
            verify(stockerRepo).deleteById(any(StockerId.class));
            verify(documentRepo).delete(doc);
        }
    }

    @Test
    void testGetDocumentsByCodeIsbn() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        List<Document> documents = List.of(doc);

        when(documentRepo.findByCodeIsbn("978-2-07-036822-8")).thenReturn(documents);

        // Act
        List<Document> result = documentService.getDocumentsByCodeIsbn("978-2-07-036822-8");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCodeIsbn()).isEqualTo("978-2-07-036822-8");
        verify(documentRepo, times(1)).findByCodeIsbn("978-2-07-036822-8");
    }

    @Test
    void testGetDocumentsByDescription() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        List<Document> documents = List.of(doc);

        when(documentRepo.findByDescriptionDocumentContainingIgnoreCase("misérables")).thenReturn(documents);

        // Act
        List<Document> result = documentService.getDocumentsByDescription("misérables");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescriptionDocument()).isEqualTo("Les Misérables");
        verify(documentRepo, times(1)).findByDescriptionDocumentContainingIgnoreCase("misérables");
    }

    @Test
    void testGetDocumentsByEmpruntable_True() {
        // Arrange
        Document doc1 = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        Document doc2 = new Document(2, "image2.gif", "Notre-Dame de Paris", "PDF", LocalDate.of(2019, 5, 20), "B2", "978-2-07-032722-6", true, "Très bon", null, null, null, null, null);
        List<Document> documents = List.of(doc1, doc2);

        when(documentRepo.findByCodeEmpruntable(true)).thenReturn(documents);

        // Act
        List<Document> result = documentService.getDocumentsByEmpruntable(true);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(d -> d.getCodeEmpruntable() == true);
        verify(documentRepo, times(1)).findByCodeEmpruntable(true);
    }

    @Test
    void testGetDocumentsByEtat() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        List<Document> documents = List.of(doc);

        when(documentRepo.findByEtatDocument("Bon")).thenReturn(documents);

        // Act
        List<Document> result = documentService.getDocumentsByEtat("Bon");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEtatDocument()).isEqualTo("Bon");
        verify(documentRepo, times(1)).findByEtatDocument("Bon");
    }

    @Test
    void testGetDocumentsByDateAcquisitionBetween() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        List<Document> documents = List.of(doc);

        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);

        when(documentRepo.findByDateAcquisitionBetween(startDate, endDate)).thenReturn(documents);

        // Act
        List<Document> result = documentService.getDocumentsByDateAcquisitionBetween(startDate, endDate);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDateAcquisition()).isBetween(startDate, endDate);
        verify(documentRepo, times(1)).findByDateAcquisitionBetween(startDate, endDate);
    }

    @Test
    void testGetDocumentsByFormatTaille() {
        // Arrange
        Document doc1 = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        Document doc2 = new Document(2, "image2.gif", "Notre-Dame de Paris", "PDF", LocalDate.of(2019, 5, 20), "B2", "978-2-07-032722-6", true, "Très bon", null, null, null, null, null);
        List<Document> documents = List.of(doc1, doc2);

        when(documentRepo.findByFormatTaille("PDF")).thenReturn(documents);

        // Act
        List<Document> result = documentService.getDocumentsByFormatTaille("PDF");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(d -> d.getFormatTaille().equals("PDF"));
        verify(documentRepo, times(1)).findByFormatTaille("PDF");
    }

    @Test
    void testGetDocumentsByCodeEmplacement() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        List<Document> documents = List.of(doc);

        when(documentRepo.findByCodeEmplacement("A1")).thenReturn(documents);

        // Act
        List<Document> result = documentService.getDocumentsByCodeEmplacement("A1");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCodeEmplacement()).isEqualTo("A1");
        verify(documentRepo, times(1)).findByCodeEmplacement("A1");
    }
}