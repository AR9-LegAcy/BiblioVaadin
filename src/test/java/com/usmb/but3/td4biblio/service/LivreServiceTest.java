package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EditeurRepo;
import com.usmb.but3.td4biblio.repository.LivreRepo;
import com.usmb.but3.td4biblio.repository.TypeDocumentRepo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class LivreServiceTest {

    @Autowired
    private LivreService livreService;

    @Autowired
    private LivreRepo livreRepo;

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private TypeDocumentRepo typeDocumentRepo;

    @Autowired
    private EditeurRepo editeurRepo;

    private TypeDocument typeDocument;
    private Editeur editeur;
    private Document document1;
    private Document document2;

    private Livre livre1;
    private Livre livre2;

    @BeforeAll
    void setUp() {

        // =====================
        // TYPE DOCUMENT
        // =====================
        typeDocument = typeDocumentRepo.save(new TypeDocument(
                null,
                "Manga",
                null,
                null,
                null
        ));

        // =====================
        // EDITEUR
        // =====================
        editeur = editeurRepo.save(new Editeur(
                null,
                "Editeur Test",
                "Paris",
                null,
                null,
                null,
                null,
                null
        ));

        // =====================
        // DOCUMENTS (OBLIGATOIRE)
        // =====================
        document1 = documentRepo.save(new Document(
                null,
                null,
                "Doc Livre 1",
                "PDF",
                LocalDate.now(),
                "A1",
                "ISBN-001",
                true,
                "BON",
                LocalDateTime.now(),
                LocalDateTime.now(),
                typeDocument,
                null,
                null
        ));

        document2 = documentRepo.save(new Document(
                null,
                null,
                "Doc Livre 2",
                "PDF",
                LocalDate.now(),
                "A2",
                "ISBN-002",
                true,
                "BON",
                LocalDateTime.now(),
                LocalDateTime.now(),
                typeDocument,
                null,
                null
        ));

        // =====================
        // LIVRES
        // =====================
        livre1 = livreRepo.save(new Livre(
                null,
                "Titre 1",
                200,
                LocalDate.of(2020, 1, 1),
                LocalDateTime.now(),
                LocalDateTime.now(),
                editeur,
                document1
        ));

        livre2 = livreRepo.save(new Livre(
                null,
                "Titre 2",
                300,
                LocalDate.of(2021, 2, 2),
                LocalDateTime.now(),
                LocalDateTime.now(),
                editeur,
                document2
        ));
    }

    @AfterAll
    void tearDown() {
        livreRepo.deleteAll(List.of(livre1, livre2));
        documentRepo.deleteAll(List.of(document1, document2));
        editeurRepo.delete(editeur);
        typeDocumentRepo.delete(typeDocument);
    }

    // =====================
    // TESTS
    // =====================

    @Test
    void testGetAllLivres() {
        List<Livre> result = livreService.getAllLivres();

        assertTrue(result.size() >= 3);
    }

    @Test
    void testGetById() {
        Livre result = livreService.getLivreById(livre1.getIdDocument());

        assertNotNull(result);
        assertEquals("Titre 1", result.getTitreLivre());
    }

    @Test
    void testGetById_NotFound() {
        Livre result = livreService.getLivreById(-999);

        assertNull(result);
    }

    @Test
    void testSaveLivre() {
        Livre l = new Livre(
                null,
                "Test Livre",
                123,
                LocalDate.of(2023, 1, 1),
                null,
                null,
                editeur,
                document1
        );

        Livre saved = livreService.saveLivre(l);

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

        livreRepo.delete(saved);
    }

    @Test
    void testUpdateLivre() {
        livre1.setTitreLivre("Java Spring Boot");

        Livre updated = livreService.updateLivre(livre1);

        assertEquals("Java Spring Boot", updated.getTitreLivre());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteLivre() {
        Livre temp = livreRepo.save(new Livre(
                null,
                "Temp Livre",
                100,
                LocalDate.now(),
                null,
                null,
                editeur,
                document1
        ));

        livreService.deleteLivreById(temp.getIdDocument());

        assertTrue(livreRepo.findById(temp.getIdDocument()).isEmpty());
    }

    @Test
    void testFindByTitreContainingIgnoreCase() {
        List<Livre> result = livreService.getByTitreContainingIgnoreCase("spring");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFiltreEditeur() {
        List<Livre> result = livreRepo.findByIdEditeur(editeur.getId());

        assertEquals(2, result.size());
    }
}