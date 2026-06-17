package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmprunteurServiceMockTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmprunteurRepo emprunteurRepo;

    @InjectMocks
    private EmprunteurService emprunteurService;

    @Test
    void testGetAllEmprunteurs() {
        // Arrange
        Emprunteur emp1 = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        Emprunteur emp2 = new Emprunteur(2, "Martin", "Sophie", "20 avenue Lyon", "Lyon", "69000", "sophie@example.com", LocalDate.of(1988, 3, 22), null, null, "password456", null, null, null);
        List<Emprunteur> emprunteurs = List.of(emp1, emp2);

        when(emprunteurRepo.findAll(any(Sort.class))).thenReturn(emprunteurs);

        // Act
        List<Emprunteur> result = emprunteurService.getAllEmprunteurs();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(emp1, emp2);
        verify(emprunteurRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetEmprunteurById_Found() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(emp));

        // Act
        Emprunteur result = emprunteurService.getEmprunteurById(1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCarteEmprunteur()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Dupont");
        assertThat(result.getPrenom()).isEqualTo("Jean");
        verify(emprunteurRepo, times(1)).findById(1);
    }

    @Test
    void testGetEmprunteurById_NotFound() {
        // Arrange
        when(emprunteurRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Emprunteur result = emprunteurService.getEmprunteurById(999);

        // Assert
        assertThat(result).isNull();
        verify(emprunteurRepo, times(1)).findById(999);
    }

    @Test
    void testSaveEmprunteur() {
        // Arrange
        Emprunteur emp = new Emprunteur();
        emp.setCarteEmprunteur(3);
        emp.setNom("Bernard");
        emp.setPrenom("Marie");
        emp.setAdresseRue("30 Boulevard des Invalides");
        emp.setAdresseVille("Paris");
        emp.setAdresseCodePostal("75007");
        emp.setEmail("marie@example.com");
        emp.setDateNaissance(LocalDate.of(1995, 7, 10));

        String expectedPassword = "10071995";
        Emprunteur savedEmp = new Emprunteur(3, "Bernard", "Marie", "30 Boulevard des Invalides", "Paris", "75007", "marie@example.com", LocalDate.of(1995, 7, 10), null, null, "$2a$10$encoded_password", LocalDateTime.now(), LocalDateTime.now(), null);

        when(passwordEncoder.encode(expectedPassword)).thenReturn("$2a$10$encoded_password");
        when(emprunteurRepo.save(any(Emprunteur.class))).thenReturn(savedEmp);

        // Act
        Emprunteur result = emprunteurService.saveEmprunteur(emp);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCarteEmprunteur()).isEqualTo(3);
        assertThat(result.getMotDePasse()).isEqualTo("$2a$10$encoded_password");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(passwordEncoder, times(1)).encode(expectedPassword);
        verify(emprunteurRepo, times(1)).save(any(Emprunteur.class));
    }

    @Test
    void testSaveEmprunteur_WithExistingCreatedAt() {
        // Arrange
        LocalDateTime existingCreatedAt = LocalDateTime.of(2020, 1, 15, 10, 30, 0);
        Emprunteur emp = new Emprunteur();
        emp.setCarteEmprunteur(4);
        emp.setNom("Leclerc");
        emp.setPrenom("Pierre");
        emp.setAdresseRue("40 rue de la Paix");
        emp.setAdresseVille("Marseille");
        emp.setAdresseCodePostal("13000");
        emp.setEmail("pierre@example.com");
        emp.setDateNaissance(LocalDate.of(1985, 12, 25));
        emp.setCreatedAt(existingCreatedAt);

        String expectedPassword = "25121985";
        Emprunteur savedEmp = new Emprunteur(4, "Leclerc", "Pierre", "40 rue de la Paix", "Marseille", "13000", "pierre@example.com", LocalDate.of(1985, 12, 25), null, null, "$2a$10$encoded_password", existingCreatedAt, LocalDateTime.now(), null);

        when(passwordEncoder.encode(expectedPassword)).thenReturn("$2a$10$encoded_password");
        when(emprunteurRepo.save(any(Emprunteur.class))).thenReturn(savedEmp);

        // Act
        Emprunteur result = emprunteurService.saveEmprunteur(emp);

        // Assert
        assertThat(result.getCreatedAt()).isEqualTo(existingCreatedAt);
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(emprunteurRepo, times(1)).save(any(Emprunteur.class));
    }

    @Test
    void testUpdateEmprunteur() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean.new@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        
        Emprunteur updatedEmp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean.new@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, LocalDateTime.now(), null);

        when(emprunteurRepo.save(emp)).thenReturn(updatedEmp);

        // Act
        Emprunteur result = emprunteurService.updateEmprunteur(emp);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(emprunteurRepo, times(1)).save(emp);
    }

    @Test
    void testUpdateEmprunteur_WithBlankPassword() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "", null, null, null);
        
        Emprunteur oldEmp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "$2a$10$old_password", null, null, null);
        
        Emprunteur updatedEmp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "$2a$10$old_password", null, LocalDateTime.now(), null);

        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(oldEmp));
        when(emprunteurRepo.save(any(Emprunteur.class))).thenReturn(updatedEmp);

        // Act
        Emprunteur result = emprunteurService.updateEmprunteur(emp);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getMotDePasse()).isEqualTo("$2a$10$old_password");
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(emprunteurRepo, times(1)).findById(1);
        verify(emprunteurRepo, times(1)).save(any(Emprunteur.class));
    }

    @Test
    void testUpdateEmprunteur_WithNullPassword() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, null, null, null, null);
        
        Emprunteur oldEmp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "$2a$10$old_password", null, null, null);
        
        Emprunteur updatedEmp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "$2a$10$old_password", null, LocalDateTime.now(), null);

        when(emprunteurRepo.findById(1)).thenReturn(Optional.of(oldEmp));
        when(emprunteurRepo.save(any(Emprunteur.class))).thenReturn(updatedEmp);

        // Act
        Emprunteur result = emprunteurService.updateEmprunteur(emp);

        // Assert
        assertThat(result.getMotDePasse()).isEqualTo("$2a$10$old_password");
        verify(emprunteurRepo, times(1)).findById(1);
        verify(emprunteurRepo, times(1)).save(any(Emprunteur.class));
    }

    @Test
    void testDeleteEmprunteurById() {
        // Act
        emprunteurService.deleteEmprunteurById(1);

        // Assert
        verify(emprunteurRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetEmprunteursByNom() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        List<Emprunteur> emprunteurs = List.of(emp);

        when(emprunteurRepo.findByNom("Dupont")).thenReturn(emprunteurs);

        // Act
        List<Emprunteur> result = emprunteurService.getEmprunteursByNom("Dupont");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(emprunteurRepo, times(1)).findByNom("Dupont");
    }

    @Test
    void testGetEmprunteursByNomAndPrenom() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        List<Emprunteur> emprunteurs = List.of(emp);

        when(emprunteurRepo.findByNomAndPrenom("Dupont", "Jean")).thenReturn(emprunteurs);

        // Act
        List<Emprunteur> result = emprunteurService.getEmprunteursByNomAndPrenom("Dupont", "Jean");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        assertThat(result.get(0).getPrenom()).isEqualTo("Jean");
        verify(emprunteurRepo, times(1)).findByNomAndPrenom("Dupont", "Jean");
    }

    @Test
    void testGetEmprunteursByNomLikeAndPrenomLike() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        List<Emprunteur> emprunteurs = List.of(emp);

        when(emprunteurRepo.findByNomLikeAndPrenomLike("%Dupon%", "%Jea%")).thenReturn(emprunteurs);

        // Act
        List<Emprunteur> result = emprunteurService.getEmprunteursByNomLikeAndPrenomLike("Dupon", "Jea");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(emprunteurRepo, times(1)).findByNomLikeAndPrenomLike("%Dupon%", "%Jea%");
    }

    @Test
    void testGetEmprunteursByNomStartWithIgnoreCase() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        List<Emprunteur> emprunteurs = List.of(emp);

        when(emprunteurRepo.findByNomStartsWithIgnoreCase("dup")).thenReturn(emprunteurs);

        // Act
        List<Emprunteur> result = emprunteurService.getEmprunteursByNomStartWithIgnoreCase("dup");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(emprunteurRepo, times(1)).findByNomStartsWithIgnoreCase("dup");
    }

    @Test
    void testGetEmprunteursByNomContainingIgnoreCase() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        List<Emprunteur> emprunteurs = List.of(emp);

        when(emprunteurRepo.findByNomContainingIgnoreCase("pont")).thenReturn(emprunteurs);

        // Act
        List<Emprunteur> result = emprunteurService.getEmprunteursByNomContainingIgnoreCase("pont");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(emprunteurRepo, times(1)).findByNomContainingIgnoreCase("pont");
    }

    @Test
    void testGetEmprunteursByPrenomContainingIgnoreCase() {
        // Arrange
        Emprunteur emp = new Emprunteur(1, "Dupont", "Jean", "10 rue de Paris", "Paris", "75001", "jean@example.com", LocalDate.of(1990, 5, 15), null, null, "password123", null, null, null);
        List<Emprunteur> emprunteurs = List.of(emp);

        when(emprunteurRepo.findByPrenomContainingIgnoreCase("jea")).thenReturn(emprunteurs);

        // Act
        List<Emprunteur> result = emprunteurService.getEmprunteursByPrenomContainingIgnoreCase("jea");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPrenom()).isEqualTo("Jean");
        verify(emprunteurRepo, times(1)).findByPrenomContainingIgnoreCase("jea");
    }
}