package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.repository.EditeurRepo;

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
public class EditeurServiceTest {

    @Autowired
    private EditeurService editeurService;

    @Autowired
    private EditeurRepo editeurRepo;

    // =====================
    // DONNÉES DE TEST
    // =====================
    private Editeur ED1;
    private Editeur ED2;
    private Editeur ED3;

    @BeforeAll
    void setUp() {

        ED1 = editeurRepo.save(new Editeur(
                null,
                "Shueisha",
                "Tokyo",
                "www.shueisha.co.jp",
                "wikipedia-shueisha",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        ED2 = editeurRepo.save(new Editeur(
                null,
                "Glénat",
                "Grenoble",
                "www.glenat.com",
                "wikipedia-glenat",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        ED3 = editeurRepo.save(new Editeur(
                null,
                "Kurokawa",
                "Paris",
                "www.kurokawa.fr",
                "wikipedia-kurokawa",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));
    }

    @AfterAll
    void tearDown() {
        editeurRepo.deleteAll(List.of(ED1, ED2, ED3));
    }

    // =====================
    // TESTS
    // =====================

    @Test
    void testGetAllEditeurs() {
        List<Editeur> result = editeurService.getAllEditeurs();

        assertTrue(result.size() >= 3);
    }

    @Test
    void testGetEditeurById() {
        Editeur result = editeurService.getEditeurById(ED1.getId());

        assertNotNull(result);
        assertEquals("Shueisha", result.getNom());
    }

    @Test
    void testGetEditeurById_NotFound() {
        Editeur result = editeurService.getEditeurById(-999);

        assertNull(result);
    }

    @Test
    void testSaveEditeur() {
        Editeur e = new Editeur(
                null,
                "Kodansha",
                "Tokyo",
                "www.kodansha.jp",
                "wiki-kodansha",
                null,
                null,
                null
        );

        Editeur saved = editeurService.saveEditeur(e);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

        editeurRepo.delete(saved);
    }

    @Test
    void testUpdateEditeur() {
        ED1.setAdresse("Osaka");

        Editeur updated = editeurService.updateEditeur(ED1);

        assertEquals("Osaka", updated.getAdresse());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteEditeur() {
        Editeur temp = editeurRepo.save(new Editeur(
                null,
                "Temp Editeur",
                "Ville",
                "site",
                "wiki",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        ));

        editeurService.deleteEditeurById(temp.getId());

        assertTrue(editeurRepo.findById(temp.getId()).isEmpty());
    }

    @Test
    void testGetEditeursByNom() {
        List<Editeur> result = editeurService.getEditeursByNom("Shueisha");

        assertEquals(1, result.size());
        assertEquals("Shueisha", result.get(0).getNom());
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        List<Editeur> result = editeurService.getByNomContainingIgnoreCase("len");

        assertTrue(result.size() >= 0);
    }

    @Test
    void testGetEditeursByNomStartWithIgnoreCase() {
        List<Editeur> result = editeurService.getEditeursByNomStartWithIgnoreCase("Glé");

        assertEquals(1, result.size());
        assertEquals("Glénat", result.get(0).getNom());
    }
}