package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Emprunter;

public interface EmprunterRepo extends JpaRepository<Emprunter, Integer> {
    List<Emprunter> findByCarteEmprunteur(Integer carteEmprunteur);
    List<Emprunter> findByIdDocument(Integer idDocument);
}
