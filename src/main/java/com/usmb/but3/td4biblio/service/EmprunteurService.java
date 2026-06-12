package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmprunteurService {
    private final EmprunteurRepo emprunteurRepo;

    public List<Emprunteur> getAllEmprunteurs() {
        return emprunteurRepo.findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom"));
    }

    public Emprunteur getEmprunteurById(Integer id) {
        return emprunteurRepo.findById(id).orElse(null);
    }

    public Emprunteur saveEmprunteur(Emprunteur emprunteur) {
        if (emprunteur.getCreatedAt() == null) {
            emprunteur.setCreatedAt(LocalDateTime.now());
        }
        emprunteur.setUpdatedAt(LocalDateTime.now());
        return emprunteurRepo.save(emprunteur);
    }

    public Emprunteur updateEmprunteur(Emprunteur emprunteur) {
        emprunteur.setUpdatedAt(LocalDateTime.now());
        return emprunteurRepo.save(emprunteur);
    }

    public void deleteEmprunteurById(Integer id) {
        emprunteurRepo.deleteById(id);
    }

    public List<Emprunteur> getEmprunteursByNom(String nom) {
        return emprunteurRepo.findByNom(nom);
    }

    public List<Emprunteur> getEmprunteursByNomAndPrenom(String nom, String prenom) {
        return emprunteurRepo.findByNomAndPrenom(nom, prenom);
    }

    public List<Emprunteur> getEmprunteursByNomLikeAndPrenomLike(String nom, String prenom) {
        return emprunteurRepo.findByNomLikeAndPrenomLike(nom, prenom);
    }

    public List<Emprunteur> getEmprunteursByNomStartWithIgnoreCase(String filter) {
        return emprunteurRepo.findByNomStartsWithIgnoreCase(filter);
    }

    public List<Emprunteur> getEmprunteursByNomContainingIgnoreCase(String filter) {
        return emprunteurRepo.findByNomContainingIgnoreCase(filter);
    }

    public List<Emprunteur> getEmprunteursByPrenomContainingIgnoreCase(String filter) {
        return emprunteurRepo.findByPrenomContainingIgnoreCase(filter);
    }
}
