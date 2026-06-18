package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmprunterRepo;
import com.usmb.but3.td4biblio.repository.LivreRepo;
import com.usmb.but3.td4biblio.repository.StockerRepo;
import com.usmb.but3.td4biblio.security.SessionManager;

@ExtendWith(MockitoExtension.class)
public class LivreServiceMockTest {

    @Mock
    private LivreRepo livreRepo;
    
    @Mock
    private DocumentRepo documentRepo;
    
    @Mock
    private StockerRepo stockerRepo;
    
    @Mock
    private IsbnGeneratorService isbnGeneratorService;
    
    @Mock
    private EmprunterRepo emprunterRepo;
    
    @InjectMocks
    private LivreService livreService;

    @Test
    void testGetAllLivres() {
        // Arrange
        Livre livre1 = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                null,
                null,
                null,
                null);

        Livre livre2 = new Livre(
                2,
                "1984",
                328,
                LocalDate.of(1949, 6, 8),
                null,
                null,
                null,
                null);

        List<Livre> livres = List.of(livre1, livre2);

        when(livreRepo.findAll(any(Sort.class))).thenReturn(livres);

        // Act
        List<Livre> result = livreService.getAllLivres();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(livre1, livre2);

        verify(livreRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetLivreById_Found() {
        // Arrange
        Livre livre = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                null,
                null,
                null,
                null);

        when(livreRepo.findById(1)).thenReturn(Optional.of(livre));

        // Act
        Livre result = livreService.getLivreById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdDocument()).isEqualTo(1);
        assertThat(result.getTitreLivre()).isEqualTo("Le Petit Prince");

        verify(livreRepo, times(1)).findById(1);
    }

    @Test
    void testGetLivreById_NotFound() {
        // Arrange
        when(livreRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Livre result = livreService.getLivreById(999);

        // Assert
        assertThat(result).isNull();

        verify(livreRepo, times(1)).findById(999);
    }

    @Test
    void testSaveLivre() {
        // Arrange
        Livre livre = new Livre();
        livre.setTitreLivre("Le Petit Prince");
        livre.setNbPages(96);
        livre.setDatePublication(LocalDate.of(1943, 4, 6));
    
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.setId(1);
    
        Document document = new Document();
        document.setIdDocument(1);
        document.setCodeIsbn("978-0-000-00000-1");
    
        Livre savedLivre = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null);
    
        when(isbnGeneratorService.generateNextIsbn())
                .thenReturn("978-0-000-00000-1");
    
        when(documentRepo.save(any(Document.class)))
                .thenReturn(document);
    
        when(livreRepo.save(any(Livre.class)))
                .thenReturn(savedLivre);
    
        when(stockerRepo.save(any(Stocker.class)))
                .thenReturn(new Stocker());
    
        // Act
        Livre result = livreService.saveLivre(livre, bibliotheque);
    
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdDocument()).isEqualTo(1);
    
        verify(isbnGeneratorService, times(1))
                .generateNextIsbn();
    
        verify(documentRepo, times(1))
                .save(any(Document.class));
    
        verify(livreRepo, times(1))
                .save(any(Livre.class));
    
        verify(stockerRepo, times(1))
                .save(any(Stocker.class));
    }

    @Test
    void testSaveLivre_WithExistingCreatedAt() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
    
        Livre livre = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                createdAt,
                null,
                null,
                null);
    
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.setId(1);
    
        Document document = new Document();
        document.setIdDocument(1);
        document.setCodeIsbn("978-0-000-00000-1");
    
        Livre savedLivre = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                createdAt,
                LocalDateTime.now(),
                null,
                null);
    
        when(isbnGeneratorService.generateNextIsbn())
                .thenReturn("978-0-000-00000-1");
    
        when(documentRepo.save(any(Document.class)))
                .thenReturn(document);
    
        when(livreRepo.save(any(Livre.class)))
                .thenReturn(savedLivre);
    
        when(stockerRepo.save(any(Stocker.class)))
                .thenReturn(new Stocker());
    
        // Act
        Livre result = livreService.saveLivre(livre, bibliotheque);
    
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isNotNull();
    
        verify(isbnGeneratorService, times(1)).generateNextIsbn();
        verify(documentRepo, times(1)).save(any(Document.class));
        verify(livreRepo, times(1)).save(any(Livre.class));
        verify(stockerRepo, times(1)).save(any(Stocker.class));
    }

    @Test
    void testUpdateLivre() {
        // Arrange
        Livre livre = new Livre(
                1,
                "Le Petit Prince - Edition 2025",
                100,
                LocalDate.of(1943, 4, 6),
                LocalDateTime.now().minusDays(10),
                null,
                null,
                null);

        Livre updatedLivre = new Livre(
                1,
                "Le Petit Prince - Edition 2025",
                100,
                LocalDate.of(1943, 4, 6),
                livre.getCreatedAt(),
                LocalDateTime.now(),
                null,
                null);

        when(livreRepo.save(livre)).thenReturn(updatedLivre);

        // Act
        Livre result = livreService.updateLivre(livre);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(livreRepo, times(1)).save(livre);
    }

    @Test
    void testDeleteLivreById() {
    
        // Arrange
        Livre livre = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                null,
                null,
                null,
                null
        );
    
        Document document = new Document();
        document.setIdDocument(10);
    
        livre.setDocument(document);
    
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.setId(1);
    
        when(livreRepo.findById(1))
                .thenReturn(Optional.of(livre));
    
        try (MockedStatic<SessionManager> mockedSession = mockStatic(SessionManager.class)) {
    
            Bibliothecaire bibliothecaire = mock(Bibliothecaire.class);
            Bibliotheque bib = new Bibliotheque();
            bib.setId(1);
    
            when(bibliothecaire.getBibliotheque()).thenReturn(bib);
    
            mockedSession.when(SessionManager::getBibliothecaire)
                    .thenReturn(bibliothecaire);
    
            // Act
            livreService.deleteLivreById(1);
        }
    
        // Assert
        verify(livreRepo, times(1)).findById(1);
    
        verify(emprunterRepo, times(1))
                .deleteByIdDocument_IdDocument(10);
    
        verify(stockerRepo, times(1))
                .deleteById(any());
    
        verify(livreRepo, times(1))
                .delete(livre);
    }

    @Test
    void testGetByTitreContainingIgnoreCase() {
        // Arrange
        Livre livre = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                null,
                null,
                null,
                null);

        List<Livre> livres = List.of(livre);

        when(livreRepo.findByTitreLivreContainingIgnoreCase("prince"))
                .thenReturn(livres);

        // Act
        List<Livre> result =
                livreService.getByTitreContainingIgnoreCase("prince");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitreLivre())
                .isEqualTo("Le Petit Prince");

        verify(livreRepo, times(1))
                .findByTitreLivreContainingIgnoreCase("prince");
    }
}