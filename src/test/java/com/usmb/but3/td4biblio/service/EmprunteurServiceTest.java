package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class EmprunteurServiceTest {

    @Autowired
    private EmprunteurService emprunteurService;

    @Autowired
    private EmprunteurRepo emprunteurRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =====================
    // DATASET
    // =====================
    private Emprunteur EMP1;
    private Emprunteur EMP2;
    private Emprunteur EMP3;

    @BeforeAll
    void setUp() {

        EMP1 = emprunteurRepo.save(new Emprunteur(
                10001,
                "Dupont",
                "Jean",
                "Rue A",
                "Chambéry",
                "73000",
                "emp1@test.com",
                LocalDate.of(1990, 1, 1),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                passwordEncoder.encode("01011990"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        EMP2 = emprunteurRepo.save(new Emprunteur(
                10002,
                "Martin",
                "Sophie",
                "Rue B",
                "Grenoble",
                "38000",
                "emp2@test.com",
                LocalDate.of(1995, 5, 10),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                passwordEncoder.encode("10051995"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        EMP3 = emprunteurRepo.save(new Emprunteur(
                10003,
                "Durand",
                "Alice",
                "Rue C",
                "Lyon",
                "69000",
                "emp3@test.com",
                LocalDate.of(2000, 3, 20),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                passwordEncoder.encode("20032000"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));
    }

    @AfterAll
    void tearDown() {
        emprunteurRepo.deleteAll(List.of(EMP1, EMP2, EMP3));
    }

    // =====================
    // TESTS
    // =====================

    @Test
    void testGetAllEmprunteurs() {
        List<Emprunteur> result = emprunteurService.getAllEmprunteurs();

        assertTrue(result.size() >= 3);
    }

    @Test
    void testGetEmprunteurById() {
        Emprunteur result = emprunteurService.getEmprunteurById(EMP1.getCarteEmprunteur());

        assertNotNull(result);
        assertEquals("Dupont", result.getNom());
    }

    @Test
    void testSaveEmprunteur() {

        Emprunteur e = new Emprunteur(
                2000,
                "Test",
                "User",
                "Rue Test",
                "Paris",
                "75000",
                "test@test.com",
                LocalDate.of(1999, 1, 1),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                null,
                null,
                null,
                null
        );

        Emprunteur saved = emprunteurService.saveEmprunteur(e);

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertNotNull(saved.getMotDePasse());

        // ✔️ vérification réelle du password encoder
        assertTrue(passwordEncoder.matches(
                "01011999",
                saved.getMotDePasse()
        ));

        emprunteurRepo.delete(saved);
    }

    @Test
    void testUpdateEmprunteur() {

        EMP1.setNom("DupontModifié");

        Emprunteur updated = emprunteurService.updateEmprunteur(EMP1);

        assertEquals("DupontModifié", updated.getNom());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteEmprunteur() {

        Emprunteur temp = emprunteurRepo.save(new Emprunteur(
                3000,
                "Temp",
                "Delete",
                "Rue",
                "Ville",
                "00000",
                "temp@test.com",
                LocalDate.of(1990, 1, 1),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                passwordEncoder.encode("test"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        emprunteurService.deleteEmprunteurById(temp.getCarteEmprunteur());

        assertTrue(emprunteurRepo.findById(temp.getCarteEmprunteur()).isEmpty());
    }

    @Test
    void testFindByNom() {
        List<Emprunteur> result = emprunteurService.getEmprunteursByNom("Dupont");

        assertFalse(result.isEmpty());
    }
}