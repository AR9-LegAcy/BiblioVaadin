package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.EmprunterId;

public interface EmprunterRepo extends JpaRepository<Emprunter, EmprunterId> {
    List<Emprunter> findByCarteEmprunteur(Integer carteEmprunteur);
    List<Emprunter> findByIdDocument(Integer idDocument);
}
