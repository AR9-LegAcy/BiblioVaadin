package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.repository.BibliothecaireRepo;

@SpringBootTest
class BibliothecaireServiceTest {

    @Mock
    private BibliothecaireRepo bibliothecaireRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private BibliothecaireService bibliothecaireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBibliothecaires() {
        Bibliothecaire biblio1 = new Bibliothecaire("pseudo1", "Nom1", "Prenom1", "Rue1", "Ville1", "75001", "email1@test.com", LocalDate.of(1990, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        Bibliothecaire biblio2 = new Bibliothecaire("pseudo2", "Nom2", "Prenom2", "Rue2", "Ville2", "75002", "email2@test.com", LocalDate.of(1995, 5, 5), null, LocalDateTime.now(), LocalDateTime.now(), null);
        when(bibliothecaireRepo.findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom")))
            .thenReturn(Arrays.asList(biblio1, biblio2));

        List<Bibliothecaire> bibliothecaires = bibliothecaireService.getAllBibliothecaires();

        assertEquals(2, bibliothecaires.size());
        assertEquals("Nom1", bibliothecaires.get(0).getNom());
        assertEquals("Nom2", bibliothecaires.get(1).getNom());
        verify(bibliothecaireRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom"));
    }

    @Test
    void testGetBibliothecaireByPseudo() {
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        when(bibliothecaireRepo.findByPseudo("pseudo1")).thenReturn(biblio);

        Bibliothecaire result = bibliothecaireService.getBibliothecaireByPseudo("pseudo1");

        assertNotNull(result);
        assertEquals("pseudo1", result.getPseudo());
        assertEquals("Nom", result.getNom());
        verify(bibliothecaireRepo, times(1)).findByPseudo("pseudo1");
    }

    @Test
    void testSaveBibliothecaire() {
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), null, null, null, null);
        Bibliothecaire savedBiblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), "encodedPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(bibliothecaireRepo.save(any(Bibliothecaire.class))).thenReturn(savedBiblio);

        Bibliothecaire result = bibliothecaireService.saveBibliothecaire(biblio);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertNotNull(result.getMotDePasse());
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(bibliothecaireRepo, times(1)).save(any(Bibliothecaire.class));
    }

    @Test
    void testUpdateBibliothecaire() {
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), "encodedPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        when(bibliothecaireRepo.save(any(Bibliothecaire.class))).thenReturn(biblio);

        Bibliothecaire result = bibliothecaireService.updateBibliothecaire(biblio);

        assertNotNull(result);
        assertNotNull(result.getUpdatedAt());
        verify(bibliothecaireRepo, times(1)).save(any(Bibliothecaire.class));
    }

    @Test
    void testUpdateBibliothecaireWithoutPassword() {
        Bibliothecaire oldBiblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), "oldPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        Bibliothecaire updatedBiblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), "oldPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        
        when(bibliothecaireRepo.findByPseudo("pseudo1")).thenReturn(oldBiblio);
        when(bibliothecaireRepo.save(any(Bibliothecaire.class))).thenReturn(updatedBiblio);

        Bibliothecaire result = bibliothecaireService.updateBibliothecaire(biblio);

        assertNotNull(result);
        assertEquals("oldPassword", result.getMotDePasse());
        verify(bibliothecaireRepo, times(1)).findByPseudo("pseudo1");
        verify(bibliothecaireRepo, times(1)).save(any(Bibliothecaire.class));
    }

    @Test
    void testDeleteBibliothecaireByPseudo() {
        bibliothecaireService.deleteBibliothecaireByPseudo("pseudo1");
    
        verify(bibliothecaireRepo, times(1)).deleteByPseudo("pseudo1");
    }

    @Test
    void testGetBibliothecairesByNom() {
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        when(bibliothecaireRepo.findByNom("Nom")).thenReturn(Arrays.asList(biblio));

        List<Bibliothecaire> bibliothecaires = bibliothecaireService.getBibliothecairesByNom("Nom");

        assertEquals(1, bibliothecaires.size());
        assertEquals("Nom", bibliothecaires.get(0).getNom());
        verify(bibliothecaireRepo, times(1)).findByNom("Nom");
    }

    @Test
    void testGetBibliothecairesByNomAndPrenom() {
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        when(bibliothecaireRepo.findByNomAndPrenom("Nom", "Prenom")).thenReturn(Arrays.asList(biblio));

        List<Bibliothecaire> bibliothecaires = bibliothecaireService.getBibliothecairesByNomAndPrenom("Nom", "Prenom");

        assertEquals(1, bibliothecaires.size());
        assertEquals("Prenom", bibliothecaires.get(0).getPrenom());
        verify(bibliothecaireRepo, times(1)).findByNomAndPrenom("Nom", "Prenom");
    }

    @Test
    void testGetBibliothecairesByNomLikeAndPrenomLike() {
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        when(bibliothecaireRepo.findByNomLikeAndPrenomLike("%Nom%", "%Prenom%")).thenReturn(Arrays.asList(biblio));

        List<Bibliothecaire> bibliothecaires = bibliothecaireService.getBibliothecairesByNomLikeAndPrenomLike("%Nom%", "%Prenom%");

        assertEquals(1, bibliothecaires.size());
        verify(bibliothecaireRepo, times(1)).findByNomLikeAndPrenomLike("%Nom%", "%Prenom%");
    }

    @Test
    void testGetBibliothecairesByNomStartWithIgnoreCase() {
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        when(bibliothecaireRepo.findByNomStartsWithIgnoreCase("Nom")).thenReturn(Arrays.asList(biblio));

        List<Bibliothecaire> bibliothecaires = bibliothecaireService.getBibliothecairesByNomStartWithIgnoreCase("Nom");

        assertEquals(1, bibliothecaires.size());
        verify(bibliothecaireRepo, times(1)).findByNomStartsWithIgnoreCase("Nom");
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        Bibliothecaire biblio = new Bibliothecaire("pseudo1", "Nom", "Prenom", "Rue", "Ville", "75001", "email@test.com", LocalDate.of(1990, 1, 1), null, LocalDateTime.now(), LocalDateTime.now(), null);
        when(bibliothecaireRepo.findByNomContainingIgnoreCase("Nom")).thenReturn(Arrays.asList(biblio));

        List<Bibliothecaire> bibliothecaires = bibliothecaireService.getByNomContainingIgnoreCase("Nom");

        assertEquals(1, bibliothecaires.size());
        verify(bibliothecaireRepo, times(1)).findByNomContainingIgnoreCase("Nom");
    }
}