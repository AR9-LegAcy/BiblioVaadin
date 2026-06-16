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

import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.repository.EvenementRepo;

@SpringBootTest
class EvenementServiceTest {

    @Mock
    private EvenementRepo evenementRepo;

    @InjectMocks
    private EvenementService evenementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvenements() {
        Evenement event1 = new Evenement(1, "Titre1", "Description1", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        Evenement event2 = new Evenement(2, "Titre2", "Description2", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(evenementRepo.findAll(Sort.by(Sort.Direction.ASC, "dateDebut")))
            .thenReturn(Arrays.asList(event1, event2));

        List<Evenement> evenements = evenementService.getAllEvenements();

        assertEquals(2, evenements.size());
        assertEquals("Titre1", evenements.get(0).getTitre());
        assertEquals("Titre2", evenements.get(1).getTitre());
        verify(evenementRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "dateDebut"));
    }

    @Test
    void testGetEvenementById() {
        Evenement evenement = new Evenement(1, "Titre1", "Description1", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(evenementRepo.findById(1)).thenReturn(Optional.of(evenement));

        Evenement result = evenementService.getEvenementById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Titre1", result.getTitre());
        verify(evenementRepo, times(1)).findById(1);
    }

    @Test
    void testGetEvenementByIdNotFound() {
        when(evenementRepo.findById(1)).thenReturn(Optional.empty());

        Evenement result = evenementService.getEvenementById(1);

        assertNull(result);
        verify(evenementRepo, times(1)).findById(1);
    }

    @Test
    void testSaveEvenement() {
        Evenement evenement = new Evenement(null, "New Evenement", "Description", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 10), null, null, null, null);
        Evenement savedEvenement = new Evenement(1, "New Evenement", "Description", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(evenementRepo.save(any(Evenement.class))).thenReturn(savedEvenement);

        Evenement result = evenementService.saveEvenement(evenement);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("New Evenement", result.getTitre());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(evenementRepo, times(1)).save(any(Evenement.class));
    }

    @Test
    void testUpdateEvenement() {
        Evenement evenement = new Evenement(1, "Updated Evenement", "Updated Description", LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(evenementRepo.save(any(Evenement.class))).thenReturn(evenement);

        Evenement result = evenementService.updateEvenement(evenement);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Updated Evenement", result.getTitre());
        assertNotNull(result.getUpdatedAt());
        verify(evenementRepo, times(1)).save(any(Evenement.class));
    }

    @Test
    void testDeleteEvenementById() {
        doNothing().when(evenementRepo).deleteById(1);

        evenementService.deleteEvenementById(1);

        verify(evenementRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetEvenementsByTitre() {
        Evenement evenement = new Evenement(1, "Titre1", "Description1", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(evenementRepo.findByTitre("Titre1")).thenReturn(Arrays.asList(evenement));

        List<Evenement> evenements = evenementService.getEvenementsByTitre("Titre1");

        assertEquals(1, evenements.size());
        assertEquals("Titre1", evenements.get(0).getTitre());
        verify(evenementRepo, times(1)).findByTitre("Titre1");
    }

    @Test
    void testGetEvenementsByTitreLike() {
        Evenement evenement1 = new Evenement(1, "Concert1", "Description1", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        Evenement evenement2 = new Evenement(2, "Another Concert", "Description2", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(evenementRepo.findByTitreLike("%Concert%")).thenReturn(Arrays.asList(evenement1, evenement2));

        List<Evenement> evenements = evenementService.getEvenementsByTitreLike("%Concert%");

        assertEquals(2, evenements.size());
        verify(evenementRepo, times(1)).findByTitreLike("%Concert%");
    }

    @Test
    void testGetEvenementsByDateDebut() {
        Evenement evenement = new Evenement(1, "Titre1", "Description1", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        LocalDate dateDebut = LocalDate.of(2024, 6, 1);
        when(evenementRepo.findByDateDebut(dateDebut)).thenReturn(Arrays.asList(evenement));

        List<Evenement> evenements = evenementService.getEvenementsByDateDebut(dateDebut);

        assertEquals(1, evenements.size());
        assertEquals(dateDebut, evenements.get(0).getDateDebut());
        verify(evenementRepo, times(1)).findByDateDebut(dateDebut);
    }

    @Test
    void testGetEvenementsByDateFin() {
        Evenement evenement = new Evenement(1, "Titre1", "Description1", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        LocalDate dateFin = LocalDate.of(2024, 6, 10);
        when(evenementRepo.findByDateFin(dateFin)).thenReturn(Arrays.asList(evenement));

        List<Evenement> evenements = evenementService.getEvenementsByDateFin(dateFin);

        assertEquals(1, evenements.size());
        assertEquals(dateFin, evenements.get(0).getDateFin());
        verify(evenementRepo, times(1)).findByDateFin(dateFin);
    }

    @Test
    void testGetEvenementsByTitreLikeAndDateDebut() {
        Evenement evenement = new Evenement(1, "Concert1", "Description1", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        LocalDate dateDebut = LocalDate.of(2024, 6, 1);
        when(evenementRepo.findByTitreLikeAndDateDebut("%Concert%", dateDebut)).thenReturn(Arrays.asList(evenement));

        List<Evenement> evenements = evenementService.getEvenementsByTitreLikeAndDateDebut("%Concert%", dateDebut);

        assertEquals(1, evenements.size());
        verify(evenementRepo, times(1)).findByTitreLikeAndDateDebut("%Concert%", dateDebut);
    }

    @Test
    void testGetEvenementsByTitreLikeAndDateFin() {
        Evenement evenement = new Evenement(1, "Concert1", "Description1", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        LocalDate dateFin = LocalDate.of(2024, 6, 10);
        when(evenementRepo.findByTitreLikeAndDateFin("%Concert%", dateFin)).thenReturn(Arrays.asList(evenement));

        List<Evenement> evenements = evenementService.getEvenementsByTitreLikeAndDateFin("%Concert%", dateFin);

        assertEquals(1, evenements.size());
        verify(evenementRepo, times(1)).findByTitreLikeAndDateFin("%Concert%", dateFin);
    }

    @Test
    void testGetEvenementsFuturs() {
        LocalDate today = LocalDate.now();
        Evenement evenement1 = new Evenement(1, "Future Event1", "Description1", today.plusDays(5), today.plusDays(10), LocalDateTime.now(), LocalDateTime.now(), null, null);
        Evenement evenement2 = new Evenement(2, "Future Event2", "Description2", today.plusDays(20), today.plusDays(25), LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(evenementRepo.findByDateDebutAfterOrderByDateDebutAsc(today))
            .thenReturn(Arrays.asList(evenement1, evenement2));

        List<Evenement> evenements = evenementService.getEvenementsFuturs();

        assertEquals(2, evenements.size());
        assertTrue(evenements.get(0).getDateDebut().isAfter(today));
        verify(evenementRepo, times(1)).findByDateDebutAfterOrderByDateDebutAsc(today);
    }

    @Test
    void testGetEvenementsFutursEmpty() {
        LocalDate today = LocalDate.now();
        when(evenementRepo.findByDateDebutAfterOrderByDateDebutAsc(today))
            .thenReturn(Arrays.asList());

        List<Evenement> evenements = evenementService.getEvenementsFuturs();

        assertEquals(0, evenements.size());
        verify(evenementRepo, times(1)).findByDateDebutAfterOrderByDateDebutAsc(today);
    }
}