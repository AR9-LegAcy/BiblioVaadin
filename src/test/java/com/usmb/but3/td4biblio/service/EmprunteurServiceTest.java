package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

@SpringBootTest
class EmprunteurServiceTest {

    @Mock
    private EmprunteurRepo emprunteurRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmprunteurService emprunteurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmprunteurs() {
        Emprunteur emp1 = new Emprunteur(1, "Nom1", "Prenom1", "Rue1", "Ville1", "75001", "email1@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password1", LocalDateTime.now(), LocalDateTime.now(), null);
        Emprunteur emp2 = new Emprunteur(2, "Nom2", "Prenom2", "Rue2", "Ville2", "75002", "email2@test.com", LocalDate.of(1995, 5, 5), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password2", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom")))
            .thenReturn(Arrays.asList(emp1, emp2));

        List<Emprunteur> emprunteurs = emprunteurService.getAllEmprunteurs();

        assertEquals(2, emprunteurs.size());
        assertEquals("Nom1", emprunteurs.get(0).getNom());
        assertEquals("Nom2", emprunteurs.get(1).getNom());
        verify(emprunteurRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom"));
    }

    @Test
    void testGetEmprunteurById() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(emprunteur));

        Emprunteur result = emprunteurService.getEmprunteurById(1);

        assertNotNull(result);
        assertEquals(1, result.getCarteEmprunteur());
        assertEquals("Nom", result.getNom());
        verify(emprunteurRepo, times(1)).findById(1);
    }

    @Test
    void testGetEmprunteurByIdNotFound() {
        when(emprunteurRepo.findById(1)).thenReturn(Optional.empty());

        Emprunteur result = emprunteurService.getEmprunteurById(1);

        assertNull(result);
        verify(emprunteurRepo, times(1)).findById(1);
    }

    @Test
    void testSaveEmprunteur() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), null, null, null, null);
        Emprunteur savedEmprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "encodedPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(emprunteurRepo.save(any(Emprunteur.class))).thenReturn(savedEmprunteur);

        Emprunteur result = emprunteurService.saveEmprunteur(emprunteur);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertNotNull(result.getMotDePasse());
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(emprunteurRepo, times(1)).save(any(Emprunteur.class));
    }

    @Test
    void testUpdateEmprunteur() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "encodedPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.save(any(Emprunteur.class))).thenReturn(emprunteur);

        Emprunteur result = emprunteurService.updateEmprunteur(emprunteur);

        assertNotNull(result);
        assertNotNull(result.getUpdatedAt());
        verify(emprunteurRepo, times(1)).save(any(Emprunteur.class));
    }

    @Test
    void testUpdateEmprunteurWithoutPassword() {
        Emprunteur oldEmprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "oldPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        Emprunteur updatedEmprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "oldPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        
        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(oldEmprunteur));
        when(emprunteurRepo.save(any(Emprunteur.class))).thenReturn(updatedEmprunteur);

        Emprunteur result = emprunteurService.updateEmprunteur(emprunteur);

        assertNotNull(result);
        assertEquals("oldPassword", result.getMotDePasse());
        verify(emprunteurRepo, times(1)).findById(1);
        verify(emprunteurRepo, times(1)).save(any(Emprunteur.class));
    }

    @Test
    void testDeleteEmprunteurById() {
        doNothing().when(emprunteurRepo).deleteById(1);

        emprunteurService.deleteEmprunteurById(1);

        verify(emprunteurRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetEmprunteursByNom() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.findByNom("Nom")).thenReturn(Arrays.asList(emprunteur));

        List<Emprunteur> emprunteurs = emprunteurService.getEmprunteursByNom("Nom");

        assertEquals(1, emprunteurs.size());
        assertEquals("Nom", emprunteurs.get(0).getNom());
        verify(emprunteurRepo, times(1)).findByNom("Nom");
    }

    @Test
    void testGetEmprunteursByNomAndPrenom() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.findByNomAndPrenom("Nom", "Prenom")).thenReturn(Arrays.asList(emprunteur));

        List<Emprunteur> emprunteurs = emprunteurService.getEmprunteursByNomAndPrenom("Nom", "Prenom");

        assertEquals(1, emprunteurs.size());
        assertEquals("Prenom", emprunteurs.get(0).getPrenom());
        verify(emprunteurRepo, times(1)).findByNomAndPrenom("Nom", "Prenom");
    }

    @Test
    void testGetEmprunteursByNomLikeAndPrenomLike() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.findByNomLikeAndPrenomLike("%Nom%", "%Prenom%")).thenReturn(Arrays.asList(emprunteur));

        List<Emprunteur> emprunteurs = emprunteurService.getEmprunteursByNomLikeAndPrenomLike("%Nom%", "%Prenom%");

        assertEquals(1, emprunteurs.size());
        verify(emprunteurRepo, times(1)).findByNomLikeAndPrenomLike("%Nom%", "%Prenom%");
    }

    @Test
    void testGetEmprunteursByNomStartWithIgnoreCase() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.findByNomStartsWithIgnoreCase("Nom")).thenReturn(Arrays.asList(emprunteur));

        List<Emprunteur> emprunteurs = emprunteurService.getEmprunteursByNomStartWithIgnoreCase("Nom");

        assertEquals(1, emprunteurs.size());
        verify(emprunteurRepo, times(1)).findByNomStartsWithIgnoreCase("Nom");
    }

    @Test
    void testGetEmprunteursByNomContainingIgnoreCase() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.findByNomContainingIgnoreCase("Nom")).thenReturn(Arrays.asList(emprunteur));

        List<Emprunteur> emprunteurs = emprunteurService.getEmprunteursByNomContainingIgnoreCase("Nom");

        assertEquals(1, emprunteurs.size());
        verify(emprunteurRepo, times(1)).findByNomContainingIgnoreCase("Nom");
    }

    @Test
    void testGetEmprunteursByPrenomContainingIgnoreCase() {
        Emprunteur emprunteur = new Emprunteur(1, "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "password", LocalDateTime.now(), LocalDateTime.now(), null);
        when(emprunteurRepo.findByPrenomContainingIgnoreCase("Prenom")).thenReturn(Arrays.asList(emprunteur));

        List<Emprunteur> emprunteurs = emprunteurService.getEmprunteursByPrenomContainingIgnoreCase("Prenom");

        assertEquals(1, emprunteurs.size());
        verify(emprunteurRepo, times(1)).findByPrenomContainingIgnoreCase("Prenom");
    }
}