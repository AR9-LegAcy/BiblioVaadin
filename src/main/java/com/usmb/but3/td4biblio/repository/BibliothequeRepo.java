package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Bibliotheque;

public interface BibliothequeRepo extends JpaRepository<Bibliotheque, Integer> {
    List<Bibliotheque> findByNom(String nom);
    List<Bibliotheque> findByNomStartsWithIgnoreCase(String filterText);
    List<Bibliotheque> findByNomContainingIgnoreCase(String filter);
}