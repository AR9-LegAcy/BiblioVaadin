package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.TypeDocumentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = {"spring.flyway.enabled=false"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class TypeDocumentServiceTest {

    @Autowired
    private TypeDocumentService typeDocumentService;

    @Autowired
    private TypeDocumentRepo typeDocumentRepo;

    private TypeDocument type1;
    private TypeDocument type2;

    @BeforeAll
    void setUp() {

        type1 = typeDocumentRepo.save(
                new TypeDocument(
                        null,
                        "Livre",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null
                ));

        type2 = typeDocumentRepo.save(
                new TypeDocument(
                        null,
                        "Magazine",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null
                ));
    }

    @AfterAll
    void tearDown() {
        typeDocumentRepo.deleteAll(List.of(type1, type2));
    }

    @Test
    void testGetAllTypeDocuments() {

        List<TypeDocument> result =
                typeDocumentService.getAllTypeDocuments();

        assertTrue(result.size() >= 2);
    }

    @Test
    void testGetTypeDocumentById() {

        TypeDocument result =
                typeDocumentService.getTypeDocumentById(type1.getIdTypeDocument());

        assertNotNull(result);
        assertEquals("Livre", result.getNomTypeDocument());
    }

    @Test
    void testGetTypeDocumentByIdNotFound() {

        TypeDocument result =
                typeDocumentService.getTypeDocumentById(-999);

        assertNull(result);
    }

    @Test
    void testSaveTypeDocument() {

        TypeDocument type =
                new TypeDocument(
                        null,
                        "DVD",
                        null,
                        null,
                        null
                );

        TypeDocument saved =
                typeDocumentService.saveTypeDocument(type);

        assertNotNull(saved.getIdTypeDocument());
        assertNotNull(saved.getCreatedAtTypeDocument());
        assertNotNull(saved.getUpdatedAtTypeDocument());

        typeDocumentRepo.delete(saved);
    }

    @Test
    void testUpdateTypeDocument() {

        type1.setNomTypeDocument("Livre modifié");

        TypeDocument updated =
                typeDocumentService.updateTypeDocument(type1);

        assertEquals("Livre modifié", updated.getNomTypeDocument());
        assertNotNull(updated.getUpdatedAtTypeDocument());
    }

    @Test
    void testDeleteTypeDocumentById() {

        TypeDocument temp =
                typeDocumentRepo.save(
                        new TypeDocument(
                                null,
                                "Temp",
                                null,
                                null,
                                null
                        )
                );

        Integer id = temp.getIdTypeDocument();

        typeDocumentService.deleteTypeDocumentById(id);

        assertTrue(typeDocumentRepo.findById(id).isEmpty());
    }

    @Test
    void testGetTypeDocumentsByNom() {

        List<TypeDocument> result =
                typeDocumentService.getTypeDocumentsByNomTypeDocument("Livre");

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetTypeDocumentsByNomEmpty() {

        List<TypeDocument> result =
                typeDocumentService.getTypeDocumentsByNomTypeDocument("Inexistant");

        assertTrue(result.isEmpty());
    }
}