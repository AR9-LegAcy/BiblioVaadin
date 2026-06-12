package com.usmb.but3.td4biblio.service;

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

    public List<Bibliotheque> getBibliothequesByAdresseRue(String adresseRue) {
        return bibliothequeRepo.findByAdresseRue(adresseRue);
    }

    public List<Bibliotheque> getBibliothequesByAdresseVille(String adresseVille) {
        return bibliothequeRepo.findByAdresseVille(adresseVille);
    }

    public List<Bibliotheque> getBibliothequesByCodePostal(String codePostal) {
        return bibliothequeRepo.findByCodePostal(codePostal);
    }

    public List<Bibliotheque> getByHoraires(String horaires) {
        return bibliothequeRepo.findByHoraires(horaires);
    }

    public List<Bibliotheque> getBibliothequesByNomAndAdresseRue(String nom, String adresseRue) {
        return bibliothequeRepo.findByNomAndAdresseRue(nom, adresseRue);
    }

    public List<Bibliotheque> getBibliothequesByNomLikeAndAdresseVille(String nom, String adresseVille) {
        return bibliothequeRepo.findByNomLikeAndAdresseVille(nom, adresseVille);
    }

    public List<Bibliotheque> getBibliothequesByNomLikeAndCodePostal(String nom, String codePostal) {
        return bibliothequeRepo.findByNomLikeAndCodePostal(nom, codePostal);
    }

    public List<Bibliotheque> getByNomLikeAndHoraires(String nom, String horaires) {
        return bibliothequeRepo.findByNomLikeAndHoraires(nom, horaires);
    }
}