package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.repository.DocumentRepo;

@SpringBootTest
class DocumentServiceTest {

    @Mock
    private DocumentRepo documentRepo;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDocuments() {
        Document doc1 = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Document doc2 = new Document(2, "gif2.jpg", "Description2", "A5", LocalDate.of(2021, 2, 2), "E002", "978-0987654321", false, "Mauvais", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findAll(Sort.by(Sort.Direction.ASC, "descriptionDocument")))
            .thenReturn(Arrays.asList(doc1, doc2));

        List<Document> documents = documentService.getAllDocuments();

        assertEquals(2, documents.size());
        assertEquals("Description1", documents.get(0).getDescriptionDocument());
        assertEquals("Description2", documents.get(1).getDescriptionDocument());
        verify(documentRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "descriptionDocument"));
    }

    @Test
    void testGetDocumentById() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findById(1)).thenReturn(Optional.of(doc));

        Document result = documentService.getDocumentById(1);

        assertNotNull(result);
        assertEquals(1, result.getIdDocument());
        assertEquals("Description1", result.getDescriptionDocument());
        verify(documentRepo, times(1)).findById(1);
    }

    @Test
    void testGetDocumentByIdNotFound() {
        when(documentRepo.findById(1)).thenReturn(Optional.empty());

        Document result = documentService.getDocumentById(1);

        assertNull(result);
        verify(documentRepo, times(1)).findById(1);
    }

    @Test
    void testSaveDocument() {
        Document doc = new Document(null, "gif1.jpg", "New Description", "A4", LocalDate.of(2022, 3, 3), "E001", "978-1234567890", true, "Bon", null, null, null, null, null);
        Document savedDoc = new Document(1, "gif1.jpg", "New Description", "A4", LocalDate.of(2022, 3, 3), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.save(any(Document.class))).thenReturn(savedDoc);

        Document result = documentService.saveDocument(doc);

        assertNotNull(result);
        assertEquals(1, result.getIdDocument());
        assertEquals("New Description", result.getDescriptionDocument());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(documentRepo, times(1)).save(any(Document.class));
    }

    @Test
    void testUpdateDocument() {
        Document doc = new Document(1, "gif1.jpg", "Updated Description", "A4", LocalDate.of(2023, 4, 4), "E001", "978-1234567890", true, "Excellent", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.save(any(Document.class))).thenReturn(doc);

        Document result = documentService.updateDocument(doc);

        assertNotNull(result);
        assertEquals(1, result.getIdDocument());
        assertEquals("Updated Description", result.getDescriptionDocument());
        assertNotNull(result.getUpdatedAt());
        verify(documentRepo, times(1)).save(any(Document.class));
    }

    @Test
    void testDeleteDocumentById() {
        doNothing().when(documentRepo).deleteById(1);

        documentService.deleteDocumentById(1);

        verify(documentRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetDocumentsByCodeIsbn() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findByCodeIsbn("978-1234567890")).thenReturn(Arrays.asList(doc));

        List<Document> documents = documentService.getDocumentsByCodeIsbn("978-1234567890");

        assertEquals(1, documents.size());
        assertEquals("978-1234567890", documents.get(0).getCodeIsbn());
        verify(documentRepo, times(1)).findByCodeIsbn("978-1234567890");
    }

    @Test
    void testGetDocumentsByDescription() {
        Document doc1 = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Document doc2 = new Document(2, "gif2.jpg", "Another Description1", "A5", LocalDate.of(2021, 2, 2), "E002", "978-0987654321", false, "Mauvais", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findByDescriptionDocumentContainingIgnoreCase("description1")).thenReturn(Arrays.asList(doc1, doc2));

        List<Document> documents = documentService.getDocumentsByDescription("description1");

        assertEquals(2, documents.size());
        verify(documentRepo, times(1)).findByDescriptionDocumentContainingIgnoreCase("description1");
    }

    @Test
    void testGetDocumentsByEmpruntable() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findByCodeEmpruntable(true)).thenReturn(Arrays.asList(doc));

        List<Document> documents = documentService.getDocumentsByEmpruntable(true);

        assertEquals(1, documents.size());
        assertTrue(documents.get(0).getCodeEmpruntable());
        verify(documentRepo, times(1)).findByCodeEmpruntable(true);
    }

    @Test
    void testGetDocumentsByEtat() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findByEtatDocument("Bon")).thenReturn(Arrays.asList(doc));

        List<Document> documents = documentService.getDocumentsByEtat("Bon");

        assertEquals(1, documents.size());
        assertEquals("Bon", documents.get(0).getEtatDocument());
        verify(documentRepo, times(1)).findByEtatDocument("Bon");
    }

    @Test
    void testGetDocumentsByDateAcquisitionBetween() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 6, 15), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);
        when(documentRepo.findByDateAcquisitionBetween(startDate, endDate)).thenReturn(Arrays.asList(doc));

        List<Document> documents = documentService.getDocumentsByDateAcquisitionBetween(startDate, endDate);

        assertEquals(1, documents.size());
        verify(documentRepo, times(1)).findByDateAcquisitionBetween(startDate, endDate);
    }

    @Test
    void testGetDocumentsByFormatTaille() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findByFormatTaille("A4")).thenReturn(Arrays.asList(doc));

        List<Document> documents = documentService.getDocumentsByFormatTaille("A4");

        assertEquals(1, documents.size());
        assertEquals("A4", documents.get(0).getFormatTaille());
        verify(documentRepo, times(1)).findByFormatTaille("A4");
    }

    @Test
    void testGetDocumentsByCodeEmplacement() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findByCodeEmplacement("E001")).thenReturn(Arrays.asList(doc));

        List<Document> documents = documentService.getDocumentsByCodeEmplacement("E001");

        assertEquals(1, documents.size());
        assertEquals("E001", documents.get(0).getCodeEmplacement());
        verify(documentRepo, times(1)).findByCodeEmplacement("E001");
    }
}