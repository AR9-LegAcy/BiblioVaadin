package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;

@SpringBootTest
class TypeAuteurServiceTest {

    @Mock
    private TypeAuteurRepo typeAuteurRepo;

    @InjectMocks
    private TypeAuteurService typeAuteurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTypeAuteurs() {
        TypeAuteur typeAuteur1 = new TypeAuteur(1, "Auteur", LocalDateTime.now(), LocalDateTime.now());
        TypeAuteur typeAuteur2 = new TypeAuteur(2, "Illustrateur", LocalDateTime.now(), LocalDateTime.now());
        when(typeAuteurRepo.findAll(Sort.by(Sort.Direction.ASC, "nom")))
            .thenReturn(Arrays.asList(typeAuteur1, typeAuteur2));

        List<TypeAuteur> typeAuteurs = typeAuteurService.getAllTypeAuteurs();

        assertEquals(2, typeAuteurs.size());
        assertEquals("Auteur", typeAuteurs.get(0).getNom());
        assertEquals("Illustrateur", typeAuteurs.get(1).getNom());
        verify(typeAuteurRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    @Test
    void testGetTypeAuteurById() {
        TypeAuteur typeAuteur = new TypeAuteur(1, "Auteur", LocalDateTime.now(), LocalDateTime.now());
        when(typeAuteurRepo.findById(1)).thenReturn(Optional.of(typeAuteur));

        TypeAuteur result = typeAuteurService.getTypeAuteurById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Auteur", result.getNom());
        verify(typeAuteurRepo, times(1)).findById(1);
    }

    @Test
    void testGetTypeAuteurByIdNotFound() {
        when(typeAuteurRepo.findById(1)).thenReturn(Optional.empty());

        TypeAuteur result = typeAuteurService.getTypeAuteurById(1);

        assertNull(result);
        verify(typeAuteurRepo, times(1)).findById(1);
    }

    @Test
    void testSaveTypeAuteur() {
        TypeAuteur typeAuteur = new TypeAuteur(null, "Editeur", null, null);
        TypeAuteur savedTypeAuteur = new TypeAuteur(1, "Editeur", LocalDateTime.now(), LocalDateTime.now());
        when(typeAuteurRepo.save(any(TypeAuteur.class))).thenReturn(savedTypeAuteur);

        TypeAuteur result = typeAuteurService.saveTypeAuteur(typeAuteur);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Editeur", result.getNom());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(typeAuteurRepo, times(1)).save(any(TypeAuteur.class));
    }

    @Test
    void testUpdateTypeAuteur() {
        TypeAuteur typeAuteur = new TypeAuteur(1, "Traducteur", LocalDateTime.now(), LocalDateTime.now());
        when(typeAuteurRepo.save(any(TypeAuteur.class))).thenReturn(typeAuteur);

        TypeAuteur result = typeAuteurService.updateTypeAuteur(typeAuteur);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Traducteur", result.getNom());
        assertNotNull(result.getUpdatedAt());
        verify(typeAuteurRepo, times(1)).save(any(TypeAuteur.class));
    }

    @Test
    void testDeleteTypeAuteurById() {
        doNothing().when(typeAuteurRepo).deleteById(1);

        typeAuteurService.deleteTypeAuteurById(1);

        verify(typeAuteurRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetTypeAuteursByNom() {
        TypeAuteur typeAuteur = new TypeAuteur(1, "Auteur", LocalDateTime.now(), LocalDateTime.now());
        when(typeAuteurRepo.findByNom("Auteur")).thenReturn(Arrays.asList(typeAuteur));

        List<TypeAuteur> typeAuteurs = typeAuteurService.getTypeAuteursByNom("Auteur");

        assertEquals(1, typeAuteurs.size());
        assertEquals("Auteur", typeAuteurs.get(0).getNom());
        verify(typeAuteurRepo, times(1)).findByNom("Auteur");
    }

    @Test
    void testGetTypeAuteursByNomLike() {
        TypeAuteur typeAuteur1 = new TypeAuteur(1, "Auteur", LocalDateTime.now(), LocalDateTime.now());
        TypeAuteur typeAuteur2 = new TypeAuteur(2, "Co-Auteur", LocalDateTime.now(), LocalDateTime.now());
        when(typeAuteurRepo.findByNomLike("%Auteur%")).thenReturn(Arrays.asList(typeAuteur1, typeAuteur2));

        List<TypeAuteur> typeAuteurs = typeAuteurService.getTypeAuteursByNomLike("%Auteur%");

        assertEquals(2, typeAuteurs.size());
        verify(typeAuteurRepo, times(1)).findByNomLike("%Auteur%");
    }

    @Test
    void testGetTypeAuteursByNomLikeEmpty() {
        when(typeAuteurRepo.findByNomLike("%NonExistent%")).thenReturn(Arrays.asList());

        List<TypeAuteur> typeAuteurs = typeAuteurService.getTypeAuteursByNomLike("%NonExistent%");

        assertEquals(0, typeAuteurs.size());
        verify(typeAuteurRepo, times(1)).findByNomLike("%NonExistent%");
    }

    @Test
    void testSaveTypeAuteurWithCreatedAtAlreadySet() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        TypeAuteur typeAuteur = new TypeAuteur(null, "Illustrateur", createdAt, null);
        TypeAuteur savedTypeAuteur = new TypeAuteur(1, "Illustrateur", createdAt, LocalDateTime.now());
        when(typeAuteurRepo.save(any(TypeAuteur.class))).thenReturn(savedTypeAuteur);

        TypeAuteur result = typeAuteurService.saveTypeAuteur(typeAuteur);

        assertNotNull(result);
        assertEquals(createdAt, result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(typeAuteurRepo, times(1)).save(any(TypeAuteur.class));
    }

    @Test
    void testGetTypeAuteursByNomEmpty() {
        when(typeAuteurRepo.findByNom("NonExistent")).thenReturn(Arrays.asList());

        List<TypeAuteur> typeAuteurs = typeAuteurService.getTypeAuteursByNom("NonExistent");

        assertEquals(0, typeAuteurs.size());
        verify(typeAuteurRepo, times(1)).findByNom("NonExistent");
    }
}