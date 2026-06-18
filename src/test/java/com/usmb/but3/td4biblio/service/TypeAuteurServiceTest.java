package com.usmb.but3.td4biblio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;

@SpringBootTest(properties = {"spring.flyway.enabled=false"})
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
                "Biographe",
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        );

        illustrateur = typeAuteurRepo.save(
            new TypeAuteur(
                null,
                "Ghostwriter",
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
        assertEquals("Biographe", result.getNom());
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
                typeAuteurService.getTypeAuteursByNom("Biographe");

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
                typeAuteurService.getTypeAuteursByNomLike("%iogra%");

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetTypeAuteursByNomLikeEmpty() {

        List<TypeAuteur> result =
                typeAuteurService.getTypeAuteursByNomLike("%xxxxx%");

        assertTrue(result.isEmpty());
    }
}