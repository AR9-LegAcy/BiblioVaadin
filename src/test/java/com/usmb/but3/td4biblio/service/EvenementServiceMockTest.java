package com.usmb.but3.td4biblio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.repository.EvenementRepo;

@ExtendWith(MockitoExtension.class)
public class EvenementServiceMockTest {

    @Mock
    private EvenementRepo evenementRepo;

    @InjectMocks
    private EvenementService evenementService;

    @Test
    void testGetAllEvenements() {
        //Arrange
        Evenement event1 = new Evenement(1, "Dédicace", "Rencontre avec l'auteur", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), LocalDateTime.now(), null, null, null);
        Evenement event2 = new Evenement(1, "Session lecture", "Session lecture de livre", LocalDate.of(2026, 8, 5), LocalDate.of(2026, 8, 5), LocalDateTime.now(), null, null, null);
        List<Evenement> evenements = List.of(event1, event2);

        when(evenementRepo.findAll(any(Sort.class))).thenReturn(evenements);

        //Act
        List<Evenement> result = evenementService.getAllEvenements();

        //Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(event1, event2);
        verify(evenementRepo, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetEvenementById_Found() { 
        //Arrange
        Evenement event = new Evenement(1, "Dédicace", "Rencontre avec l'auteur", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), LocalDateTime.now(), null, null, null);
        when(evenementRepo.findById(1)).thenReturn(Optional.of(event));

        //Act
        Evenement result = evenementService.getEvenementById(1);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        verify(evenementRepo, times(1)).findById(1);
    }

    @Test
    void testGetEvenementById_NotFound() {
        //Arrange
        when(evenementRepo.findById(999)).thenReturn(Optional.empty());

        //Act
        Evenement result = evenementService.getEvenementById(999);

        //Assert
        assertThat(result).isNull();
        verify(evenementRepo, times(1)).findById(999);
    }

    @Test
    void testSaveEvenement() { 
        //Arrange        
        Evenement savedEvent = new Evenement(1, "Dédicace", "Rencontre avec l'auteur", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), LocalDateTime.now(), null, null, null);
        when(evenementRepo.save(any(Evenement.class))).thenReturn(savedEvent);

