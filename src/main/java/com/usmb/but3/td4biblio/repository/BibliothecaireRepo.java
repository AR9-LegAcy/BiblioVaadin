package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Bibliothecaire;

public interface BibliothecaireRepo extends JpaRepository<Bibliothecaire, String> {
    List<Bibliothecaire> findByNom(String nom);
    List<Bibliothecaire> findByPseudo(String pseudo);
    List<Bibliothecaire> findByNomAndPrenom(String nom, String prenom);
    List<Bibliothecaire> findByNomLikeAndPrenomLike(String nom, String prenom);
    List<Bibliothecaire> findByNomStartsWithIgnoreCase(String filterText);
    List<Bibliothecaire> findByNomContainingIgnoreCase(String filter);
    List<Bibliothecaire> findByPrenomContainingIgnoreCase(String filter);
    List<Bibliothecaire> deleteByPseudo(String pseudo);
}