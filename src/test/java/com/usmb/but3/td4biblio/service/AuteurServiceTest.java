package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.repository.AuteurRepo;

@SpringBootTest
class AuteurServiceTest {

    @Mock
    private AuteurRepo auteurRepo;

    @InjectMocks
    private AuteurService auteurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuteurs() {
    Auteur auteur1 = new Auteur(1, "Nom1", "Prenom1", null, null, null, null, null, null, null, null);
    Auteur auteur2 = new Auteur(2, "Nom2", "Prenom2", null, null, null, null, null, null, null, null);
    when(auteurRepo.findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom")))
        .thenReturn(Arrays.asList(auteur1, auteur2));

    List<Auteur> auteurs = auteurService.getAllAuteurs();

    assertEquals(2, auteurs.size());
    assertEquals("Nom1", auteurs.get(0).getNom());
    assertEquals("Nom2", auteurs.get(1).getNom());
    verify(auteurRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom"));
}

    @Test
    void testGetAuteurById() {
        Auteur auteur = new Auteur(1, "Nom", "Prenom", null, null, null, null, null, null, null, null);
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));

        Auteur result = auteurService.getAuteurById(1);

        assertNotNull(result);
        assertEquals("Nom", result.getNom());
        verify(auteurRepo, times(1)).findById(1);
    }

    @Test
    void testSaveAuteur() {
        Auteur auteur = new Auteur(null, "Nom", "Prenom", null, null, null, null, null, null, null, null);
        when(auteurRepo.save(any(Auteur.class))).thenReturn(auteur);

        Auteur result = auteurService.saveAuteur(auteur);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(auteurRepo, times(1)).save(auteur);
    }

    @Test
    void testUpdateAuteur() {
        Auteur auteur = new Auteur(1, "Nom", "Prenom", null, null, null, null, null, null, null, null);
        when(auteurRepo.save(any(Auteur.class))).thenReturn(auteur);

        Auteur result = auteurService.updateAuteur(auteur);

        assertNotNull(result);
        assertNotNull(result.getUpdatedAt());
        verify(auteurRepo, times(1)).save(auteur);
    }

    @Test
    void testDeleteAuteurById() {
        doNothing().when(auteurRepo).deleteById(1);

        auteurService.deleteAuteurById(1);

        verify(auteurRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetAuteursByNom() {
        Auteur auteur = new Auteur(1, "Nom", "Prenom", null, null, null, null, null, null, null, null);
        when(auteurRepo.findByNom("Nom")).thenReturn(Arrays.asList(auteur));

        List<Auteur> auteurs = auteurService.getAuteursByNom("Nom");

        assertEquals(1, auteurs.size());
        assertEquals("Nom", auteurs.get(0).getNom());
        verify(auteurRepo, times(1)).findByNom("Nom");
    }

    @Test
    void testGetAuteursByNomAndPrenom() {
        Auteur auteur = new Auteur(1, "Nom", "Prenom", null, null, null, null, null, null, null, null);
        when(auteurRepo.findByNomAndPrenom("Nom", "Prenom")).thenReturn(Arrays.asList(auteur));

        List<Auteur> auteurs = auteurService.getAuteursByNomAndPrenom("Nom", "Prenom");

        assertEquals(1, auteurs.size());
        assertEquals("Prenom", auteurs.get(0).getPrenom());
        verify(auteurRepo, times(1)).findByNomAndPrenom("Nom", "Prenom");
    }

    @Test
    void testGetAuteursByNomLikeAndPrenomLike() {
        Auteur auteur = new Auteur(1, "Nom", "Prenom", null, null, null, null, null, null, null, null);
        when(auteurRepo.findByNomLikeAndPrenomLike("%Nom%", "%Prenom%")).thenReturn(Arrays.asList(auteur));

        List<Auteur> auteurs = auteurService.getAuteursByNomLikeAndPrenomLike("%Nom%", "%Prenom%");

        assertEquals(1, auteurs.size());
        verify(auteurRepo, times(1)).findByNomLikeAndPrenomLike("%Nom%", "%Prenom%");
    }

    @Test
    void testGetAuteursByNomStartWithIgnoreCase() {
        Auteur auteur = new Auteur(1, "Nom", "Prenom", null, null, null, null, null, null, null, null);
        when(auteurRepo.findByNomStartsWithIgnoreCase("Nom")).thenReturn(Arrays.asList(auteur));

        List<Auteur> auteurs = auteurService.getAuteursByNomStartWithIgnoreCase("Nom");

        assertEquals(1, auteurs.size());
        verify(auteurRepo, times(1)).findByNomStartsWithIgnoreCase("Nom");
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        Auteur auteur = new Auteur(1, "Nom", "Prenom", null, null, null, null, null, null, null, null);
        when(auteurRepo.findByNomContainingIgnoreCase("Nom")).thenReturn(Arrays.asList(auteur));

        List<Auteur> auteurs = auteurService.getByNomContainingIgnoreCase("Nom");

        assertEquals(1, auteurs.size());
        verify(auteurRepo, times(1)).findByNomContainingIgnoreCase("Nom");
    }
}