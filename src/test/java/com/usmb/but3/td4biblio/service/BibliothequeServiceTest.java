package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class BibliothequeServiceTest {

    @Autowired
    private BibliothequeService bibliothequeService;

    @Autowired
    private BibliothequeRepo bibliothequeRepo;

    // =====================
    // DONNÉES DE TEST
    // =====================
    private Bibliotheque BIBLIO1;
    private Bibliotheque BIBLIO2;
    private Bibliotheque BIBLIO3;

    @BeforeAll
    void setUp() {

        // ⚠️ On NE supprime PAS toute la base
        // seulement les données de test si besoin

        BIBLIO1 = bibliothequeRepo.save(new Bibliotheque(
                null,
                "Bibliotheque Test A",
                "Chambéry",
                "Rue de la République",
                "73000",
                "9h-18h",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        ));

        BIBLIO2 = bibliothequeRepo.save(new Bibliotheque(
                null,
                "Bibliotheque Test B",
                "Grenoble",
                "Rue des Alpes",
                "38000",
                "10h-19h",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        ));

        BIBLIO3 = bibliothequeRepo.save(new Bibliotheque(
                null,
                "Bibliotheque Test C",
                "Lyon",
                "Rue Centrale",
                "69000",
                "8h-17h",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        ));
    }

    @AfterAll
    void tearDown() {
        // 🔥 Nettoyage uniquement des données créées par les tests
        bibliothequeRepo.deleteAll(List.of(BIBLIO1, BIBLIO2, BIBLIO3));
    }

    // =====================
    // TESTS
    // =====================

    @Test
    void testGetAllBibliotheques() {
        List<Bibliotheque> result = bibliothequeService.getAllBibliotheques();

        assertTrue(result.size() >= 3);
    }

    @Test
    void testGetBibliothequeById() {
        Bibliotheque result = bibliothequeService.getBibliothequeById(BIBLIO1.getId());

        assertNotNull(result);
        assertEquals("Bibliotheque Test A", result.getNom());
    }

    @Test
    void testGetBibliothequeById_NotFound() {
        Bibliotheque result = bibliothequeService.getBibliothequeById(-999);

        assertNull(result);
    }

    @Test
    void testSaveBibliotheque() {
        Bibliotheque b = new Bibliotheque(
                null,
                "Bibliotheque Save",
                "Paris",
                "Rue Test",
                "75000",
                "9h-18h",
                null,
                null,
                null,
                null
        );

        Bibliotheque saved = bibliothequeService.saveBibliotheque(b);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

        bibliothequeRepo.delete(saved);
    }

    @Test
    void testUpdateBibliotheque() {
        BIBLIO1.setHoraires("7h-20h");

        Bibliotheque updated = bibliothequeService.updateBibliotheque(BIBLIO1);

        assertEquals("7h-20h", updated.getHoraires());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteBibliotheque() {
        Bibliotheque temp = bibliothequeRepo.save(new Bibliotheque(
                null,
                "Temp Delete",
                "Ville",
                "Rue",
                "00000",
                "9h",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        ));

        bibliothequeService.deleteBibliothequeById(temp.getId());

        assertTrue(bibliothequeRepo.findById(temp.getId()).isEmpty());
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        List<Bibliotheque> result = bibliothequeService.getByNomContainingIgnoreCase("Test");

        assertTrue(result.size() >= 3);
    }
}