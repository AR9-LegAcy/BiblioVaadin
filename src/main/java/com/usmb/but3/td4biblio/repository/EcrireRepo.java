package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Ecrire;
import com.usmb.but3.td4biblio.entity.EcrireId;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Auteur;

public interface EcrireRepo extends JpaRepository<Ecrire, EcrireId> {

    List<Ecrire> findByIdAuteur(Auteur auteur);

    List<Ecrire> findByIdDocument(Document document);
}