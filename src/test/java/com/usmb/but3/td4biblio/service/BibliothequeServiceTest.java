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

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;

@SpringBootTest
class BibliothequeServiceTest {

    @Mock
    private BibliothequeRepo bibliothequeRepo;

    @InjectMocks
    private BibliothequeService bibliothequeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBibliotheques() {
        Bibliotheque biblio1 = new Bibliotheque(1, "Bibliotheque1", "Ville1", "Rue1", "75001", "9h-18h", LocalDateTime.now(), LocalDateTime.now(), null, null);
        Bibliotheque biblio2 = new Bibliotheque(2, "Bibliotheque2", "Ville2", "Rue2", "75002", "10h-19h", LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(bibliothequeRepo.findAll(Sort.by(Sort.Direction.ASC, "nom")))
            .thenReturn(Arrays.asList(biblio1, biblio2));

        List<Bibliotheque> bibliotheques = bibliothequeService.getAllBibliotheques();

        assertEquals(2, bibliotheques.size());
        assertEquals("Bibliotheque1", bibliotheques.get(0).getNom());
        assertEquals("Bibliotheque2", bibliotheques.get(1).getNom());
        verify(bibliothequeRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    @Test
    void testGetBibliothequeById() {
        Bibliotheque biblio = new Bibliotheque(1, "Bibliotheque1", "Ville1", "Rue1", "75001", "9h-18h", LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(biblio));

        Bibliotheque result = bibliothequeService.getBibliothequeById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Bibliotheque1", result.getNom());
        verify(bibliothequeRepo, times(1)).findById(1);
    }

    @Test
    void testGetBibliothequeByIdNotFound() {
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.empty());

        Bibliotheque result = bibliothequeService.getBibliothequeById(1);

        assertNull(result);
        verify(bibliothequeRepo, times(1)).findById(1);
    }

    @Test
    void testSaveBibliotheque() {
        Bibliotheque biblio = new Bibliotheque(null, "New Bibliotheque", "Ville", "Rue", "75001", "9h-18h", null, null, null, null);
        Bibliotheque savedBiblio = new Bibliotheque(1, "New Bibliotheque", "Ville", "Rue", "75001", "9h-18h", LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(bibliothequeRepo.save(any(Bibliotheque.class))).thenReturn(savedBiblio);

        Bibliotheque result = bibliothequeService.saveBibliotheque(biblio);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("New Bibliotheque", result.getNom());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(bibliothequeRepo, times(1)).save(any(Bibliotheque.class));
    }

    @Test
    void testUpdateBibliotheque() {
        Bibliotheque biblio = new Bibliotheque(1, "Updated Bibliotheque", "Ville", "Rue", "75001", "10h-19h", LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(bibliothequeRepo.save(any(Bibliotheque.class))).thenReturn(biblio);

        Bibliotheque result = bibliothequeService.updateBibliotheque(biblio);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Updated Bibliotheque", result.getNom());
        assertNotNull(result.getUpdatedAt());
        verify(bibliothequeRepo, times(1)).save(any(Bibliotheque.class));
    }

    @Test
    void testDeleteBibliothequeById() {
        doNothing().when(bibliothequeRepo).deleteById(1);

        bibliothequeService.deleteBibliothequeById(1);

        verify(bibliothequeRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetBibliothequesByNom() {
        Bibliotheque biblio = new Bibliotheque(1, "Bibliotheque1", "Ville1", "Rue1", "75001", "9h-18h", LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(bibliothequeRepo.findByNom("Bibliotheque1")).thenReturn(Arrays.asList(biblio));

        List<Bibliotheque> bibliotheques = bibliothequeService.getBibliothequesByNom("Bibliotheque1");

        assertEquals(1, bibliotheques.size());
        assertEquals("Bibliotheque1", bibliotheques.get(0).getNom());
        verify(bibliothequeRepo, times(1)).findByNom("Bibliotheque1");
    }

    @Test
    void testGetBibliothequesByNomStartWithIgnoreCase() {
        Bibliotheque biblio = new Bibliotheque(1, "Bibliotheque1", "Ville1", "Rue1", "75001", "9h-18h", LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(bibliothequeRepo.findByNomStartsWithIgnoreCase("Biblio")).thenReturn(Arrays.asList(biblio));

        List<Bibliotheque> bibliotheques = bibliothequeService.getBibliothequesByNomStartWithIgnoreCase("Biblio");

        assertEquals(1, bibliotheques.size());
        verify(bibliothequeRepo, times(1)).findByNomStartsWithIgnoreCase("Biblio");
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        Bibliotheque biblio = new Bibliotheque(1, "Bibliotheque1", "Ville1", "Rue1", "75001", "9h-18h", LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(bibliothequeRepo.findByNomContainingIgnoreCase("theque")).thenReturn(Arrays.asList(biblio));

        List<Bibliotheque> bibliotheques = bibliothequeService.getByNomContainingIgnoreCase("theque");

        assertEquals(1, bibliotheques.size());
        verify(bibliothequeRepo, times(1)).findByNomContainingIgnoreCase("theque");
    }
}