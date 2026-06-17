package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.repository.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuteurServiceTest {

    @Autowired
    private AuteurService auteurService;

    @Autowired
    private AuteurRepo auteurRepo;

    @Autowired
    private TypeAuteurRepo typeAuteurRepo;

    @Autowired
    private ClasserRepo classerRepo;

    // =========================
    // TRACKING DES DONNÉES TEST
    // =========================
    private final List<Auteur> auteursCree = new ArrayList<>();
    private final List<TypeAuteur> typesCree = new ArrayList<>();
    private final List<Classer> classersCree = new ArrayList<>();

    private Auteur MARTIN;
    private Auteur DURAND;
    private Auteur DUMAS;
    private Auteur ZOLA;

    private TypeAuteur ROMAN;
    private TypeAuteur POESIE;

    // =========================
    // SETUP
    // =========================
    @BeforeAll
    void setUp() {

        // =========================
        // TYPES AUTEURS
        // =========================
        ROMAN = typeAuteurRepo.save(new TypeAuteur(null, "TEST_ROMAN", null, null));
        POESIE = typeAuteurRepo.save(new TypeAuteur(null, "TEST_POESIE", null, null));

        typesCree.add(ROMAN);
        typesCree.add(POESIE);

        // =========================
        // AUTEURS
        // =========================
        MARTIN = auteurRepo.save(new Auteur(
                null,
                "Martin",
                "Jean",
                LocalDate.of(1970, 5, 10),
                null,
                "France",
                "Lyon",
                "Française",
                null,
                null,
                null
        ));

        DURAND = auteurRepo.save(new Auteur(
                null,
                "Durand",
                "Paul",
                LocalDate.of(1980, 8, 22),
                null,
                "France",
                "Grenoble",
                "Française",
                null,
                null,
                null
        ));

        DUMAS = auteurRepo.save(new Auteur(
                null,
                "Dumas",
                "Alexandre",
                LocalDate.of(1802, 7, 24),
                LocalDate.of(1870, 12, 5),
                "France",
                "Villers-Cotterêts",
                "Française",
                null,
                null,
                null
        ));

        ZOLA = auteurRepo.save(new Auteur(
                null,
                "Zola",
                "Émile",
                LocalDate.of(1840, 4, 2),
                LocalDate.of(1902, 9, 29),
                "France",
                "Paris",
                "Française",
                null,
                null,
                null
        ));

        auteursCree.add(MARTIN);
        auteursCree.add(DURAND);
        auteursCree.add(DUMAS);
        auteursCree.add(ZOLA);

        // =========================
        // CLASSER (LIENS)
        // =========================
        classersCree.add(classerRepo.save(new Classer(null, null, MARTIN, ROMAN)));
        classersCree.add(classerRepo.save(new Classer(null, null, DURAND, POESIE)));
        classersCree.add(classerRepo.save(new Classer(null, null, DUMAS, ROMAN)));
        classersCree.add(classerRepo.save(new Classer(null, null, ZOLA, ROMAN)));
    }

    // =========================
    // CLEANUP (UNIQUEMENT TESTS)
    // =========================
    @AfterAll
    void tearDown() {

        classerRepo.deleteAll(classersCree);
        typeAuteurRepo.deleteAll(typesCree);
        auteurRepo.deleteAll(auteursCree);
    }

    // =========================
    // TESTS
    // =========================

    @Test
    void testGetAllAuteurs() {
        List<Auteur> result = auteurService.getAllAuteurs();

        assertEquals(7, result.size());
    }

    @Test
    void testGetAuteurById() {
        Auteur result = auteurService.getAuteurById(MARTIN.getId());

        assertNotNull(result);
        assertEquals("Martin", result.getNom());
        assertEquals("Jean", result.getPrenom());
    }

    @Test
    void testGetAuteurById_NotFound() {
        Auteur result = auteurService.getAuteurById(999999);

        assertNull(result);
    }

    @Test
    void testSaveAuteur() {
        Auteur auteur = new Auteur(
                null,
                "Balzac",
                "Honoré",
                LocalDate.of(1799, 5, 20),
                LocalDate.of(1850, 8, 18),
                "France",
                "Tours",
                "Française",
                null,
                null,
                null
        );

        Auteur saved = auteurService.saveAuteur(auteur);

        assertNotNull(saved.getId());
        assertEquals("Balzac", saved.getNom());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void testUpdateAuteur() {
        DURAND.setVilleAuteur("Paris");

        Auteur updated = auteurService.updateAuteur(DURAND);

        assertEquals("Paris", updated.getVilleAuteur());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteAuteur() {
        Integer id = ZOLA.getId();

        auteurService.deleteAuteurById(id);

        assertTrue(auteurRepo.findById(id).isEmpty());
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        List<Auteur> result = auteurService.getByNomContainingIgnoreCase("zol");

        assertEquals(1, result.size());
        assertEquals("Zola", result.get(0).getNom());
    }

    @Test
    void testGetByNomAndPrenom() {
        List<Auteur> result = auteurService.getAuteursByNomAndPrenom("Dumas", "Alexandre");

        assertEquals(1, result.size());
        assertEquals(DUMAS.getId(), result.get(0).getId());
    }
}