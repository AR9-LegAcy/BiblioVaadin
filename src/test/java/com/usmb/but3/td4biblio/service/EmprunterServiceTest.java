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
import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.EmprunterId;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmprunterRepo;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

@SpringBootTest
class EmprunterServiceTest {

    @Mock
    private EmprunterRepo emprunterRepo;

    @Mock
    private DocumentRepo documentRepo;

    @Mock
    private EmprunteurRepo emprunteurRepo;

    @InjectMocks
    private EmprunterService emprunterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmprunts() {
        Document doc1 = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Document doc2 = new Document(2, "gif2.jpg", "Description2", "A5", LocalDate.of(2021, 2, 2), "E002", "978-0987654321", false, "Mauvais", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Emprunteur emprunteur1 = new Emprunteur();
        Emprunteur emprunteur2 = new Emprunteur();
        
        Emprunter emprunt1 = new Emprunter(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc1, emprunteur1);
        Emprunter emprunt2 = new Emprunter(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 15), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc2, emprunteur2);
        
        when(emprunterRepo.findAll(Sort.by(Sort.Direction.ASC, "dateEmprunt")))
            .thenReturn(Arrays.asList(emprunt1, emprunt2));

        List<Emprunter> emprunts = emprunterService.getAllEmprunts();

        assertEquals(2, emprunts.size());
        verify(emprunterRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "dateEmprunt"));
    }

    @Test
    void testGetEmpruntById() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Emprunteur emprunteur = new Emprunteur();
        EmprunterId id = new EmprunterId(1, 1);
        Emprunter emprunt = new Emprunter(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc, emprunteur);
        
        when(emprunterRepo.findById(id)).thenReturn(Optional.of(emprunt));

        Emprunter result = emprunterService.getEmpruntById(id);

        assertNotNull(result);
        assertEquals(LocalDate.of(2024, 1, 1), result.getDateEmprunt());
        verify(emprunterRepo, times(1)).findById(id);
    }

    @Test
    void testGetEmpruntByIdNotFound() {
        EmprunterId id = new EmprunterId(1, 1);
        when(emprunterRepo.findById(id)).thenReturn(Optional.empty());

        Emprunter result = emprunterService.getEmpruntById(id);

        assertNull(result);
        verify(emprunterRepo, times(1)).findById(id);
    }

    @Test
    void testSaveEmprunt() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Emprunteur emprunteur = new Emprunteur();
        Emprunter emprunt = new Emprunter(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15), null, 0, null, null, doc, emprunteur);
        Emprunter savedEmprunt = new Emprunter(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc, emprunteur);
        
        when(emprunterRepo.save(any(Emprunter.class))).thenReturn(savedEmprunt);

        Emprunter result = emprunterService.saveEmprunt(emprunt);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(emprunterRepo, times(1)).save(any(Emprunter.class));
    }

    @Test
    void testUpdateEmprunt() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Emprunteur emprunteur = new Emprunteur();
        Emprunter emprunt = new Emprunter(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 10), 0, LocalDateTime.now(), LocalDateTime.now(), doc, emprunteur);
        
        when(emprunterRepo.save(any(Emprunter.class))).thenReturn(emprunt);

        Emprunter result = emprunterService.updateEmprunt(emprunt);

        assertNotNull(result);
        assertNotNull(result.getDateRetourReelle());
        assertNotNull(result.getUpdatedAt());
        verify(emprunterRepo, times(1)).save(any(Emprunter.class));
    }

    @Test
    void testDeleteEmpruntById() {
        EmprunterId id = new EmprunterId(1, 1);
        doNothing().when(emprunterRepo).deleteById(id);

        emprunterService.deleteEmpruntById(id);

        verify(emprunterRepo, times(1)).deleteById(id);
    }

    @Test
    void testGetDocumentById() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        when(documentRepo.findById(1)).thenReturn(Optional.of(doc));

        Document result = emprunterService.getDocumentById(1);

        assertNotNull(result);
        assertEquals(1, result.getIdDocument());
        verify(documentRepo, times(1)).findById(1);
    }

    @Test
    void testGetDocumentByIdNotFound() {
        when(documentRepo.findById(1)).thenReturn(Optional.empty());

        Document result = emprunterService.getDocumentById(1);

        assertNull(result);
        verify(documentRepo, times(1)).findById(1);
    }

    @Test
    void testGetEmprunteurById() {
        Emprunteur emprunteur = new Emprunteur();
        emprunteur.setCarteEmprunteur(1);
        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(emprunteur));

        Emprunteur result = emprunterService.getEmprunteurById(1);

        assertNotNull(result);
        assertEquals(1, result.getCarteEmprunteur());
        verify(emprunteurRepo, times(1)).findById(1);
    }

    @Test
    void testGetEmprunteurByIdNotFound() {
        when(emprunteurRepo.findById(1)).thenReturn(Optional.empty());

        Emprunteur result = emprunterService.getEmprunteurById(1);

        assertNull(result);
        verify(emprunteurRepo, times(1)).findById(1);
    }

    @Test
    void testGetEmpruntsByCarteEmprunteur() {
        Emprunteur emprunteur = new Emprunteur();
        emprunteur.setCarteEmprunteur(1);
        Document doc1 = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Document doc2 = new Document(2, "gif2.jpg", "Description2", "A5", LocalDate.of(2021, 2, 2), "E002", "978-0987654321", false, "Mauvais", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        
        Emprunter emprunt1 = new Emprunter(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc1, emprunteur);
        Emprunter emprunt2 = new Emprunter(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 15), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc2, emprunteur);
        
        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(emprunteur));
        when(emprunterRepo.findByCarteEmprunteur(emprunteur)).thenReturn(Arrays.asList(emprunt1, emprunt2));

        List<Emprunter> emprunts = emprunterService.getEmpruntsByCarteEmprunteur(1);

        assertEquals(2, emprunts.size());
        verify(emprunteurRepo, times(1)).findById(1);
        verify(emprunterRepo, times(1)).findByCarteEmprunteur(emprunteur);
    }

    @Test
    void testGetEmpruntsByCarteEmprunteurNotFound() {
        when(emprunteurRepo.findById(1)).thenReturn(Optional.empty());

        List<Emprunter> emprunts = emprunterService.getEmpruntsByCarteEmprunteur(1);

        assertEquals(0, emprunts.size());
        verify(emprunteurRepo, times(1)).findById(1);
    }

    @Test
    void testGetEmpruntsByIdDocument() {
        Document doc = new Document(1, "gif1.jpg", "Description1", "A4", LocalDate.of(2020, 1, 1), "E001", "978-1234567890", true, "Bon", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        Emprunteur emprunteur1 = new Emprunteur();
        Emprunteur emprunteur2 = new Emprunteur();
        
        Emprunter emprunt1 = new Emprunter(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc, emprunteur1);
        Emprunter emprunt2 = new Emprunter(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 15), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc, emprunteur2);
        
        when(documentRepo.findById(1)).thenReturn(Optional.of(doc));
        when(emprunterRepo.findByIdDocument(doc)).thenReturn(Arrays.asList(emprunt1, emprunt2));

        List<Emprunter> emprunts = emprunterService.getEmpruntsByIdDocument(1);

        assertEquals(2, emprunts.size());
        verify(documentRepo, times(1)).findById(1);
        verify(emprunterRepo, times(1)).findByIdDocument(doc);
    }

    @Test
    void testGetEmpruntsByIdDocumentNotFound() {
        when(documentRepo.findById(1)).thenReturn(Optional.empty());

        List<Emprunter> emprunts = emprunterService.getEmpruntsByIdDocument(1);

        assertEquals(0, emprunts.size());
        verify(documentRepo, times(1)).findById(1);
    }
}