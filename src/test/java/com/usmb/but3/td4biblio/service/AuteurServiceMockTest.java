package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.repository.AuteurRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuteurServiceMockTest {

    @Mock
    private AuteurRepo auteurRepo;

    @InjectMocks
    private AuteurService auteurService;

    @Test
    void testGetAllAuteurs() {
        // Arrange
        Auteur auteur1 = new Auteur(1, "Dumas", "Alexandre", LocalDate.of(1802, 7, 24), null, "France", "Paris", "Français", null, null, null);
        Auteur auteur2 = new Auteur(2, "Hugo", "Victor", LocalDate.of(1802, 2, 26), null, "France", "Besançon", "Français", null, null, null);
        List<Auteur> auteurs = List.of(auteur1, auteur2);

        when(auteurRepo.findAll(any(Sort.class))).thenReturn(auteurs);

        // Act
        List<Auteur> result = auteurService.getAllAuteurs();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(auteur1, auteur2);
        verify(auteurRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetAuteurById_Found() {
        // Arrange
        Auteur auteur = new Auteur(1, "Dumas", "Alexandre", LocalDate.of(1802, 7, 24), null, "France", "Paris", "Français", null, null, null);
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));

        // Act
        Auteur result = auteurService.getAuteurById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Dumas");
        verify(auteurRepo, times(1)).findById(1);
    }

    @Test
    void testGetAuteurById_NotFound() {
        // Arrange
        when(auteurRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Auteur result = auteurService.getAuteurById(999);

        // Assert
        assertThat(result).isNull();
        verify(auteurRepo, times(1)).findById(999);
    }

    @Test
    void testSaveAuteur() {
        // Arrange
        Auteur auteur = new Auteur();
        auteur.setNom("Balzac");
        auteur.setPrenom("Honoré de");
        auteur.setDateNaissance(LocalDate.of(1799, 5, 20));

        Auteur savedAuteur = new Auteur();
        savedAuteur.setId(3);
        savedAuteur.setNom("Balzac");
        savedAuteur.setPrenom("Honoré de");
        savedAuteur.setDateNaissance(LocalDate.of(1799, 5, 20));
        savedAuteur.setCreatedAt(LocalDateTime.now());
        savedAuteur.setUpdatedAt(LocalDateTime.now());

        when(auteurRepo.save(any(Auteur.class))).thenReturn(savedAuteur);

        // Act
        Auteur result = auteurService.saveAuteur(auteur);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(auteurRepo, times(1)).save(any(Auteur.class));
    }

    @Test
    void testUpdateAuteur() {
        // Arrange
        Auteur auteur = new Auteur(1, "Dumas", "Alexandre", LocalDate.of(1802, 7, 24), null, "France", "Paris", "Français", null, null, null);
        auteur.setUpdatedAt(LocalDateTime.now());

        when(auteurRepo.save(auteur)).thenReturn(auteur);

        // Act
        Auteur result = auteurService.updateAuteur(auteur);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(auteurRepo, times(1)).save(auteur);
    }

    @Test
    void testDeleteAuteurById() {
        // Act
        auteurService.deleteAuteurById(1);

        // Assert
        verify(auteurRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetAuteursByNom() {
        // Arrange
        Auteur auteur = new Auteur(1, "Dumas", "Alexandre", null, null, "France", null, "Français", null, null, null);
        List<Auteur> auteurs = List.of(auteur);

        when(auteurRepo.findByNom("Dumas")).thenReturn(auteurs);

        // Act
        List<Auteur> result = auteurService.getAuteursByNom("Dumas");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dumas");
        verify(auteurRepo, times(1)).findByNom("Dumas");
    }

    @Test
    void testGetAuteursByNomAndPrenom() {
        // Arrange
        Auteur auteur = new Auteur(1, "Dumas", "Alexandre", null, null, "France", null, "Français", null, null, null);
        List<Auteur> auteurs = List.of(auteur);

        when(auteurRepo.findByNomAndPrenom("Dumas", "Alexandre")).thenReturn(auteurs);

        // Act
        List<Auteur> result = auteurService.getAuteursByNomAndPrenom("Dumas", "Alexandre");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dumas");
        assertThat(result.get(0).getPrenom()).isEqualTo("Alexandre");
        verify(auteurRepo, times(1)).findByNomAndPrenom("Dumas", "Alexandre");
    }

    @Test
    void testGetAuteursByNomContainingIgnoreCase() {
        // Arrange
        Auteur auteur = new Auteur(1, "Dumas", "Alexandre", null, null, "France", null, "Français", null, null, null);
        List<Auteur> auteurs = List.of(auteur);

        when(auteurRepo.findByNomContainingIgnoreCase("dum")).thenReturn(auteurs);

        // Act
        List<Auteur> result = auteurService.getByNomContainingIgnoreCase("dum");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dumas");
        verify(auteurRepo, times(1)).findByNomContainingIgnoreCase("dum");
    }
}