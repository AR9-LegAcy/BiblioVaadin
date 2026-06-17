package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class TypeAuteurServiceTest {

    @Autowired
    private TypeAuteurService typeAuteurService;

    @Autowired
    private TypeAuteurRepo typeAuteurRepo;

    private TypeAuteur auteur;
    private TypeAuteur illustrateur;

    @BeforeAll
    void setUp() {

        auteur = typeAuteurRepo.save(
            new TypeAuteur(
                null,
                "Auteur",
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        );

        illustrateur = typeAuteurRepo.save(
            new TypeAuteur(
                null,
                "Illustrateur",
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        );
    }

    @AfterAll
    void tearDown() {
        typeAuteurRepo.deleteAll(List.of(auteur, illustrateur));
    }

    @Test
    void testGetAllTypeAuteurs() {
        List<TypeAuteur> result = typeAuteurService.getAllTypeAuteurs();

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetTypeAuteurById() {
        TypeAuteur result =
                typeAuteurService.getTypeAuteurById(auteur.getId());

        assertNotNull(result);
        assertEquals("Auteur", result.getNom());
    }

    @Test
    void testGetTypeAuteurByIdNotFound() {
        TypeAuteur result =
                typeAuteurService.getTypeAuteurById(-999);

        assertNull(result);
    }

    @Test
    void testSaveTypeAuteur() {

        TypeAuteur nouveau =
                new TypeAuteur(
                        null,
                        "Traducteur",
                        null,
                        null
                );

        TypeAuteur saved =
                typeAuteurService.saveTypeAuteur(nouveau);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

        typeAuteurRepo.delete(saved);
    }

    @Test
    void testUpdateTypeAuteur() {

        auteur.setNom("Auteur Principal");

        TypeAuteur updated =
                typeAuteurService.updateTypeAuteur(auteur);

        assertEquals(
                "Auteur Principal",
                updated.getNom()
        );

        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteTypeAuteurById() {

        TypeAuteur temp =
                typeAuteurRepo.save(
                        new TypeAuteur(
                                null,
                                "Temp",
                                null,
                                null
                        )
                );

        Integer id = temp.getId();

        typeAuteurService.deleteTypeAuteurById(id);

        assertTrue(
                typeAuteurRepo.findById(id).isEmpty()
        );
    }

    @Test
    void testGetTypeAuteursByNom() {

        List<TypeAuteur> result =
                typeAuteurService.getTypeAuteursByNom("Auteur");

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetTypeAuteursByNomEmpty() {

        List<TypeAuteur> result =
                typeAuteurService.getTypeAuteursByNom("Inexistant");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTypeAuteursByNomLike() {

        List<TypeAuteur> result =
                typeAuteurService.getTypeAuteursByNomLike("%uteur%");

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetTypeAuteursByNomLikeEmpty() {

        List<TypeAuteur> result =
                typeAuteurService.getTypeAuteursByNomLike("%xxxxx%");

        assertTrue(result.isEmpty());
    }
}