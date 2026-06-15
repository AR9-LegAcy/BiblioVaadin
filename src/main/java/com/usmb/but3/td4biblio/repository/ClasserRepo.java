package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Classer;
import com.usmb.but3.td4biblio.entity.ClasserId;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.entity.Auteur;

public interface ClasserRepo extends JpaRepository<Classer, ClasserId> {

    List<Classer> findByIdAuteur(Auteur idAuteur);

    List<Classer> findByIdTypeAuteur(TypeAuteur idTypeAuteur);
}