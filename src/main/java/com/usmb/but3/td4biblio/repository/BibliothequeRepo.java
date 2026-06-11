package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Bibliotheque;

public interface BibliothequeRepo extends JpaRepository<Bibliotheque, Integer> {
    List<Bibliotheque> findByNom(String nom);
    List<Bibliotheque> findByAdresseRue(String adresseRue);
    List<Bibliotheque> findByAdresseVille(String adresseVille);
    List<Bibliotheque> findByCodePostal(String codePostal);
    List<Bibliotheque> findByHoraires(String horaires);
    List<Bibliotheque> findByNomAndAdresseRue(String nom, String adresseRue);
    List<Bibliotheque> findByNomLikeAndAdresseVille(String nom, String adresseVille);
    List<Bibliotheque> findByNomLikeAndCodePostal(String nom, String codePostal);
    List<Bibliotheque> findByNomLikeAndHoraires(String nom, String horaires);
}