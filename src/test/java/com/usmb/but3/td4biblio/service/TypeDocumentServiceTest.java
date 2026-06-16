package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.TypeDocumentRepo;

@SpringBootTest
class TypeDocumentServiceTest {

    @Mock
    private TypeDocumentRepo typeDocumentRepo;

    @InjectMocks
    private TypeDocumentService typeDocumentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTypeDocuments() {
        TypeDocument typeDoc1 = new TypeDocument(1, "Livre", LocalDateTime.now(), LocalDateTime.now(), null);
        TypeDocument typeDoc2 = new TypeDocument(2, "Magazine", LocalDateTime.now(), LocalDateTime.now(), null);
        when(typeDocumentRepo.findAll(Sort.by(Sort.Direction.ASC, "nomTypeDocument")))
            .thenReturn(Arrays.asList(typeDoc1, typeDoc2));

        List<TypeDocument> typeDocuments = typeDocumentService.getAllTypeDocuments();

        assertEquals(2, typeDocuments.size());
        assertEquals("Livre", typeDocuments.get(0).getNomTypeDocument());
        assertEquals("Magazine", typeDocuments.get(1).getNomTypeDocument());
        verify(typeDocumentRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "nomTypeDocument"));
    }

    @Test
    void testGetTypeDocumentById() {
        TypeDocument typeDocument = new TypeDocument(1, "Livre", LocalDateTime.now(), LocalDateTime.now(), null);
        when(typeDocumentRepo.findById(1)).thenReturn(Optional.of(typeDocument));

        TypeDocument result = typeDocumentService.getTypeDocumentById(1);

        assertNotNull(result);
        assertEquals(1, result.getIdTypeDocument());
        assertEquals("Livre", result.getNomTypeDocument());
        verify(typeDocumentRepo, times(1)).findById(1);
    }

    @Test
    void testGetTypeDocumentByIdNotFound() {
        when(typeDocumentRepo.findById(1)).thenReturn(Optional.empty());

        TypeDocument result = typeDocumentService.getTypeDocumentById(1);

        assertNull(result);
        verify(typeDocumentRepo, times(1)).findById(1);
    }

    @Test
    void testSaveTypeDocument() {
        TypeDocument typeDocument = new TypeDocument(null, "DVD", null, null, null);
        TypeDocument savedTypeDocument = new TypeDocument(1, "DVD", LocalDateTime.now(), LocalDateTime.now(), null);
        when(typeDocumentRepo.save(any(TypeDocument.class))).thenReturn(savedTypeDocument);

        TypeDocument result = typeDocumentService.saveTypeDocument(typeDocument);

        assertNotNull(result);
        assertEquals(1, result.getIdTypeDocument());
        assertEquals("DVD", result.getNomTypeDocument());
        assertNotNull(result.getCreatedAtTypeDocument());
        assertNotNull(result.getUpdatedAtTypeDocument());
        verify(typeDocumentRepo, times(1)).save(any(TypeDocument.class));
    }

    @Test
    void testUpdateTypeDocument() {
        TypeDocument typeDocument = new TypeDocument(1, "Audiobook", LocalDateTime.now(), LocalDateTime.now(), null);
        when(typeDocumentRepo.save(any(TypeDocument.class))).thenReturn(typeDocument);

        TypeDocument result = typeDocumentService.updateTypeDocument(typeDocument);

        assertNotNull(result);
        assertEquals(1, result.getIdTypeDocument());
        assertEquals("Audiobook", result.getNomTypeDocument());
        assertNotNull(result.getUpdatedAtTypeDocument());
        verify(typeDocumentRepo, times(1)).save(any(TypeDocument.class));
    }

    @Test
    void testDeleteTypeDocumentById() {
        doNothing().when(typeDocumentRepo).deleteById(1);

        typeDocumentService.deleteTypeDocumentById(1);

        verify(typeDocumentRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocument() {
        TypeDocument typeDocument = new TypeDocument(1, "Livre", LocalDateTime.now(), LocalDateTime.now(), null);
        when(typeDocumentRepo.findByNomTypeDocument("Livre")).thenReturn(Arrays.asList(typeDocument));

        List<TypeDocument> typeDocuments = typeDocumentService.getTypeDocumentsByNomTypeDocument("Livre");

        assertEquals(1, typeDocuments.size());
        assertEquals("Livre", typeDocuments.get(0).getNomTypeDocument());
        verify(typeDocumentRepo, times(1)).findByNomTypeDocument("Livre");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentLike() {
        TypeDocument typeDoc1 = new TypeDocument(1, "Livre", LocalDateTime.now(), LocalDateTime.now(), null);
        TypeDocument typeDoc2 = new TypeDocument(2, "E-Livre", LocalDateTime.now(), LocalDateTime.now(), null);
        when(typeDocumentRepo.findByNomTypeDocumentLike("%Livre%")).thenReturn(Arrays.asList(typeDoc1, typeDoc2));

        List<TypeDocument> typeDocuments = typeDocumentService.getTypeDocumentsByNomTypeDocumentLike("%Livre%");

        assertEquals(2, typeDocuments.size());
        verify(typeDocumentRepo, times(1)).findByNomTypeDocumentLike("%Livre%");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentStartWithIgnoreCase() {
        TypeDocument typeDocument = new TypeDocument(1, "Livre", LocalDateTime.now(), LocalDateTime.now(), null);
        when(typeDocumentRepo.findByNomTypeDocumentStartsWithIgnoreCase("Liv")).thenReturn(Arrays.asList(typeDocument));

        List<TypeDocument> typeDocuments = typeDocumentService.getTypeDocumentsByNomTypeDocumentStartWithIgnoreCase("Liv");

        assertEquals(1, typeDocuments.size());
        verify(typeDocumentRepo, times(1)).findByNomTypeDocumentStartsWithIgnoreCase("Liv");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentContainingIgnoreCase() {
        TypeDocument typeDoc1 = new TypeDocument(1, "Livre", LocalDateTime.now(), LocalDateTime.now(), null);
        TypeDocument typeDoc2 = new TypeDocument(2, "E-Livre", LocalDateTime.now(), LocalDateTime.now(), null);
        when(typeDocumentRepo.findByNomTypeDocumentContainingIgnoreCase("livre")).thenReturn(Arrays.asList(typeDoc1, typeDoc2));

        List<TypeDocument> typeDocuments = typeDocumentService.getTypeDocumentsByNomTypeDocumentContainingIgnoreCase("livre");

        assertEquals(2, typeDocuments.size());
        verify(typeDocumentRepo, times(1)).findByNomTypeDocumentContainingIgnoreCase("livre");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentEmpty() {
        when(typeDocumentRepo.findByNomTypeDocument("NonExistent")).thenReturn(Arrays.asList());

        List<TypeDocument> typeDocuments = typeDocumentService.getTypeDocumentsByNomTypeDocument("NonExistent");

        assertEquals(0, typeDocuments.size());
        verify(typeDocumentRepo, times(1)).findByNomTypeDocument("NonExistent");
    }

    @Test
    void testSaveTypeDocumentWithCreatedAtAlreadySet() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        TypeDocument typeDocument = new TypeDocument(null, "Magazine", createdAt, null, null);
        TypeDocument savedTypeDocument = new TypeDocument(1, "Magazine", createdAt, LocalDateTime.now(), null);
        when(typeDocumentRepo.save(any(TypeDocument.class))).thenReturn(savedTypeDocument);

        TypeDocument result = typeDocumentService.saveTypeDocument(typeDocument);

        assertNotNull(result);
        assertEquals(createdAt, result.getCreatedAtTypeDocument());
        assertNotNull(result.getUpdatedAtTypeDocument());
        verify(typeDocumentRepo, times(1)).save(any(TypeDocument.class));
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentLikeEmpty() {
        when(typeDocumentRepo.findByNomTypeDocumentLike("%NonExistent%")).thenReturn(Arrays.asList());

        List<TypeDocument> typeDocuments = typeDocumentService.getTypeDocumentsByNomTypeDocumentLike("%NonExistent%");

        assertEquals(0, typeDocuments.size());
        verify(typeDocumentRepo, times(1)).findByNomTypeDocumentLike("%NonExistent%");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentStartWithIgnoreCaseEmpty() {
        when(typeDocumentRepo.findByNomTypeDocumentStartsWithIgnoreCase("XYZ")).thenReturn(Arrays.asList());

        List<TypeDocument> typeDocuments = typeDocumentService.getTypeDocumentsByNomTypeDocumentStartWithIgnoreCase("XYZ");

        assertEquals(0, typeDocuments.size());
        verify(typeDocumentRepo, times(1)).findByNomTypeDocumentStartsWithIgnoreCase("XYZ");
    }

    @Test
    void testGetTypeDocumentsByNomTypeDocumentContainingIgnoreCaseEmpty() {
        when(typeDocumentRepo.findByNomTypeDocumentContainingIgnoreCase("xyz")).thenReturn(Arrays.asList());

        List<TypeDocument> typeDocuments = typeDocumentService.getTypeDocumentsByNomTypeDocumentContainingIgnoreCase("xyz");

        assertEquals(0, typeDocuments.size());
        verify(typeDocumentRepo, times(1)).findByNomTypeDocumentContainingIgnoreCase("xyz");
    }
}