        //Act
        Evenement result = evenementService.saveEvenement(savedEvent);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        verify(evenementRepo, times(1)).save(any(Evenement.class));
    }

    @Test
    void testSaveEvenement_WithExistingCreatedAt() {
        //Arrange
        LocalDateTime existingCreatedAt = LocalDateTime.of(2026, 6, 1, 10, 0);
        Evenement event = new Evenement(1, "Dédicace", "Rencontre avec l'auteur", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), existingCreatedAt, null, null, null);
        when(evenementRepo.save(any(Evenement.class))).thenReturn(event);

        //Act
        Evenement result = evenementService.saveEvenement(event);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getCreatedAt()).isEqualTo(existingCreatedAt);
        verify(evenementRepo, times(1)).save(any(Evenement.class));
    }

    @Test
    void testUpdateEvenement() {
        // Arrange
        Evenement event = new Evenement(
                1,
                "Dédicace",
                "Nouvelle description",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalDateTime.now().minusDays(10),
                null,
                null,
                null);

        Evenement updatedEvent = new Evenement(
                1,
                "Dédicace",
                "Nouvelle description",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                event.getCreatedAt(),
                LocalDateTime.now(),
                null,
                null);

        when(evenementRepo.save(event)).thenReturn(updatedEvent);

        // Act
        Evenement result = evenementService.updateEvenement(event);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(evenementRepo, times(1)).save(event);
    }

    @Test
    void testDeleteEvenementById() {
        // Act
        evenementService.deleteEvenementById(1);

        // Assert
        verify(evenementRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetEvenementByTitre() {
        // Arrange
        Evenement event = new Evenement(
                1,
                "Dédicace",
                "Rencontre auteur",
                LocalDate.now(),
                LocalDate.now(),
                LocalDateTime.now(),
                null,
                null,
                null);

        List<Evenement> events = List.of(event);

        when(evenementRepo.findByTitre("Dédicace"))
                .thenReturn(events);

        // Act
        List<Evenement> result =
                evenementService.getEvenementsByTitre("Dédicace");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("Dédicace");

        verify(evenementRepo, times(1))
                .findByTitre("Dédicace");
    }

    @Test
    void testGetEvenementByDateDebut() {
        // Arrange
        LocalDate date = LocalDate.of(2026, 7, 1);

        Evenement event = new Evenement(
                1,
                "Dédicace",
                "Description",
                date,
                date.plusDays(2),
                LocalDateTime.now(),
                null,
                null,
                null);

        when(evenementRepo.findByDateDebut(date))
                .thenReturn(List.of(event));

        // Act
        List<Evenement> result =
                evenementService.getEvenementsByDateDebut(date);

        // Assert
        assertThat(result).hasSize(1);

        verify(evenementRepo, times(1))
                .findByDateDebut(date);
    }

    @Test
    void testGetEvenementByDateFin() {
        // Arrange
        LocalDate date = LocalDate.of(2026, 7, 3);

        Evenement event = new Evenement(
                1,
                "Dédicace",
                "Description",
                date.minusDays(2),
                date,
                LocalDateTime.now(),
                null,
                null,
                null);

        when(evenementRepo.findByDateFin(date))
                .thenReturn(List.of(event));

        // Act
        List<Evenement> result =
                evenementService.getEvenementsByDateFin(date);

        // Assert
        assertThat(result).hasSize(1);

        verify(evenementRepo, times(1))
                .findByDateFin(date);
    }

    @Test
    void testGetEvenementsByTitreLikeAndDateDebut() {
        // Arrange
        String titre = "%Dédi%";
        LocalDate date = LocalDate.of(2026, 7, 1);

        Evenement event = new Evenement(
                1,
                "Dédicace",
                "Description",
                date,
                date.plusDays(2),
                LocalDateTime.now(),
                null,
                null,
                null);

        when(evenementRepo.findByTitreLikeAndDateDebut(titre, date))
                .thenReturn(List.of(event));

        // Act
        List<Evenement> result =
                evenementService.getEvenementsByTitreLikeAndDateDebut(titre, date);

        // Assert
        assertThat(result).hasSize(1);

        verify(evenementRepo, times(1))
                .findByTitreLikeAndDateDebut(titre, date);
    }

    @Test
    void testGetEvenementsByTitreLikeAndDateFin() {
        // Arrange
        String titre = "%Dédi%";
        LocalDate date = LocalDate.of(2026, 7, 3);

        Evenement event = new Evenement(
                1,
                "Dédicace",
                "Description",
                date.minusDays(2),
                date,
                LocalDateTime.now(),
                null,
                null,
                null);

        when(evenementRepo.findByTitreLikeAndDateFin(titre, date))
                .thenReturn(List.of(event));

        // Act
        List<Evenement> result =
                evenementService.getEvenementsByTitreLikeAndDateFin(titre, date);

        // Assert
        assertThat(result).hasSize(1);

        verify(evenementRepo, times(1))
                .findByTitreLikeAndDateFin(titre, date);
    }

    @Test
    void testGetEvenementsByTitreLike() {
       // Arrange
       Evenement event = new Evenement(
               1,
               "Dédicace",
               "Description",
               LocalDate.now(),
               LocalDate.now(),
               LocalDateTime.now(),
               null,
               null,
               null);

       when(evenementRepo.findByTitreLike("%Dédi%"))
               .thenReturn(List.of(event));

       // Act
       List<Evenement> result =
               evenementService.getEvenementsByTitreLike("%Dédi%");

       // Assert
       assertThat(result).hasSize(1);
       assertThat(result.get(0).getTitre()).isEqualTo("Dédicace");

       verify(evenementRepo, times(1))
               .findByTitreLike("%Dédi%");
    }

    @Test
    void testGetEvenementsFuturs() {
        // Arrange
        Evenement event = new Evenement(
                1,
                "Salon du livre",
                "Description",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                LocalDateTime.now(),
                null,
                null,
                null);

        when(evenementRepo.findByDateDebutAfterOrderByDateDebutAsc(any(LocalDate.class)))
                .thenReturn(List.of(event));

        // Act
        List<Evenement> result = evenementService.getEvenementsFuturs();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("Salon du livre");

        verify(evenementRepo, times(1))
                .findByDateDebutAfterOrderByDateDebutAsc(any(LocalDate.class));
    }
}
