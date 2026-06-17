package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.repository.*;
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
public class EvenementServiceTest {

    @Autowired
    private EvenementService evenementService;

    @Autowired
    private EvenementRepo evenementRepo;

    @Autowired
    private TypeEvenementRepo typeEvenementRepo;

    @Autowired
    private BibliothequeRepo bibliothequeRepo;

    // =====================
    // DATASET
    // =====================
    private TypeEvenement TYPE1;
    private Bibliotheque BIBLIO1;

    private Evenement EVT1;
    private Evenement EVT2;
    private Evenement EVT3;

    @BeforeAll
    void setUp() {

        // =====================
        // DEPENDANCES
        // =====================
        TYPE1 = typeEvenementRepo.save(new TypeEvenement(
                null,
                "Culture",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        BIBLIO1 = bibliothequeRepo.save(new Bibliotheque(
                null,
                "Bibliotheque Test",
                "Chambery",
                "Rue Centrale",
                "73000",
                "9h-18h",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        ));

        // =====================
        // EVENEMENTS
        // =====================
        EVT1 = evenementRepo.save(new Evenement(
                null,
                "Concert A",
                "Desc A",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                LocalDateTime.now(),
                LocalDateTime.now(),
                TYPE1,
                BIBLIO1
        ));

        EVT2 = evenementRepo.save(new Evenement(
                null,
                "Expo B",
                "Desc B",
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4),
                LocalDateTime.now(),
                LocalDateTime.now(),
                TYPE1,
                BIBLIO1
        ));

        EVT3 = evenementRepo.save(new Evenement(
                null,
                "Conférence C",
                "Desc C",
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(1),
                LocalDateTime.now(),
                LocalDateTime.now(),
                TYPE1,
                BIBLIO1
        ));
    }

    @AfterAll
    void tearDown() {
        evenementRepo.deleteAll(List.of(EVT1, EVT2, EVT3));
        typeEvenementRepo.delete(TYPE1);
        bibliothequeRepo.delete(BIBLIO1);
    }

    // =====================
    // TESTS
    // =====================

    @Test
    void testGetAllEvenements() {
        List<Evenement> result = evenementService.getAllEvenements();
        assertTrue(result.size() >= 3);
    }

    @Test
    void testGetEvenementById() {
        Evenement result = evenementService.getEvenementById(EVT1.getId());

        assertNotNull(result);
        assertEquals("Concert A", result.getTitre());
    }

    @Test
    void testSaveEvenement() {
        Evenement e = new Evenement(
                null,
                "Save Event",
                "Desc",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(11),
                null,
                null,
                TYPE1,
                BIBLIO1
        );

        Evenement saved = evenementService.saveEvenement(e);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

        evenementRepo.delete(saved);
    }

    @Test
    void testUpdateEvenement() {
        EVT1.setTitre("Updated");

        Evenement updated = evenementService.updateEvenement(EVT1);

        assertEquals("Updated", updated.getTitre());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteEvenement() {
        Evenement temp = evenementRepo.save(new Evenement(
                null,
                "Temp",
                "Temp",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                LocalDateTime.now(),
                LocalDateTime.now(),
                TYPE1,
                BIBLIO1
        ));

        evenementService.deleteEvenementById(temp.getId());

        assertTrue(evenementRepo.findById(temp.getId()).isEmpty());
    }

    @Test
    void testGetEvenementsFuturs() {
        List<Evenement> result = evenementService.getEvenementsFuturs();

        assertTrue(
                result.stream().allMatch(e ->
                        e.getDateDebut().isAfter(LocalDate.now().minusDays(1))
                )
        );
    }
}