package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.repository.*;

@SpringBootTest(properties = {"spring.flyway.enabled=false"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class EmprunterServiceTest {

    @Autowired
    private EmprunterService emprunterService;

    @Autowired
    private EmprunterRepo emprunterRepo;

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private EmprunteurRepo emprunteurRepo;

    @Autowired
    private TypeDocumentRepo typeDocumentRepo;

    private TypeDocument typeManga;
    private Document doc1;
    private Document doc2;
    private Emprunteur emp1;
    private Emprunteur emp2;

    @BeforeAll
    void setUp() {

        // =====================
        // TYPE DOCUMENT (MANGA)
        // =====================
        typeManga = typeDocumentRepo.save(
                new TypeDocument(null, "Manga",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null)
        );

        // =====================
        // DOCUMENTS
        // =====================
        doc1 = documentRepo.save(
                new Document(
                        null,
                        "gif1.jpg",
                        "Manga test 1",
                        "A4",
                        LocalDate.of(2024, 1, 1),
                        "EMP1",
                        "ISBN-111",
                        true,
                        "Bon",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        typeManga,
                        null,
                        null
                )
        );

        doc2 = documentRepo.save(
                new Document(
                        null,
                        "gif2.jpg",
                        "Manga test 2",
                        "A5",
                        LocalDate.of(2024, 2, 1),
                        "EMP2",
                        "ISBN-222",
                        true,
                        "Bon",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        typeManga,
                        null,
                        null
                )
        );

        // =====================
        // EMPRUNTEURS
        // =====================
        emp1 = emprunteurRepo.save(new Emprunteur(
                1,
                "Test",
                "User1",
                "Rue 1",
                "Chambery",
                "73000",
                "user1@mail.com",
                LocalDate.of(2000, 1, 1),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                "pwd",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        emp2 = emprunteurRepo.save(new Emprunteur(
                2,
                "Test",
                "User2",
                "Rue 2",
                "Lyon",
                "69000",
                "user2@mail.com",
                LocalDate.of(1999, 1, 1),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                "pwd",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));
    }

    @AfterAll
    void tearDown() {
        emprunterRepo.deleteAll();
        documentRepo.deleteAll(List.of(doc1, doc2));
        emprunteurRepo.deleteAll(List.of(emp1, emp2));
        typeDocumentRepo.delete(typeManga);
    }

    // =====================
    // TESTS
    // =====================

    @Test
    void testGetAllEmprunts() {
        Emprunter temp = emprunterRepo.save(new Emprunter(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                null,
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                doc1,
                emp1
        ));

        List<Emprunter> result = emprunterService.getAllEmprunts();

        assertTrue(result.size() >= 1);
    }

    @Test
    void testGetEmpruntById() {
        Emprunter temp = emprunterRepo.save(new Emprunter(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                null,
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                doc1,
                emp1
        ));

        EmprunterId id = new EmprunterId(
                doc1.getIdDocument(),
                emp1.getCarteEmprunteur()
        );

        Emprunter result = emprunterService.getEmpruntById(id);

        assertNotNull(result);
    }

    @Test
    void testSaveEmprunt() {
        Emprunter emprunt = new Emprunter(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                null,
                0,
                null,
                null,
                doc1,
                emp1
        );

        Emprunter saved = emprunterService.saveEmprunt(emprunt);

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void testUpdateEmprunt() {
        Emprunter temp = emprunterRepo.save(new Emprunter(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                null,
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                doc1,
                emp1
        ));

        temp.setDateRetourReelle(LocalDate.now());

        Emprunter updated = emprunterService.updateEmprunt(temp);

        assertNotNull(updated.getUpdatedAt());
        assertNotNull(updated.getDateRetourReelle());
    }

    @Test
    void testDeleteEmprunt() {
        Emprunter temp = emprunterRepo.save(new Emprunter(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                null,
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                doc1,
                emp1
        ));

        EmprunterId id = new EmprunterId(
                doc1.getIdDocument(),
                emp1.getCarteEmprunteur()
        );

        emprunterService.deleteEmpruntById(id);

        assertTrue(emprunterRepo.findById(id).isEmpty());
    }

    @Test
    void testGetDocumentById() {
        Document result = emprunterService.getDocumentById(doc1.getIdDocument());

        assertNotNull(result);
    }

    @Test
    void testGetEmprunteurById() {
        Emprunteur result = emprunterService.getEmprunteurById(emp1.getCarteEmprunteur());

        assertNotNull(result);
    }

    @Test
    void testGetEmpruntsByCarteEmprunteur() {
        emprunterRepo.save(new Emprunter(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                null,
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                doc1,
                emp1
        ));

        List<Emprunter> result = emprunterService.getEmpruntsByCarteEmprunteur(emp1.getCarteEmprunteur());

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetEmpruntsByIdDocument() {
        emprunterRepo.save(new Emprunter(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                null,
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                doc1,
                emp1
        ));

        List<Emprunter> result = emprunterService.getEmpruntsByIdDocument(doc1.getIdDocument());

        assertFalse(result.isEmpty());
    }
}