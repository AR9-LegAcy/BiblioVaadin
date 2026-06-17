package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.repository.EditeurRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditeurServiceMockTest {

    @Mock
    private EditeurRepo editeurRepo;

    @InjectMocks
    private EditeurService editeurService;

    @Test
    void testGetAllEditeurs() {
        // Arrange
        Editeur editeur1 = new Editeur(1, "Éditions Gallimard", "5 rue Gaston-Gallimard, Paris", "https://www.gallimard.fr", "https://fr.wikipedia.org/wiki/Gallimard", null, null, null);
        Editeur editeur2 = new Editeur(2, "Éditions Grasset", "61 rue des Saints-Pères, Paris", "https://www.grasset.fr", "https://fr.wikipedia.org/wiki/Grasset", null, null, null);
        List<Editeur> editeurs = List.of(editeur1, editeur2);

        when(editeurRepo.findAll(any(Sort.class))).thenReturn(editeurs);

        // Act
        List<Editeur> result = editeurService.getAllEditeurs();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(editeur1, editeur2);
        verify(editeurRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetEditeurById_Found() {
        // Arrange
        Editeur editeur = new Editeur(1, "Éditions Gallimard", "5 rue Gaston-Gallimard, Paris", "https://www.gallimard.fr", "https://fr.wikipedia.org/wiki/Gallimard", null, null, null);
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));

        // Act
        Editeur result = editeurService.getEditeurById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Éditions Gallimard");
        assertThat(result.getAdresse()).isEqualTo("5 rue Gaston-Gallimard, Paris");
        verify(editeurRepo, times(1)).findById(1);
    }

    @Test
    void testGetEditeurById_NotFound() {
        // Arrange
        when(editeurRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Editeur result = editeurService.getEditeurById(999);

        // Assert
        assertThat(result).isNull();
        verify(editeurRepo, times(1)).findById(999);
    }

    @Test
    void testSaveEditeur() {
        // Arrange
        Editeur editeur = new Editeur();
        editeur.setNom("Éditions Albin Michel");
        editeur.setAdresse("22 rue Huyghens, Paris");
        editeur.setSiteWeb("https://www.albin-michel.fr");
        editeur.setWikipedia("https://fr.wikipedia.org/wiki/Albin_Michel");

        Editeur savedEditeur = new Editeur(3, "Éditions Albin Michel", "22 rue Huyghens, Paris", "https://www.albin-michel.fr", "https://fr.wikipedia.org/wiki/Albin_Michel", LocalDateTime.now(), LocalDateTime.now(), null);

        when(editeurRepo.save(any(Editeur.class))).thenReturn(savedEditeur);

        // Act
        Editeur result = editeurService.saveEditeur(editeur);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3);
        assertThat(result.getNom()).isEqualTo("Éditions Albin Michel");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(editeurRepo, times(1)).save(any(Editeur.class));
    }

    @Test
    void testSaveEditeur_WithExistingCreatedAt() {
        // Arrange
        LocalDateTime existingCreatedAt = LocalDateTime.of(2020, 1, 15, 10, 30, 0);
        Editeur editeur = new Editeur();
        editeur.setNom("Éditions du Seuil");
        editeur.setAdresse("27 rue Jacob, Paris");
        editeur.setCreatedAt(existingCreatedAt);

        Editeur savedEditeur = new Editeur(4, "Éditions du Seuil", "27 rue Jacob, Paris", null, null, existingCreatedAt, LocalDateTime.now(), null);

        when(editeurRepo.save(any(Editeur.class))).thenReturn(savedEditeur);

        // Act
        Editeur result = editeurService.saveEditeur(editeur);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCreatedAt()).isEqualTo(existingCreatedAt);
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(editeurRepo, times(1)).save(any(Editeur.class));
    }

    @Test
    void testUpdateEditeur() {
        // Arrange
        Editeur editeur = new Editeur(1, "Éditions Gallimard", "5 rue Gaston-Gallimard, Paris", "https://www.gallimard.fr", "https://fr.wikipedia.org/wiki/Gallimard", null, null, null);
        
        Editeur updatedEditeur = new Editeur(1, "Éditions Gallimard", "5 rue Gaston-Gallimard, Paris", "https://www.gallimard.fr", "https://fr.wikipedia.org/wiki/Gallimard", null, LocalDateTime.now(), null);

        when(editeurRepo.save(editeur)).thenReturn(updatedEditeur);

        // Act
        Editeur result = editeurService.updateEditeur(editeur);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(editeurRepo, times(1)).save(editeur);
    }

    @Test
    void testDeleteEditeurById() {
        // Act
        editeurService.deleteEditeurById(1);

        // Assert
        verify(editeurRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetEditeursByNom() {
        // Arrange
        Editeur editeur = new Editeur(1, "Éditions Gallimard", "5 rue Gaston-Gallimard, Paris", "https://www.gallimard.fr", "https://fr.wikipedia.org/wiki/Gallimard", null, null, null);
        List<Editeur> editeurs = List.of(editeur);

        when(editeurRepo.findByNom("Éditions Gallimard")).thenReturn(editeurs);

        // Act
        List<Editeur> result = editeurService.getEditeursByNom("Éditions Gallimard");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Éditions Gallimard");
        verify(editeurRepo, times(1)).findByNom("Éditions Gallimard");
    }

    @Test
    void testGetEditeursByNomStartWithIgnoreCase() {
        // Arrange
        Editeur editeur1 = new Editeur(1, "Éditions Gallimard", "5 rue Gaston-Gallimard, Paris", "https://www.gallimard.fr", "https://fr.wikipedia.org/wiki/Gallimard", null, null, null);
        Editeur editeur2 = new Editeur(2, "Éditions Grasset", "61 rue des Saints-Pères, Paris", "https://www.grasset.fr", "https://fr.wikipedia.org/wiki/Grasset", null, null, null);
        List<Editeur> editeurs = List.of(editeur1, editeur2);

        when(editeurRepo.findByNomStartsWithIgnoreCase("éditions")).thenReturn(editeurs);

        // Act
        List<Editeur> result = editeurService.getEditeursByNomStartWithIgnoreCase("éditions");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Editeur::getNom).contains("Éditions Gallimard", "Éditions Grasset");
        assertThat(result).allMatch(e -> e.getNom().toLowerCase().startsWith("éditions"));
        verify(editeurRepo, times(1)).findByNomStartsWithIgnoreCase("éditions");
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        // Arrange
        Editeur editeur = new Editeur(1, "Éditions Gallimard", "5 rue Gaston-Gallimard, Paris", "https://www.gallimard.fr", "https://fr.wikipedia.org/wiki/Gallimard", null, null, null);
        List<Editeur> editeurs = List.of(editeur);

        when(editeurRepo.findByNomContainingIgnoreCase("gallimard")).thenReturn(editeurs);

        // Act
        List<Editeur> result = editeurService.getByNomContainingIgnoreCase("gallimard");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Éditions Gallimard");
        verify(editeurRepo, times(1)).findByNomContainingIgnoreCase("gallimard");
    }

    @Test
    void testGetByNomContainingIgnoreCase_MultipleResults() {
        // Arrange
        Editeur editeur1 = new Editeur(1, "Éditions Gallimard", "5 rue Gaston-Gallimard, Paris", "https://www.gallimard.fr", "https://fr.wikipedia.org/wiki/Gallimard", null, null, null);
        Editeur editeur2 = new Editeur(5, "Gallimard Jeunesse", "15 rue Rémusat, Paris", "https://gallimardjeunesse.fr", null, null, null, null);
        List<Editeur> editeurs = List.of(editeur1, editeur2);

        when(editeurRepo.findByNomContainingIgnoreCase("gallimard")).thenReturn(editeurs);

        // Act
        List<Editeur> result = editeurService.getByNomContainingIgnoreCase("gallimard");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Editeur::getNom).contains("Éditions Gallimard", "Gallimard Jeunesse");
        assertThat(result).allMatch(e -> e.getNom().toLowerCase().contains("gallimard"));
        verify(editeurRepo, times(1)).findByNomContainingIgnoreCase("gallimard");
    }
}