package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.repository.BibliothecaireRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BibliothecaireServiceMockTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BibliothecaireRepo bibliothecaireRepo;

    @InjectMocks
    private BibliothecaireService bibliothecaireService;

    @Test
    void testGetAllBibliothecaires() {
        // Arrange
        Bibliothecaire bib1 = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, null, null);
        Bibliothecaire bib2 = new Bibliothecaire("admin2", "Martin", "Sophie", "20 avenue Lyon", "Lyon", "69000", "sophie@example.com", LocalDate.of(1988, 3, 22), null, null, null, null);
        List<Bibliothecaire> bibliothecaires = List.of(bib1, bib2);

        when(bibliothecaireRepo.findAll(any(Sort.class))).thenReturn(bibliothecaires);

        // Act
        List<Bibliothecaire> result = bibliothecaireService.getAllBibliothecaires();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(bib1, bib2);
        verify(bibliothecaireRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetBibliothecaireByPseudo() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, null, null);
        when(bibliothecaireRepo.findByPseudo("admin1")).thenReturn(bib);

        // Act
        Bibliothecaire result = bibliothecaireService.getBibliothecaireByPseudo("admin1");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPseudo()).isEqualTo("admin1");
        assertThat(result.getNom()).isEqualTo("Dupont");
        verify(bibliothecaireRepo, times(1)).findByPseudo("admin1");
    }

    @Test
    void testSaveBibliothecaire() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire();
        bib.setPseudo("newadmin");
        bib.setNom("Bernard");
        bib.setPrenom("Marie");
        bib.setAdresseRue("30 Boulevard des Invalides");
        bib.setAdresseVille("Paris");
        bib.setAdresseCP("75007");
        bib.setEmail("marie@example.com");
        bib.setDateNaissance(LocalDate.of(1995, 7, 10));

        Bibliothecaire savedBib = new Bibliothecaire();
        savedBib.setPseudo("newadmin");
        savedBib.setNom("Bernard");
        savedBib.setPrenom("Marie");
        savedBib.setAdresseRue("30 Boulevard des Invalides");
        savedBib.setAdresseVille("Paris");
        savedBib.setAdresseCP("75007");
        savedBib.setEmail("marie@example.com");
        savedBib.setDateNaissance(LocalDate.of(1995, 7, 10));
        savedBib.setMotDePasse("$2a$10$encoded_password");
        savedBib.setCreatedAt(LocalDateTime.now());
        savedBib.setUpdatedAt(LocalDateTime.now());

        when(passwordEncoder.encode("10071995")).thenReturn("$2a$10$encoded_password");
        when(bibliothecaireRepo.save(any(Bibliothecaire.class))).thenReturn(savedBib);

        // Act
        Bibliothecaire result = bibliothecaireService.saveBibliothecaire(bib);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPseudo()).isEqualTo("newadmin");
        assertThat(result.getMotDePasse()).isEqualTo("$2a$10$encoded_password");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(passwordEncoder, times(1)).encode("10071995");
        verify(bibliothecaireRepo, times(1)).save(any(Bibliothecaire.class));
    }

    @Test
    void testUpdateBibliothecaire() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), "$2a$10$old_password", null, null, null);
        
        Bibliothecaire updatedBib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean.new@example.com", LocalDate.of(1990, 5, 15), "$2a$10$old_password", null, LocalDateTime.now(), null);

        when(bibliothecaireRepo.save(bib)).thenReturn(updatedBib);

        // Act
        Bibliothecaire result = bibliothecaireService.updateBibliothecaire(bib);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(bibliothecaireRepo, times(1)).save(bib);
    }

    @Test
    void testUpdateBibliothecaire_WithNewPassword() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), "$2a$10$new_encoded_password", null, null, null);
        
        Bibliothecaire updatedBib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), "$2a$10$new_encoded_password", null, LocalDateTime.now(), null);

        when(bibliothecaireRepo.save(bib)).thenReturn(updatedBib);

        // Act
        Bibliothecaire result = bibliothecaireService.updateBibliothecaire(bib);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getMotDePasse()).isEqualTo("$2a$10$new_encoded_password");
        verify(bibliothecaireRepo, times(1)).save(bib);
    }

    @Test
    void testDeleteBibliothecaireByPseudo() {
        // Act
        bibliothecaireService.deleteBibliothecaireByPseudo("admin1");

        // Assert
        verify(bibliothecaireRepo, times(1)).deleteByPseudo("admin1");
    }

    @Test
    void testGetBibliothecairesByNom() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, null, null);
        List<Bibliothecaire> bibliothecaires = List.of(bib);

        when(bibliothecaireRepo.findByNom("Dupont")).thenReturn(bibliothecaires);

        // Act
        List<Bibliothecaire> result = bibliothecaireService.getBibliothecairesByNom("Dupont");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(bibliothecaireRepo, times(1)).findByNom("Dupont");
    }

    @Test
    void testGetBibliothecairesByNomAndPrenom() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, null, null);
        List<Bibliothecaire> bibliothecaires = List.of(bib);

        when(bibliothecaireRepo.findByNomAndPrenom("Dupont", "Jean")).thenReturn(bibliothecaires);

        // Act
        List<Bibliothecaire> result = bibliothecaireService.getBibliothecairesByNomAndPrenom("Dupont", "Jean");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        assertThat(result.get(0).getPrenom()).isEqualTo("Jean");
        verify(bibliothecaireRepo, times(1)).findByNomAndPrenom("Dupont", "Jean");
    }

    @Test
    void testGetBibliothecairesByNomLikeAndPrenomLike() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, null, null);
        List<Bibliothecaire> bibliothecaires = List.of(bib);

        when(bibliothecaireRepo.findByNomLikeAndPrenomLike("%Dupon%", "%Jea%")).thenReturn(bibliothecaires);

        // Act
        List<Bibliothecaire> result = bibliothecaireService.getBibliothecairesByNomLikeAndPrenomLike("Dupon", "Jea");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(bibliothecaireRepo, times(1)).findByNomLikeAndPrenomLike("%Dupon%", "%Jea%");
    }

    @Test
    void testGetBibliothecairesByNomStartWithIgnoreCase() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, null, null);
        List<Bibliothecaire> bibliothecaires = List.of(bib);

        when(bibliothecaireRepo.findByNomStartsWithIgnoreCase("dup")).thenReturn(bibliothecaires);

        // Act
        List<Bibliothecaire> result = bibliothecaireService.getBibliothecairesByNomStartWithIgnoreCase("dup");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(bibliothecaireRepo, times(1)).findByNomStartsWithIgnoreCase("dup");
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        // Arrange
        Bibliothecaire bib = new Bibliothecaire("admin1", "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, null, null);
        List<Bibliothecaire> bibliothecaires = List.of(bib);

        when(bibliothecaireRepo.findByNomContainingIgnoreCase("pont")).thenReturn(bibliothecaires);

        // Act
        List<Bibliothecaire> result = bibliothecaireService.getByNomContainingIgnoreCase("pont");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(bibliothecaireRepo, times(1)).findByNomContainingIgnoreCase("pont");
    }
}