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

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.LivreRepo;

@SpringBootTest
public class LivreServiceTest {

    @Mock
    private LivreRepo livreRepo;

    @InjectMocks
    private LivreService livreService;
    private IsbnGeneratorService isbnGeneratorService;
    private DocumentRepo documentRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLivres() {
        Livre livre1 = new Livre(1, "Title1", 300, LocalDate.of(2020, 1, 1), LocalDateTime.now(), LocalDateTime.now(), new Editeur(), null);
        Livre livre2 = new Livre(2, "Title2", 400, LocalDate.of(2021, 2, 2), LocalDateTime.now(), LocalDateTime.now(), new Editeur(), null);
        when(livreRepo.findAll(Sort.by(Sort.Direction.ASC, "titreLivre"))).thenReturn(Arrays.asList(livre1, livre2));
    
        List<Livre> livres = livreService.getAllLivres();
    
        assertNotNull(livres);
        assertEquals(2, livres.size());
        verify(livreRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "titreLivre"));
    }

    @Test
    void testGetLivreById() {
        Livre livre = new Livre(1, "Title1", 300, LocalDate.of(2020, 1, 1), LocalDateTime.now(), LocalDateTime.now(), new Editeur(), null);
        when(livreRepo.findById(1)).thenReturn(Optional.of(livre));

        Livre result = livreService.getLivreById(1);

        assertNotNull(result);
        assertEquals(1, result.getIdDocument());
        assertEquals("Title1", result.getTitreLivre());
        verify(livreRepo, times(1)).findById(1);
    }

    @Test
    void testSaveLivre() {
        Livre livre = new Livre(null, "New Title", 200, LocalDate.of(2022, 3, 3), null, null, new Editeur(), null);
        Livre savedLivre = new Livre(1, "New Title", 200, LocalDate.of(2022, 3, 3), LocalDateTime.now(), LocalDateTime.now(), new Editeur(), null);
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.setId(1);

        when(isbnGeneratorService.generateNextIsbn()).thenReturn("ISBN-123");
        when(documentRepo.save(any(Document.class))).thenReturn(new Document() {{ setIdDocument(1); }});
        when(livreRepo.save(any(Livre.class))).thenReturn(savedLivre);

        Livre result = livreService.saveLivre(livre, bibliotheque);

        assertNotNull(result);
        assertEquals(1, result.getIdDocument());
        assertEquals("New Title", result.getTitreLivre());
        verify(livreRepo, times(1)).save(any(Livre.class));
    }

    @Test
    void testUpdateLivre() {
        Livre livre = new Livre(1, "Updated Title", 250, LocalDate.of(2023, 4, 4), LocalDateTime.now(), LocalDateTime.now(), new Editeur(), null);
        when(livreRepo.save(any(Livre.class))).thenReturn(livre);

        Livre result = livreService.updateLivre(livre);

        assertNotNull(result);
        assertEquals(1, result.getIdDocument());
        assertEquals("Updated Title", result.getTitreLivre());
        verify(livreRepo, times(1)).save(any(Livre.class));
    }

    @Test
    void testDeleteLivreById() {
        doNothing().when(livreRepo).deleteById(1);

        livreService.deleteLivreById(1);

        verify(livreRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetByTitreContainingIgnoreCase() {
        Livre livre1 = new Livre(1, "Title1", 300, LocalDate.of(2020, 1, 1), LocalDateTime.now(), LocalDateTime.now(), new Editeur(), null);
        Livre livre2 = new Livre(2, "Another Title1", 400, LocalDate.of(2021, 2, 2), LocalDateTime.now(), LocalDateTime.now(), new Editeur(), null);
        when(livreRepo.findByTitreLivreContainingIgnoreCase("title1")).thenReturn(Arrays.asList(livre1, livre2));

        List<Livre> livres = livreService.getByTitreContainingIgnoreCase("title1");

        assertNotNull(livres);
        assertEquals(2, livres.size());
        verify(livreRepo, times(1)).findByTitreLivreContainingIgnoreCase("title1");
    }
}