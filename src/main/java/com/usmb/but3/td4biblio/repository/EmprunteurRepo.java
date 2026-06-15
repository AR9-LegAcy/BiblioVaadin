package com.usmb.but3.td4biblio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Emprunteur;

public interface EmprunteurRepo extends JpaRepository<Emprunteur, Integer> {
    Optional<Emprunteur> findByCarteEmprunteur(Integer carteEmprunteur);
    List<Emprunteur> findByNom(String nom);
    List<Emprunteur> findByNomAndPrenom(String nom, String prenom);
    List<Emprunteur> findByNomLikeAndPrenomLike(String nom, String prenom);
    List<Emprunteur> findByNomStartsWithIgnoreCase(String filterText);
    List<Emprunteur> findByNomContainingIgnoreCase(String filter);
    List<Emprunteur> findByPrenomContainingIgnoreCase(String filter);
}
