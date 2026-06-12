package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BibliothequeService {

    private final BibliothequeRepo bibliothequeRepo;

    public List<Bibliotheque> getAllBibliotheques() {
        return bibliothequeRepo.findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    public Bibliotheque getBibliothequeById(Integer id) {
        return bibliothequeRepo.findById(id).orElse(null);
    }

    public Bibliotheque saveBibliotheque(Bibliotheque bibliotheque) {
        if (bibliotheque.getCreatedAt() == null) {
            bibliotheque.setCreatedAt(LocalDateTime.now());
        }
        bibliotheque.setUpdatedAt(LocalDateTime.now());
        return bibliothequeRepo.save(bibliotheque);
    }

    public Bibliotheque updateBibliotheque(Bibliotheque bibliotheque) {
        bibliotheque.setUpdatedAt(LocalDateTime.now());
        return bibliothequeRepo.save(bibliotheque);
    }

    public void deleteBibliothequeById(Integer id) {
        bibliothequeRepo.deleteById(id);
    }

    public List<Bibliotheque> getBibliothequesByNom(String nom) {
        return bibliothequeRepo.findByNom(nom);
    }

    public List<Bibliotheque> getBibliothequesByNomStartWithIgnoreCase(String filter) {
        return bibliothequeRepo.findByNomStartsWithIgnoreCase(filter);
    }

    public List<Bibliotheque> getByNomContainingIgnoreCase(String filter) {
        return bibliothequeRepo.findByNomContainingIgnoreCase(filter);
    }
}