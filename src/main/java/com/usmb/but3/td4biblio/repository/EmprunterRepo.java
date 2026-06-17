package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.EmprunterId;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.entity.Document;

public interface EmprunterRepo extends JpaRepository<Emprunter, EmprunterId> {

    List<Emprunter> findByCarteEmprunteur(Emprunteur carteEmprunteur);

    List<Emprunter> findByIdDocument(Document idDocument);

    void deleteByIdDocument_IdDocument(Integer idDocument);
}