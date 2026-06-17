package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BibliothequeServiceMockTest {

    @Mock
    private BibliothequeRepo bibliothequeRepo;

    @InjectMocks
    private BibliothequeService bibliothequeService;

    @Test
    void testGetAllBibliotheques() {
        // Arrange
        Bibliotheque biblio1 = new Bibliotheque(1, "BNF Paris", "Paris", "11 rue de Richelieu", "75002", "09:00-22:00", null, null, null, null);
        Bibliotheque biblio2 = new Bibliotheque(2, "Bibliothèque Municipale Lyon", "Lyon", "30 boulevard Vivier Merle", "69003", "09:00-20:00", null, null, null, null);
        List<Bibliotheque> bibliotheques = List.of(biblio1, biblio2);

        when(bibliothequeRepo.findAll(any(Sort.class))).thenReturn(bibliotheques);

        // Act
        List<Bibliotheque> result = bibliothequeService.getAllBibliotheques();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(biblio1, biblio2);
        verify(bibliothequeRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetBibliothequeById_Found() {
        // Arrange
        Bibliotheque biblio = new Bibliotheque(1, "BNF Paris", "Paris", "11 rue de Richelieu", "75002", "09:00-22:00", null, null, null, null);
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(biblio));

        // Act
        Bibliotheque result = bibliothequeService.getBibliothequeById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("BNF Paris");
        assertThat(result.getAdresseVille()).isEqualTo("Paris");
        verify(bibliothequeRepo, times(1)).findById(1);
    }

    @Test
    void testGetBibliothequeById_NotFound() {
        // Arrange
        when(bibliothequeRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Bibliotheque result = bibliothequeService.getBibliothequeById(999);

        // Assert
        assertThat(result).isNull();
        verify(bibliothequeRepo, times(1)).findById(999);
    }

    @Test
    void testSaveBibliotheque() {
        // Arrange
        Bibliotheque biblio = new Bibliotheque();
        biblio.setNom("Bibliothèque Sainte-Geneviève");
        biblio.setAdresseVille("Paris");
        biblio.setAdresseRue("10 place du Panthéon");
        biblio.setAdresseCP("75005");
        biblio.setHoraires("10:00-19:00");

        Bibliotheque savedBiblio = new Bibliotheque();
        savedBiblio.setId(3);
        savedBiblio.setNom("Bibliothèque Sainte-Geneviève");
        savedBiblio.setAdresseVille("Paris");
        savedBiblio.setAdresseRue("10 place du Panthéon");
        savedBiblio.setAdresseCP("75005");
        savedBiblio.setHoraires("10:00-19:00");
        savedBiblio.setCreatedAt(LocalDateTime.now());
        savedBiblio.setUpdatedAt(LocalDateTime.now());

        when(bibliothequeRepo.save(any(Bibliotheque.class))).thenReturn(savedBiblio);

        // Act
        Bibliotheque result = bibliothequeService.saveBibliotheque(biblio);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3);
        assertThat(result.getNom()).isEqualTo("Bibliothèque Sainte-Geneviève");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(bibliothequeRepo, times(1)).save(any(Bibliotheque.class));
    }

    @Test
    void testUpdateBibliotheque() {
        // Arrange
        Bibliotheque biblio = new Bibliotheque(1, "BNF Paris", "Paris", "11 rue de Richelieu", "75002", "09:00-22:00", null, null, null, null);
        
        Bibliotheque updatedBiblio = new Bibliotheque(1, "BNF Paris", "Paris", "11 rue de Richelieu", "75002", "10:00-23:00", null, LocalDateTime.now(), null, null);

        when(bibliothequeRepo.save(biblio)).thenReturn(updatedBiblio);

        // Act
        Bibliotheque result = bibliothequeService.updateBibliotheque(biblio);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getHoraires()).isEqualTo("10:00-23:00");
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(bibliothequeRepo, times(1)).save(biblio);
    }

    @Test
    void testDeleteBibliothequeById() {
        // Act
        bibliothequeService.deleteBibliothequeById(1);

        // Assert
        verify(bibliothequeRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetBibliothequesByNom() {
        // Arrange
        Bibliotheque biblio = new Bibliotheque(1, "BNF Paris", "Paris", "11 rue de Richelieu", "75002", "09:00-22:00", null, null, null, null);
        List<Bibliotheque> bibliotheques = List.of(biblio);

        when(bibliothequeRepo.findByNom("BNF Paris")).thenReturn(bibliotheques);

        // Act
        List<Bibliotheque> result = bibliothequeService.getBibliothequesByNom("BNF Paris");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("BNF Paris");
        verify(bibliothequeRepo, times(1)).findByNom("BNF Paris");
    }

    @Test
    void testGetBibliothequesByNomStartWithIgnoreCase() {
        // Arrange
        Bibliotheque biblio1 = new Bibliotheque(1, "BNF Paris", "Paris", "11 rue de Richelieu", "75002", "09:00-22:00", null, null, null, null);
        Bibliotheque biblio2 = new Bibliotheque(3, "BNF Toulouse", "Toulouse", "10 rue Metz", "31000", "08:00-20:00", null, null, null, null);
        List<Bibliotheque> bibliotheques = List.of(biblio1, biblio2);

        when(bibliothequeRepo.findByNomStartsWithIgnoreCase("bnf")).thenReturn(bibliotheques);

        // Act
        List<Bibliotheque> result = bibliothequeService.getBibliothequesByNomStartWithIgnoreCase("bnf");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Bibliotheque::getNom).contains("BNF Paris", "BNF Toulouse");
        verify(bibliothequeRepo, times(1)).findByNomStartsWithIgnoreCase("bnf");
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        // Arrange
        Bibliotheque biblio = new Bibliotheque(2, "Bibliothèque Municipale Lyon", "Lyon", "30 boulevard Vivier Merle", "69003", "09:00-20:00", null, null, null, null);
        List<Bibliotheque> bibliotheques = List.of(biblio);

        when(bibliothequeRepo.findByNomContainingIgnoreCase("municipal")).thenReturn(bibliotheques);

        // Act
        List<Bibliotheque> result = bibliothequeService.getByNomContainingIgnoreCase("municipal");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Bibliothèque Municipale Lyon");
        verify(bibliothequeRepo, times(1)).findByNomContainingIgnoreCase("municipal");
    }
}