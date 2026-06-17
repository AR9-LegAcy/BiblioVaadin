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

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.TypeDocumentRepo;

@ExtendWith(MockitoExtension.class)
public class TypeDocumentServiceMockTest {

    @Mock
    private TypeDocumentRepo typeDocumentRepo;

    @InjectMocks
    private TypeDocumentService typeDocumentService;

    @Test
    void testGetAllTypeDocuments() {
        // Arrange
        TypeDocument td1 = new TypeDocument(
                1,
                "Livre",
                null,
                null,
                null);

        TypeDocument td2 = new TypeDocument(
                2,
                "Magazine",
                null,
                null,
                null);

        List<TypeDocument> typeDocuments = List.of(td1, td2);

        when(typeDocumentRepo.findAll(any(Sort.class)))
                .thenReturn(typeDocuments);

        // Act
        List<TypeDocument> result =
                typeDocumentService.getAllTypeDocuments();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(td1, td2);

        verify(typeDocumentRepo, times(1))
                .findAll(any(Sort.class));
    }

    @Test
    void testGetTypeDocumentById_Found() {
        // Arrange
        TypeDocument td = new TypeDocument(
                1,
                "Livre",
                null,
                null,
                null);

        when(typeDocumentRepo.findById(1))
                .thenReturn(Optional.of(td));

        // Act
        TypeDocument result =
                typeDocumentService.getTypeDocumentById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdTypeDocument()).isEqualTo(1);
        assertThat(result.getNomTypeDocument()).isEqualTo("Livre");

        verify(typeDocumentRepo, times(1))
                .findById(1);
    }

    @Test
    void testGetTypeDocumentById_NotFound() {
        // Arrange
        when(typeDocumentRepo.findById(999))
                .thenReturn(Optional.empty());

        // Act
        TypeDocument result =
                typeDocumentService.getTypeDocumentById(999);

        // Assert
        assertThat(result).isNull();

        verify(typeDocumentRepo, times(1))
                .findById(999);
    }

    @Test
    void testSaveTypeDocument() {
        // Arrange
        TypeDocument td = new TypeDocument();
        td.setIdTypeDocument(1);
        td.setNomTypeDocument("Livre");

        TypeDocument savedTd = new TypeDocument(
                1,
                "Livre",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);

        when(typeDocumentRepo.save(any(TypeDocument.class)))
                .thenReturn(savedTd);

        // Act
        TypeDocument result =
                typeDocumentService.saveTypeDocument(td);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdTypeDocument()).isEqualTo(1);
        assertThat(result.getCreatedAtTypeDocument()).isNotNull();
        assertThat(result.getUpdatedAtTypeDocument()).isNotNull();

        verify(typeDocumentRepo, times(1))
                .save(any(TypeDocument.class));
    }

    @Test
    void testSaveTypeDocument_WithExistingCreatedAt() {
        // Arrange
        LocalDateTime createdAt =
                LocalDateTime.of(2025, 1, 1, 10, 0);

        TypeDocument td = new TypeDocument(
                1,
                "Livre",
                createdAt,
                null,
                null);

        TypeDocument savedTd = new TypeDocument(
                1,
                "Livre",
                createdAt,
                LocalDateTime.now(),
                null);

        when(typeDocumentRepo.save(any(TypeDocument.class)))
                .thenReturn(savedTd);

        // Act
        TypeDocument result =
                typeDocumentService.saveTypeDocument(td);

        // Assert
        assertThat(result.getCreatedAtTypeDocument())
                .isEqualTo(createdAt);

        assertThat(result.getUpdatedAtTypeDocument())
                .isNotNull();

        verify(typeDocumentRepo, times(1))
                .save(any(TypeDocument.class));
    }

    @Test
    void testUpdateTypeDocument() {
        // Arrange
        TypeDocument td = new TypeDocument(
                1,
                "Livre modifié",
                LocalDateTime.now().minusDays(5),
                null,
                null);

        TypeDocument updatedTd = new TypeDocument(
                1,
                "Livre modifié",
                td.getCreatedAtTypeDocument(),
                LocalDateTime.now(),
                null);

        when(typeDocumentRepo.save(td))
                .thenReturn(updatedTd);

        // Act
        TypeDocument result =
                typeDocumentService.updateTypeDocument(td);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAtTypeDocument())
                .isNotNull();

        verify(typeDocumentRepo, times(1))
                .save(td);
    }

    @Test
    void testDeleteTypeDocumentById() {
        // Act
        typeDocumentService.deleteTypeDocumentById(1);

        // Assert
        verify(typeDocumentRepo, times(1))
                .deleteById(1);
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocument() {
        // Arrange
        TypeDocument td = new TypeDocument(
                1,
                "Livre",
                null,
                null,
                null);

        when(typeDocumentRepo.findByNomTypeDocument("Livre"))
                .thenReturn(List.of(td));

        // Act
        List<TypeDocument> result =
                typeDocumentService
                        .getTypeDocumentsByNomTypeDocument("Livre");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNomTypeDocument())
                .isEqualTo("Livre");

        verify(typeDocumentRepo, times(1))
                .findByNomTypeDocument("Livre");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentLike() {
        // Arrange
        TypeDocument td = new TypeDocument(
                1,
                "Livre",
                null,
                null,
                null);

        when(typeDocumentRepo.findByNomTypeDocumentLike("%Liv%"))
                .thenReturn(List.of(td));

        // Act
        List<TypeDocument> result =
                typeDocumentService
                        .getTypeDocumentsByNomTypeDocumentLike("%Liv%");

        // Assert
        assertThat(result).hasSize(1);

        verify(typeDocumentRepo, times(1))
                .findByNomTypeDocumentLike("%Liv%");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentStartWithIgnoreCase() {
        // Arrange
        TypeDocument td = new TypeDocument(
                1,
                "Livre",
                null,
                null,
                null);

        when(typeDocumentRepo
                .findByNomTypeDocumentStartsWithIgnoreCase("liv"))
                .thenReturn(List.of(td));

        // Act
        List<TypeDocument> result =
                typeDocumentService
                        .getTypeDocumentsByNomTypeDocumentStartWithIgnoreCase("liv");

        // Assert
        assertThat(result).hasSize(1);

        verify(typeDocumentRepo, times(1))
                .findByNomTypeDocumentStartsWithIgnoreCase("liv");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentContainingIgnoreCase() {
        // Arrange
        TypeDocument td = new TypeDocument(
                1,
                "Livre",
                null,
                null,
                null);

        when(typeDocumentRepo
                .findByNomTypeDocumentContainingIgnoreCase("ivr"))
                .thenReturn(List.of(td));

        // Act
        List<TypeDocument> result =
                typeDocumentService
                        .getTypeDocumentsByNomTypeDocumentContainingIgnoreCase("ivr");

        // Assert
        assertThat(result).hasSize(1);

        verify(typeDocumentRepo, times(1))
                .findByNomTypeDocumentContainingIgnoreCase("ivr");
    }
}