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

import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.repository.TypeEvenementRepo;

@ExtendWith(MockitoExtension.class)
public class TypeEvenementServiceMockTest {

    @Mock
    private TypeEvenementRepo typeEvenementRepo;

    @InjectMocks
    private TypeEvenementService typeEvenementService;

    @Test
    void testGetAllTypeEvenements() {
        // Arrange
        TypeEvenement t1 = new TypeEvenement(1, "Conférence", null, null, null);
        TypeEvenement t2 = new TypeEvenement(2, "Atelier", null, null, null);

        when(typeEvenementRepo.findAll(any(Sort.class)))
                .thenReturn(List.of(t1, t2));

        // Act
        List<TypeEvenement> result = typeEvenementService.getAllTypeEvenements();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(t1, t2);

        verify(typeEvenementRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetTypeEvenementById_Found() {
        // Arrange
        TypeEvenement type = new TypeEvenement(1, "Conférence", null, null, null);

        when(typeEvenementRepo.findById(1))
                .thenReturn(Optional.of(type));

        // Act
        TypeEvenement result = typeEvenementService.getTypeEvenementById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Conférence");

        verify(typeEvenementRepo, times(1)).findById(1);
    }

    @Test
    void testGetTypeEvenementById_NotFound() {
        // Arrange
        when(typeEvenementRepo.findById(999))
                .thenReturn(Optional.empty());

        // Act
        TypeEvenement result = typeEvenementService.getTypeEvenementById(999);

        // Assert
        assertThat(result).isNull();

        verify(typeEvenementRepo, times(1)).findById(999);
    }

    @Test
    void testSaveTypeEvenement() {
        // Arrange
        TypeEvenement type = new TypeEvenement();
        type.setId(1);
        type.setNom("Conférence");

        TypeEvenement saved = new TypeEvenement(
                1,
                "Conférence",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);

        when(typeEvenementRepo.save(any(TypeEvenement.class)))
                .thenReturn(saved);

        // Act
        TypeEvenement result = typeEvenementService.saveTypeEvenement(type);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(typeEvenementRepo, times(1)).save(any(TypeEvenement.class));
    }

    @Test
    void testSaveTypeEvenement_WithExistingCreatedAt() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 10, 0);

        TypeEvenement type = new TypeEvenement(
                1,
                "Conférence",
                createdAt,
                null,
                null);

        TypeEvenement saved = new TypeEvenement(
                1,
                "Conférence",
                createdAt,
                LocalDateTime.now(),
                null);

        when(typeEvenementRepo.save(any(TypeEvenement.class)))
                .thenReturn(saved);

        // Act
        TypeEvenement result = typeEvenementService.saveTypeEvenement(type);

        // Assert
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(typeEvenementRepo, times(1)).save(any(TypeEvenement.class));
    }

    @Test
    void testUpdateTypeEvenement() {
        // Arrange
        TypeEvenement type = new TypeEvenement(1, "Conférence", null, null, null);

        TypeEvenement updated = new TypeEvenement(
                1,
                "Conférence",
                null,
                LocalDateTime.now(),
                null);

        when(typeEvenementRepo.save(type)).thenReturn(updated);

        // Act
        TypeEvenement result = typeEvenementService.updateTypeEvenement(type);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(typeEvenementRepo, times(1)).save(type);
    }

    @Test
    void testDeleteTypeEvenementById() {
        // Act
        typeEvenementService.deleteTypeEvenementById(1);

        // Assert
        verify(typeEvenementRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetTypeEvenementsByNom() {
        // Arrange
        TypeEvenement type = new TypeEvenement(1, "Conférence", null, null, null);

        when(typeEvenementRepo.findByNom("Conférence"))
                .thenReturn(List.of(type));

        // Act
        List<TypeEvenement> result = typeEvenementService.getTypeEvenementsByNom("Conférence");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Conférence");

        verify(typeEvenementRepo, times(1)).findByNom("Conférence");
    }

    @Test
    void testGetTypeEvenementsByNomLike() {
        // Arrange
        TypeEvenement type = new TypeEvenement(1, "Conférence", null, null, null);

        when(typeEvenementRepo.findByNomLike("%Conf%"))
                .thenReturn(List.of(type));

        // Act
        List<TypeEvenement> result =
                typeEvenementService.getTypeEvenementsByNomLike("%Conf%");

        // Assert
        assertThat(result).hasSize(1);

        verify(typeEvenementRepo, times(1)).findByNomLike("%Conf%");
    }
}