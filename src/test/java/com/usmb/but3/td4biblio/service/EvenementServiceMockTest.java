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
        Evenement result = evenementService.getEvenementById(1);

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
        //Arrange
        Evenement eventToUpdate = new Evenement(1, "Dédicace", "Rencontre avec l'auteur", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), LocalDateTime.now(), null, null, null);
        when(evenementRepo.save(any(Evenement.class))).thenReturn(eventToUpdate);

        //Act
        Evenement result = evenementService.updateEvenement(eventToUpdate);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        verify(evenementRepo, times(1)).save(any(Evenement.class));
    }
}
