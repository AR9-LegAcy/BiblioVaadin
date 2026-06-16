package com.usmb.but3.td4biblio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.repository.TypeEvenementRepo;

@SpringBootTest
class TypeEvenementServiceTest {

    @Mock
    private TypeEvenementRepo typeEvenementRepo;

    @InjectMocks
    private TypeEvenementService typeEvenementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTypeEvenements() {
        TypeEvenement type1 = new TypeEvenement();
        type1.setNom("Type1");
        TypeEvenement type2 = new TypeEvenement();
        type2.setNom("Type2");

        when(typeEvenementRepo.findAll(Sort.by(Sort.Direction.ASC, "nom"))).thenReturn(Arrays.asList(type1, type2));

        List<TypeEvenement> result = typeEvenementService.getAllTypeEvenements();

        assertEquals(2, result.size());
        assertEquals("Type1", result.get(0).getNom());
        assertEquals("Type2", result.get(1).getNom());
        verify(typeEvenementRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    @Test
    void testGetTypeEvenementById() {
        TypeEvenement type = new TypeEvenement();
        type.setId(1);
        type.setNom("Type1");

        when(typeEvenementRepo.findById(1)).thenReturn(Optional.of(type));

        TypeEvenement result = typeEvenementService.getTypeEvenementById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Type1", result.getNom());
        verify(typeEvenementRepo, times(1)).findById(1);
    }

    @Test
    void testSaveTypeEvenement() {
        TypeEvenement type = new TypeEvenement();
        type.setNom("Type1");

        when(typeEvenementRepo.save(any(TypeEvenement.class))).thenReturn(type);

        TypeEvenement result = typeEvenementService.saveTypeEvenement(type);

        assertNotNull(result);
        assertEquals("Type1", result.getNom());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(typeEvenementRepo, times(1)).save(type);
    }

    @Test
    void testUpdateTypeEvenement() {
        TypeEvenement type = new TypeEvenement();
        type.setId(1);
        type.setNom("Type1");

        when(typeEvenementRepo.save(any(TypeEvenement.class))).thenReturn(type);

        TypeEvenement result = typeEvenementService.updateTypeEvenement(type);

        assertNotNull(result);
        assertEquals("Type1", result.getNom());
        assertNotNull(result.getUpdatedAt());
        verify(typeEvenementRepo, times(1)).save(type);
    }

    @Test
    void testDeleteTypeEvenementById() {
        doNothing().when(typeEvenementRepo).deleteById(1);

        typeEvenementService.deleteTypeEvenementById(1);

        verify(typeEvenementRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetTypeEvenementsByNom() {
        TypeEvenement type = new TypeEvenement();
        type.setNom("Type1");

        when(typeEvenementRepo.findByNom("Type1")).thenReturn(Arrays.asList(type));

        List<TypeEvenement> result = typeEvenementService.getTypeEvenementsByNom("Type1");

        assertEquals(1, result.size());
        assertEquals("Type1", result.get(0).getNom());
        verify(typeEvenementRepo, times(1)).findByNom("Type1");
    }

    @Test
    void testGetTypeEvenementsByNomLike() {
        TypeEvenement type = new TypeEvenement();
        type.setNom("Type1");

        when(typeEvenementRepo.findByNomLike("%Type%")).thenReturn(Arrays.asList(type));

        List<TypeEvenement> result = typeEvenementService.getTypeEvenementsByNomLike("%Type%");

        assertEquals(1, result.size());
        assertEquals("Type1", result.get(0).getNom());
        verify(typeEvenementRepo, times(1)).findByNomLike("%Type%");
    }
}
