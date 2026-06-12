package com.usmb.but3.td4biblio.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Evenement;

public interface EvenementRepo extends JpaRepository<Evenement, Integer> {
    List<Evenement> findByTitre(String titre);
    List<Evenement> findByTitreLike(String titre);
    List<Evenement> findByDateDebut(LocalDate dateDebut);
    List<Evenement> findByDateFin(LocalDate dateFin);
    List<Evenement> findByTitreLikeAndDateDebut(String titre, LocalDate dateDebut);
    List<Evenement> findByTitreLikeAndDateFin(String titre, LocalDate dateFin);
    List<Evenement> findByDateDebutAfterOrderByDateDebutAsc(LocalDate dateDebut);
}