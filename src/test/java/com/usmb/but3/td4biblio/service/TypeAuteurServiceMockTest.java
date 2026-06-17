package com.usmb.but3.td4biblio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;

@ExtendWith(MockitoExtension.class)
public class TypeAuteurServiceMockTest {

    @Mock
    private TypeAuteurRepo typeAuteurRepo;

    @InjectMocks
    private TypeAuteurService typeAuteurService;

    @Test
    void testGetAllTypeAuteurs() {
        // Arrange
        TypeAuteur type1 = new TypeAuteur(
                1,
                "Auteur",
                null,
                null);

        TypeAuteur type2 = new TypeAuteur(
                2,
                "Illustrateur",
                null,
                null);

        List<TypeAuteur> types = List.of(type1, type2);

        when(typeAuteurRepo.findAll(any(Sort.class)))
                .thenReturn(types);

        // Act
        List<TypeAuteur> result =
                typeAuteurService.getAllTypeAuteurs();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(type1, type2);

        verify(typeAuteurRepo, times(1))
                .findAll(any(Sort.class));
    }

    @Test
    void testGetTypeAuteurById_Found() {
        // Arrange
        TypeAuteur typeAuteur = new TypeAuteur(
                1,
                "Auteur",
                null,
                null);

        when(typeAuteurRepo.findById(1))
                .thenReturn(Optional.of(typeAuteur));

        // Act
        TypeAuteur result =
                typeAuteurService.getTypeAuteurById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Auteur");

        verify(typeAuteurRepo, times(1))
                .findById(1);
    }

    @Test
    void testGetTypeAuteurById_NotFound() {
        // Arrange
        when(typeAuteurRepo.findById(999))
                .thenReturn(Optional.empty());

        // Act
        TypeAuteur result =
                typeAuteurService.getTypeAuteurById(999);

        // Assert
        assertThat(result).isNull();

        verify(typeAuteurRepo, times(1))
                .findById(999);
    }

    @Test
    void testSaveTypeAuteur() {
        // Arrange
        TypeAuteur typeAuteur = new TypeAuteur();
        typeAuteur.setId(1);
        typeAuteur.setNom("Auteur");

        TypeAuteur savedTypeAuteur = new TypeAuteur(
                1,
                "Auteur",
                LocalDateTime.now(),
                LocalDateTime.now());

        when(typeAuteurRepo.save(any(TypeAuteur.class)))
                .thenReturn(savedTypeAuteur);

        // Act
        TypeAuteur result =
                typeAuteurService.saveTypeAuteur(typeAuteur);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Auteur");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(typeAuteurRepo, times(1))
                .save(any(TypeAuteur.class));
    }

    @Test
    void testSaveTypeAuteur_WithExistingCreatedAt() {
        // Arrange
        LocalDateTime createdAt =
                LocalDateTime.of(2025, 1, 1, 10, 0);

        TypeAuteur typeAuteur = new TypeAuteur(
                1,
                "Auteur",
                createdAt,
                null);

        TypeAuteur savedTypeAuteur = new TypeAuteur(
                1,
                "Auteur",
                createdAt,
                LocalDateTime.now());

        when(typeAuteurRepo.save(any(TypeAuteur.class)))
                .thenReturn(savedTypeAuteur);

        // Act
        TypeAuteur result =
                typeAuteurService.saveTypeAuteur(typeAuteur);

        // Assert
        assertThat(result.getCreatedAt())
                .isEqualTo(createdAt);

        assertThat(result.getUpdatedAt())
                .isNotNull();

        verify(typeAuteurRepo, times(1))
                .save(any(TypeAuteur.class));
    }

    @Test
    void testUpdateTypeAuteur() {
        // Arrange
        TypeAuteur typeAuteur = new TypeAuteur(
                1,
                "Auteur principal",
                LocalDateTime.now().minusDays(5),
                null);

        TypeAuteur updatedTypeAuteur = new TypeAuteur(
                1,
                "Auteur principal",
                typeAuteur.getCreatedAt(),
                LocalDateTime.now());

        when(typeAuteurRepo.save(typeAuteur))
                .thenReturn(updatedTypeAuteur);

        // Act
        TypeAuteur result =
                typeAuteurService.updateTypeAuteur(typeAuteur);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(typeAuteurRepo, times(1))
                .save(typeAuteur);
    }

    @Test
    void testDeleteTypeAuteurById() {
        // Act
        typeAuteurService.deleteTypeAuteurById(1);

        // Assert
        verify(typeAuteurRepo, times(1))
                .deleteById(1);
    }

    @Test
    void testGetTypeAuteursByNom() {
        // Arrange
        TypeAuteur typeAuteur = new TypeAuteur(
                1,
                "Auteur",
                null,
                null);

        when(typeAuteurRepo.findByNom("Auteur"))
                .thenReturn(List.of(typeAuteur));

        // Act
        List<TypeAuteur> result =
                typeAuteurService.getTypeAuteursByNom("Auteur");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom())
                .isEqualTo("Auteur");

        verify(typeAuteurRepo, times(1))
                .findByNom("Auteur");
    }

    @Test
    void testGetTypeAuteursByNomLike() {
        // Arrange
        TypeAuteur typeAuteur = new TypeAuteur(
                1,
                "Auteur",
                null,
                null);

        when(typeAuteurRepo.findByNomLike("%Aut%"))
                .thenReturn(List.of(typeAuteur));

        // Act
        List<TypeAuteur> result =
                typeAuteurService.getTypeAuteursByNomLike("%Aut%");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom())
                .isEqualTo("Auteur");

        verify(typeAuteurRepo, times(1))
                .findByNomLike("%Aut%");
    }
}