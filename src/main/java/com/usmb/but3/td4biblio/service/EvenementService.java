package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.repository.EvenementRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvenementService {

    private final EvenementRepo evenementRepo;

    public List<Evenement> getAllEvenements() {
        return evenementRepo.findAll(Sort.by(Sort.Direction.ASC, "dateDebut"));
    }

    public Evenement getEvenementById(Integer id) {
        return evenementRepo.findById(id).orElse(null);
    }

    public Evenement saveEvenement(Evenement evenement) {
        if (evenement.getCreatedAt() == null) {
            evenement.setCreatedAt(LocalDateTime.now());
        }
        evenement.setUpdatedAt(LocalDateTime.now());
        return evenementRepo.save(evenement);
    }

    public Evenement updateEvenement(Evenement evenement) {
        evenement.setUpdatedAt(LocalDateTime.now());
        return evenementRepo.save(evenement);
    }

    public void deleteEvenementById(Integer id) {
        evenementRepo.deleteById(id);
    }

    public List<Evenement> getEvenementsByTitre(String titre) {
        return evenementRepo.findByTitre(titre);
    }

    public List<Evenement> getEvenementsByTitreLike(String titre) {
        return evenementRepo.findByTitreLike(titre);
    }

    public List<Evenement> getEvenementsByDateDebut(LocalDate dateDebut) {
        return evenementRepo.findByDateDebut(dateDebut);
    }

    public List<Evenement> getEvenementsByDateFin(LocalDate dateFin) {
        return evenementRepo.findByDateFin(dateFin);
    }

    public List<Evenement> getEvenementsByTitreLikeAndDateDebut(String titre, LocalDate dateDebut) {
        return evenementRepo.findByTitreLikeAndDateDebut(titre, dateDebut);
    }

    public List<Evenement> getEvenementsByTitreLikeAndDateFin(String titre, LocalDate dateFin) {
        return evenementRepo.findByTitreLikeAndDateFin(titre, dateFin);
    }
    
    public List<Evenement> getEvenementsFuturs() {
        return evenementRepo.findByDateDebutAfterOrderByDateDebutAsc(
                LocalDate.now());
    }
}