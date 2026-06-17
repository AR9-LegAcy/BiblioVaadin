package com.usmb.but3.td4biblio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.repository.LivreRepo;

@ExtendWith(MockitoExtension.class)
public class LivreServiceMockTest {

    @Mock
    private LivreRepo livreRepo;

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
    
        Livre savedLivre = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null);
    
        when(livreRepo.save(any(Livre.class))).thenReturn(savedLivre);
    
        // Act
        Livre result = livreService.saveLivre(livre, bibliotheque);
    
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdDocument()).isEqualTo(1);
    
        verify(livreRepo, times(1)).save(any(Livre.class));
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

        Livre savedLivre = new Livre(
                1,
                "Le Petit Prince",
                96,
                LocalDate.of(1943, 4, 6),
                createdAt,
                LocalDateTime.now(),
                null,
                null);

        when(livreRepo.save(any(Livre.class))).thenReturn(savedLivre);

        // Act
        Livre result = livreService.saveLivre(livre, bibliotheque);

        // Assert
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(livreRepo, times(1)).save(any(Livre.class));
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
        // Act
        livreService.deleteLivreById(1);

        // Assert
        verify(livreRepo, times(1)).deleteById(1);
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