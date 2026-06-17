package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.repository.TypeEvenementRepo;

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
class TypeEvenementServiceTest {

    @Autowired
    private TypeEvenementService typeEvenementService;

    @Autowired
    private TypeEvenementRepo typeEvenementRepo;

    private TypeEvenement type1;
    private TypeEvenement type2;

    @BeforeAll
    void setUp() {

        type1 = typeEvenementRepo.save(
                new TypeEvenement(
                        null,
                        "Conference",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null
                ));

        type2 = typeEvenementRepo.save(
                new TypeEvenement(
                        null,
                        "Atelier",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        null
                ));
    }

    @AfterAll
    void tearDown() {
        typeEvenementRepo.deleteAll(List.of(type1, type2));
    }

    @Test
    void testGetAllTypeEvenements() {

        List<TypeEvenement> result =
                typeEvenementService.getAllTypeEvenements();

        assertTrue(result.size() >= 2);
    }

    @Test
    void testGetTypeEvenementById() {

        TypeEvenement result =
                typeEvenementService.getTypeEvenementById(type1.getId());

        assertNotNull(result);
        assertEquals("Conference", result.getNom());
    }

    @Test
    void testGetTypeEvenementByIdNotFound() {

        TypeEvenement result =
                typeEvenementService.getTypeEvenementById(-999);

        assertNull(result);
    }

    @Test
    void testSaveTypeEvenement() {

        TypeEvenement type =
                new TypeEvenement(
                        null,
                        "Salon",
                        null,
                        null,
                        null
                );

        TypeEvenement saved =
                typeEvenementService.saveTypeEvenement(type);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

        typeEvenementRepo.delete(saved);
    }

    @Test
    void testUpdateTypeEvenement() {

        type1.setNom("Conference Internationale");

        TypeEvenement updated =
                typeEvenementService.updateTypeEvenement(type1);

        assertEquals(
                "Conference Internationale",
                updated.getNom()
        );

        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteTypeEvenementById() {

        TypeEvenement temp =
                typeEvenementRepo.save(
                        new TypeEvenement(
                                null,
                                "Temporaire",
                                null,
                                null,
                                null
                        )
                );

        Integer id = temp.getId();

        typeEvenementService.deleteTypeEvenementById(id);

        assertTrue(
                typeEvenementRepo.findById(id).isEmpty()
        );
    }

    @Test
    void testGetTypeEvenementsByNom() {

        List<TypeEvenement> result =
                typeEvenementService.getTypeEvenementsByNom("Conference");

        assertFalse(result.isEmpty());
        assertEquals("Conference", result.get(0).getNom());
    }

    @Test
    void testGetTypeEvenementsByNomEmpty() {

        List<TypeEvenement> result =
                typeEvenementService.getTypeEvenementsByNom("Inexistant");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTypeEvenementsByNomLike() {

        List<TypeEvenement> result =
                typeEvenementService.getTypeEvenementsByNomLike("%feren%");

        assertFalse(result.isEmpty());
    }

    @Test
    void testGetTypeEvenementsByNomLikeEmpty() {

        List<TypeEvenement> result =
                typeEvenementService.getTypeEvenementsByNomLike("%XYZ%");

        assertTrue(result.isEmpty());
    }
}