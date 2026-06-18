package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.TypeDocumentRepo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.usmb.but3.td4biblio.security.SessionManager;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.flyway.enabled=false"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private TypeDocumentRepo typeDocumentRepo;

    // =====================
    // DONNÉES DE TEST
    // =====================
    private TypeDocument MANGA;

    private Document DOC1;
    private Document DOC2;
    private Document DOC3;

    private MockedStatic<SessionManager> sessionMock;

    @BeforeEach
    void setupSession() {

        sessionMock = mockStatic(SessionManager.class);

        Bibliothecaire bib = new Bibliothecaire();
        Bibliotheque b = new Bibliotheque();
        b.setId(1);
        bib.setBibliotheque(b);

        sessionMock.when(SessionManager::getBibliothecaire)
                .thenReturn(bib);
    }

    @BeforeAll
    void setUp() {

        // ⚠️ on ne supprime PAS toute la base

        // 1. créer type document Manga
        MANGA = typeDocumentRepo.save(new TypeDocument(
                null,
                "Manga",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        // 2. créer documents test (MANGA)
        DOC1 = documentRepo.save(new Document(
                null,
                "onepiece.jpg",
                "One Piece - Tome 1",
                "A5",
                LocalDate.of(2020, 1, 10),
                "E001",
                "9781111111111",
                true,
                "Bon",
                LocalDateTime.now(),
                LocalDateTime.now(),
                MANGA,
                null,
                null
        ));

        DOC2 = documentRepo.save(new Document(
                null,
                "naruto.jpg",
                "Naruto - Tome 1",
                "A5",
                LocalDate.of(2021, 3, 15),
                "E002",
                "9782222222222",
                true,
                "Bon",
                LocalDateTime.now(),
                LocalDateTime.now(),
                MANGA,
                null,
                null
        ));

        DOC3 = documentRepo.save(new Document(
                null,
                "bleach.jpg",
                "Bleach - Tome 1",
                "A5",
                LocalDate.of(2022, 5, 20),
                "E003",
                "9783333333333",
                false,
                "Moyen",
                LocalDateTime.now(),
                LocalDateTime.now(),
                MANGA,
                null,
                null
        ));
    }

    @AfterEach
    void tearDownVaadin() {
        sessionMock.close();
    }

    @AfterAll
    void tearDown() {

        // nettoyage uniquement des données test
        documentRepo.deleteAll(List.of(DOC1, DOC2, DOC3));

        typeDocumentRepo.delete(MANGA);
    }

    // =====================
    // TESTS
    // =====================

    @Test
    void testGetAllDocuments() {
        List<Document> result = documentService.getAllDocuments();

        assertTrue(result.size() >= 3);
    }

    @Test
    void testGetDocumentById() {
        Document result = documentService.getDocumentById(DOC1.getIdDocument());

        assertNotNull(result);
        assertEquals("One Piece - Tome 1", result.getDescriptionDocument());
    }

    @Test
    void testGetDocumentById_NotFound() {
        Document result = documentService.getDocumentById(-999);

        assertNull(result);
    }

    @Test
    void testSaveDocument() {
        Document doc = new Document(
                null,
                "test.jpg",
                "Test Manga",
                "A4",
                LocalDate.now(),
                "E999",
                "9789999999999",
                true,
                "Bon",
                null,
                null,
                MANGA,
                null,
                null
        );

        Bibliotheque bib = SessionManager.getBibliothecaire().getBibliotheque();

        Document saved = documentService.saveDocument(doc, bib);

        assertNotNull(saved.getIdDocument());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

        documentRepo.delete(saved);
    }

    @Test
    void testUpdateDocument() {
        DOC1.setEtatDocument("Excellent");

        Document updated = documentService.updateDocument(DOC1);

        assertEquals("Excellent", updated.getEtatDocument());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteDocument() {
        Document temp = documentRepo.save(new Document(
                null,
                "temp.jpg",
                "Temp Doc",
                "A4",
                LocalDate.now(),
                "ETEMP",
                "9780000000000",
                true,
                "Bon",
                LocalDateTime.now(),
                LocalDateTime.now(),
                MANGA,
                null,
                null
        ));

        documentService.deleteDocumentById(temp.getIdDocument());

        assertTrue(documentRepo.findById(temp.getIdDocument()).isEmpty());
    }

    @Test
    void testGetDocumentsByCodeIsbn() {
        List<Document> result = documentService.getDocumentsByCodeIsbn("9781111111111");

        assertEquals(1, result.size());
        assertEquals("One Piece - Tome 1", result.get(0).getDescriptionDocument());
    }

    @Test
    void testGetDocumentsByEmpruntable() {
        List<Document> result = documentService.getDocumentsByEmpruntable(true);

        assertTrue(result.size() >= 2);
    }

    @Test
    void testGetDocumentsByEtat() {
        List<Document> result = documentService.getDocumentsByEtat("Bon");

        assertTrue(result.size() >= 2);
    }
}