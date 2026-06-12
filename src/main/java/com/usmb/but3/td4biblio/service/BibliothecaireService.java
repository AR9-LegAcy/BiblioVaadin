package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.repository.BibliothecaireRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BibliothecaireService {

    private final BibliothecaireRepo bibliothecaireRepo;

    public List<Bibliothecaire> getAllBibliothecaires() {
        return bibliothecaireRepo.findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom"));
    }

    public Bibliothecaire getBibliothecaireByPseudo(String pseudo) {
        return bibliothecaireRepo.findByPseudo(pseudo).getFirst();
    }

    public Bibliothecaire saveBibliothecaire(Bibliothecaire bibliothecaire) {
        if (bibliothecaire.getCreatedAt() == null) {
            bibliothecaire.setCreatedAt(LocalDateTime.now());
        }
        bibliothecaire.setUpdatedAt(LocalDateTime.now());
        return bibliothecaireRepo.save(bibliothecaire);
    }

    public Bibliothecaire updateBibliothecaire(Bibliothecaire bibliothecaire) {
        bibliothecaire.setUpdatedAt(LocalDateTime.now());
        return bibliothecaireRepo.save(bibliothecaire);
    }

    public void deleteBibliothecaireByPseudo(String pseudo) {
        bibliothecaireRepo.deleteByPseudo(pseudo);
    }

    public List<Bibliothecaire> getBibliothecairesByNom(String nom) {
        return bibliothecaireRepo.findByNom(nom);
    }

    public List<Bibliothecaire> getBibliothecairesByNomAndPrenom(String nom, String prenom) {
        return bibliothecaireRepo.findByNomAndPrenom(nom, prenom);
    }

    public List<Bibliothecaire> getBibliothecairesByNomLikeAndPrenomLike(String nom, String prenom) {
        return bibliothecaireRepo.findByNomLikeAndPrenomLike(nom, prenom);
    }

    public List<Bibliothecaire> getBibliothecairesByNomStartWithIgnoreCase(String filter) {
        return bibliothecaireRepo.findByNomStartsWithIgnoreCase(filter);
    }

    public List<Bibliothecaire> getByNomContainingIgnoreCase(String filter) {
        return bibliothecaireRepo.findByNomContainingIgnoreCase(filter);
    }
}