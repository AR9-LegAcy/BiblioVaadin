package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.repository.BibliothecaireRepo;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.flyway.enabled=false"})
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class BibliothecaireServiceTest {

    @Autowired
    private BibliothecaireService bibliothecaireService;

    @Autowired
    private BibliothecaireRepo bibliothecaireRepo;

    @Autowired
    private BibliothequeRepo bibliothequeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // TRACKING TEST DATA
    // =========================
    private Bibliothecaire BIB1;
    private Bibliothecaire BIB2;

    private Bibliotheque BIBLIO1;

    // =========================
    // SETUP
    // =========================
    @BeforeAll
    void setUp() {

        // =========================
        // BIBLIOTHEQUE TEST
        // =========================
        BIBLIO1 = bibliothequeRepo.save(new Bibliotheque(
                null,
                "TEST_BIBLIO_PARIS",
                "Paris",
                "Rue Test",
                "75001",
                "9h-18h",
                null,
                null,
                null,
                null
        ));

        // =========================
        // BIBLIOTHECAIRES TEST
        // =========================
        BIB1 = bibliothecaireRepo.save(new Bibliothecaire(
                "test_user1",
                "Martin",
                "Jean",
                "Rue A",
                "Lyon",
                "69000",
                "martin@test.fr",
                LocalDate.of(1990, 1, 1),
                passwordEncoder.encode("rawpassword"),
                null,
                null,
                BIBLIO1
        ));

        BIB2 = bibliothecaireRepo.save(new Bibliothecaire(
                "test_user2",
                "Durand",
                "Paul",
                "Rue B",
                "Grenoble",
                "38000",
                "durand@test.fr",
                LocalDate.of(1985, 5, 10),
                passwordEncoder.encode("rawpassword"),
                null,
                null,
                BIBLIO1
        ));
    }

    // =========================
    // CLEANUP (ONLY TEST DATA)
    // =========================
    @AfterAll
    void tearDown() {
        bibliothecaireRepo.deleteAll(List.of(BIB1, BIB2));
        bibliothecaireRepo.flush();

        bibliothequeRepo.delete(BIBLIO1);
        bibliothequeRepo.flush();
    }

    // =========================
    // TESTS
    // =========================

    @Test
    void testGetAllBibliothecaires() {
        List<Bibliothecaire> result = bibliothecaireService.getAllBibliothecaires();

        assertTrue(result.size() >= 2);
    }

    @Test
    void testGetBibliothecaireByPseudo() {
        Bibliothecaire result = bibliothecaireService.getBibliothecaireByPseudo("test_user1");

        assertNotNull(result);
        assertEquals("Martin", result.getNom());
    }

    @Test
    void testSaveBibliothecaire() {

        Bibliothecaire b = new Bibliothecaire(
                "test_user3",
                "Leroy",
                "Anne",
                "Rue C",
                "Paris",
                "75002",
                "anne@test.fr",
                LocalDate.of(1992, 3, 15),
                null,
                null,
                null,
                BIBLIO1
        );

        Bibliothecaire saved = bibliothecaireService.saveBibliothecaire(b);

        assertNotNull(saved.getMotDePasse());
        assertTrue(passwordEncoder.matches(
                LocalDate.of(1992, 3, 15).format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy")),
                saved.getMotDePasse()
        ));
    }

    @Test
    void testUpdateBibliothecaire() {

        BIB1.setNom("MartinUpdated");

        Bibliothecaire updated = bibliothecaireService.updateBibliothecaire(BIB1);

        assertEquals("MartinUpdated", updated.getNom());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteBibliothecaireByPseudo() {

        bibliothecaireService.deleteBibliothecaireByPseudo("test_user2");

        assertNull(bibliothecaireRepo.findByPseudo("test_user2"));
    }

    @Test
    void testGetBibliothecairesByNom() {

        List<Bibliothecaire> result =
                bibliothecaireService.getBibliothecairesByNom("Martin");

        assertEquals(2, result.size());
    }

    @Test
    void testGetBibliothecairesByNomAndPrenom() {

        List<Bibliothecaire> result =
                bibliothecaireService.getBibliothecairesByNomAndPrenom("Martin", "Jean");

        assertEquals(1, result.size());
    }

    @Test
    void testGetByNomContainingIgnoreCase() {

        List<Bibliothecaire> result =
                bibliothecaireService.getByNomContainingIgnoreCase("mart");

        assertEquals(2, result.size());
    }
}