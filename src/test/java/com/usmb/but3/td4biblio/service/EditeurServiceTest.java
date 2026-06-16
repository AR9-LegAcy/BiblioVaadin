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

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.repository.EditeurRepo;

@SpringBootTest
class EditeurServiceTest {

    @Mock
    private EditeurRepo editeurRepo;

    @InjectMocks
    private EditeurService editeurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEditeurs() {
        Editeur editeur1 = new Editeur(1, "Editeur1", "Rue1", "www.editeur1.com", "wikipedia1", LocalDateTime.now(), LocalDateTime.now(), null);
        Editeur editeur2 = new Editeur(2, "Editeur2", "Rue2", "www.editeur2.com", "wikipedia2", LocalDateTime.now(), LocalDateTime.now(), null);
        when(editeurRepo.findAll(Sort.by(Sort.Direction.ASC, "nom")))
            .thenReturn(Arrays.asList(editeur1, editeur2));

        List<Editeur> editeurs = editeurService.getAllEditeurs();

        assertEquals(2, editeurs.size());
        assertEquals("Editeur1", editeurs.get(0).getNom());
        assertEquals("Editeur2", editeurs.get(1).getNom());
        verify(editeurRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    @Test
    void testGetEditeurById() {
        Editeur editeur = new Editeur(1, "Editeur1", "Rue1", "www.editeur1.com", "wikipedia1", LocalDateTime.now(), LocalDateTime.now(), null);
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));

        Editeur result = editeurService.getEditeurById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Editeur1", result.getNom());
        verify(editeurRepo, times(1)).findById(1);
    }

    @Test
    void testGetEditeurByIdNotFound() {
        when(editeurRepo.findById(1)).thenReturn(Optional.empty());

        Editeur result = editeurService.getEditeurById(1);

        assertNull(result);
        verify(editeurRepo, times(1)).findById(1);
    }

    @Test
    void testSaveEditeur() {
        Editeur editeur = new Editeur(null, "New Editeur", "Rue", "www.editeur.com", "wikipedia", null, null, null);
        Editeur savedEditeur = new Editeur(1, "New Editeur", "Rue", "www.editeur.com", "wikipedia", LocalDateTime.now(), LocalDateTime.now(), null);
        when(editeurRepo.save(any(Editeur.class))).thenReturn(savedEditeur);

        Editeur result = editeurService.saveEditeur(editeur);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("New Editeur", result.getNom());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(editeurRepo, times(1)).save(any(Editeur.class));
    }

    @Test
    void testUpdateEditeur() {
        Editeur editeur = new Editeur(1, "Updated Editeur", "Rue Updated", "www.editeur-updated.com", "wikipedia-updated", LocalDateTime.now(), LocalDateTime.now(), null);
        when(editeurRepo.save(any(Editeur.class))).thenReturn(editeur);

        Editeur result = editeurService.updateEditeur(editeur);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Updated Editeur", result.getNom());
        assertNotNull(result.getUpdatedAt());
        verify(editeurRepo, times(1)).save(any(Editeur.class));
    }

    @Test
    void testDeleteEditeurById() {
        doNothing().when(editeurRepo).deleteById(1);

        editeurService.deleteEditeurById(1);

        verify(editeurRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetEditeursByNom() {
        Editeur editeur = new Editeur(1, "Editeur1", "Rue1", "www.editeur1.com", "wikipedia1", LocalDateTime.now(), LocalDateTime.now(), null);
        when(editeurRepo.findByNom("Editeur1")).thenReturn(Arrays.asList(editeur));

        List<Editeur> editeurs = editeurService.getEditeursByNom("Editeur1");

        assertEquals(1, editeurs.size());
        assertEquals("Editeur1", editeurs.get(0).getNom());
        verify(editeurRepo, times(1)).findByNom("Editeur1");
    }

    @Test
    void testGetEditeursByNomStartWithIgnoreCase() {
        Editeur editeur = new Editeur(1, "Editeur1", "Rue1", "www.editeur1.com", "wikipedia1", LocalDateTime.now(), LocalDateTime.now(), null);
        when(editeurRepo.findByNomStartsWithIgnoreCase("Edit")).thenReturn(Arrays.asList(editeur));

        List<Editeur> editeurs = editeurService.getEditeursByNomStartWithIgnoreCase("Edit");

        assertEquals(1, editeurs.size());
        verify(editeurRepo, times(1)).findByNomStartsWithIgnoreCase("Edit");
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        Editeur editeur = new Editeur(1, "Editeur1", "Rue1", "www.editeur1.com", "wikipedia1", LocalDateTime.now(), LocalDateTime.now(), null);
        when(editeurRepo.findByNomContainingIgnoreCase("teur")).thenReturn(Arrays.asList(editeur));

        List<Editeur> editeurs = editeurService.getByNomContainingIgnoreCase("teur");

        assertEquals(1, editeurs.size());
        verify(editeurRepo, times(1)).findByNomContainingIgnoreCase("teur");
    }
}