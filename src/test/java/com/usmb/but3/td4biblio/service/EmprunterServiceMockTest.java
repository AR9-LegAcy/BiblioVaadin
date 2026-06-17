package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.EmprunterId;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmprunterRepo;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmprunterServiceMockTest {

    @Mock
    private EmprunterRepo emprunterRepo;

    @Mock
    private DocumentRepo documentRepo;

    @Mock
    private EmprunteurRepo emprunteurRepo;

    @InjectMocks
    private EmprunterService emprunterService;

    @Test
    void testGetAllEmprunts() {
        // Arrange
        Document doc1 = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        Emprunteur emprunteur1 = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        
        Emprunter emprunt1 = new Emprunter(LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 24), null, 0, null, null, doc1, emprunteur1);
        
        Document doc2 = new Document(2, "image2.gif", "Notre-Dame de Paris", "PDF", LocalDate.of(2019, 5, 20), "B2", "978-2-07-032722-6", true, "Très bon", null, null, null, null, null);
        Emprunteur emprunteur2 = new Emprunteur(2, "Martin", "Sophie", "20 avenue Lyon", "Lyon", "69000", "sophie@example.com", LocalDate.of(1988, 3, 22), null, null, "password456", null, null, null);
        
        Emprunter emprunt2 = new Emprunter(LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 29), null, 0, null, null, doc2, emprunteur2);
        
        List<Emprunter> emprunts = List.of(emprunt1, emprunt2);

        when(emprunterRepo.findAll(any(Sort.class))).thenReturn(emprunts);

        // Act
        List<Emprunter> result = emprunterService.getAllEmprunts();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(emprunt1, emprunt2);
        verify(emprunterRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetEmpruntById_Found() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        Emprunteur emprunteur = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        
        Emprunter emprunt = new Emprunter(LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 24), null, 0, null, null, doc, emprunteur);
        EmprunterId empruntId = new EmprunterId(1, 1);

        when(emprunterRepo.findById(empruntId)).thenReturn(Optional.of(emprunt));

        // Act
        Emprunter result = emprunterService.getEmpruntById(empruntId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDateEmprunt()).isEqualTo(LocalDate.of(2024, 1, 10));
        assertThat(result.getIdDocument().getIdDocument()).isEqualTo(1);
        assertThat(result.getCarteEmprunteur().getCarteEmprunteur()).isEqualTo(1);
        verify(emprunterRepo, times(1)).findById(empruntId);
    }

    @Test
    void testGetEmpruntById_NotFound() {
        // Arrange
        EmprunterId empruntId = new EmprunterId(999, 999);
        when(emprunterRepo.findById(empruntId)).thenReturn(Optional.empty());

        // Act
        Emprunter result = emprunterService.getEmpruntById(empruntId);

        // Assert
        assertThat(result).isNull();
        verify(emprunterRepo, times(1)).findById(empruntId);
    }

    @Test
    void testSaveEmprunt() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        Emprunteur emprunteur = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        
        Emprunter emprunt = new Emprunter();
        emprunt.setDateEmprunt(LocalDate.of(2024, 6, 17));
        emprunt.setDateRetourPrevue(LocalDate.of(2024, 7, 1));
        emprunt.setProlongationEmprunt(0);
        emprunt.setIdDocument(doc);
        emprunt.setCarteEmprunteur(emprunteur);

        Emprunter savedEmprunt = new Emprunter(LocalDate.of(2024, 6, 17), LocalDate.of(2024, 7, 1), null, 0, LocalDateTime.now(), LocalDateTime.now(), doc, emprunteur);

        when(emprunterRepo.save(any(Emprunter.class))).thenReturn(savedEmprunt);

        // Act
        Emprunter result = emprunterService.saveEmprunt(emprunt);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDateEmprunt()).isEqualTo(LocalDate.of(2024, 6, 17));
        assertThat(result.getDateRetourPrevue()).isEqualTo(LocalDate.of(2024, 7, 1));
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(emprunterRepo, times(1)).save(any(Emprunter.class));
    }

    @Test
    void testUpdateEmprunt() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        Emprunteur emprunteur = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        
        Emprunter emprunt = new Emprunter(LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 24), null, 0, null, null, doc, emprunteur);
        
        Emprunter updatedEmprunt = new Emprunter(LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 24), LocalDate.of(2024, 1, 22), 0, null, LocalDateTime.now(), doc, emprunteur);

        when(emprunterRepo.save(emprunt)).thenReturn(updatedEmprunt);

        // Act
        Emprunter result = emprunterService.updateEmprunt(emprunt);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDateRetourReelle()).isEqualTo(LocalDate.of(2024, 1, 22));
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(emprunterRepo, times(1)).save(emprunt);
    }

    @Test
    void testDeleteEmpruntById() {
        // Arrange
        EmprunterId empruntId = new EmprunterId(1, 1);

        // Act
        emprunterService.deleteEmpruntById(empruntId);

        // Assert
        verify(emprunterRepo, times(1)).deleteById(empruntId);
    }

    @Test
    void testGetDocumentById_Found() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        when(documentRepo.findById(1)).thenReturn(Optional.of(doc));

        // Act
        Document result = emprunterService.getDocumentById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdDocument()).isEqualTo(1);
        assertThat(result.getDescriptionDocument()).isEqualTo("Les Misérables");
        verify(documentRepo, times(1)).findById(1);
    }

    @Test
    void testGetDocumentById_NotFound() {
        // Arrange
        when(documentRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Document result = emprunterService.getDocumentById(999);

        // Assert
        assertThat(result).isNull();
        verify(documentRepo, times(1)).findById(999);
    }

    @Test
    void testGetEmprunteurById_Found() {
        // Arrange
        Emprunteur emprunteur = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(emprunteur));

        // Act
        Emprunteur result = emprunterService.getEmprunteurById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCarteEmprunteur()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Dupont");
        verify(emprunteurRepo, times(1)).findById(1);
    }

    @Test
    void testGetEmprunteurById_NotFound() {
        // Arrange
        when(emprunteurRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Emprunteur result = emprunterService.getEmprunteurById(999);

        // Assert
        assertThat(result).isNull();
        verify(emprunteurRepo, times(1)).findById(999);
    }

    @Test
    void testGetEmpruntsByCarteEmprunteur_Found() {
        // Arrange
        Emprunteur emprunteur = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        
        Document doc1 = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        Document doc2 = new Document(2, "image2.gif", "Notre-Dame de Paris", "PDF", LocalDate.of(2019, 5, 20), "B2", "978-2-07-032722-6", true, "Très bon", null, null, null, null, null);
        
        Emprunter emprunt1 = new Emprunter(LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 24), null, 0, null, null, doc1, emprunteur);
        Emprunter emprunt2 = new Emprunter(LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 29), null, 0, null, null, doc2, emprunteur);
        
        List<Emprunter> emprunts = List.of(emprunt1, emprunt2);

        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(emprunteur));
        when(emprunterRepo.findByCarteEmprunteur(emprunteur)).thenReturn(emprunts);

        // Act
        List<Emprunter> result = emprunterService.getEmpruntsByCarteEmprunteur(1);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(emprunt1, emprunt2);
        assertThat(result).allMatch(e -> e.getCarteEmprunteur().equals(emprunteur));
        verify(emprunteurRepo, times(1)).findById(1);
        verify(emprunterRepo, times(1)).findByCarteEmprunteur(emprunteur);
    }

    @Test
    void testGetEmpruntsByCarteEmprunteur_NotFound() {
        // Arrange
        when(emprunteurRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        List<Emprunter> result = emprunterService.getEmpruntsByCarteEmprunteur(999);

        // Assert
        assertThat(result).isEmpty();
        verify(emprunteurRepo, times(1)).findById(999);
        verify(emprunterRepo, never()).findByCarteEmprunteur(any());
    }

    @Test
    void testGetEmpruntsByIdDocument_Found() {
        // Arrange
        Document doc = new Document(1, "image1.gif", "Les Misérables", "PDF", LocalDate.of(2020, 1, 15), "A1", "978-2-07-036822-8", true, "Bon", null, null, null, null, null);
        
        Emprunteur emprunteur1 = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        Emprunteur emprunteur2 = new Emprunteur(2, "Martin", "Sophie", "20 avenue Lyon", "Lyon", "69000", "sophie@example.com", LocalDate.of(1988, 3, 22), null, null, "password456", null, null, null);
        
        Emprunter emprunt1 = new Emprunter(LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 24), null, 0, null, null, doc, emprunteur1);
        Emprunter emprunt2 = new Emprunter(LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 29), null, 0, null, null, doc, emprunteur2);
        
        List<Emprunter> emprunts = List.of(emprunt1, emprunt2);

        when(documentRepo.findById(1)).thenReturn(Optional.of(doc));
        when(emprunterRepo.findByIdDocument(doc)).thenReturn(emprunts);

        // Act
        List<Emprunter> result = emprunterService.getEmpruntsByIdDocument(1);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(emprunt1, emprunt2);
        assertThat(result).allMatch(e -> e.getIdDocument().equals(doc));
        verify(documentRepo, times(1)).findById(1);
        verify(emprunterRepo, times(1)).findByIdDocument(doc);
    }

    @Test
    void testGetEmpruntsByIdDocument_NotFound() {
        // Arrange
        when(documentRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        List<Emprunter> result = emprunterService.getEmpruntsByIdDocument(999);

        // Assert
        assertThat(result).isEmpty();
        verify(documentRepo, times(1)).findById(999);
        verify(emprunterRepo, never()).findByIdDocument(any());
    }
}