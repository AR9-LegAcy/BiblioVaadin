package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.TypeEvenement;

public interface TypeEvenementRepo extends JpaRepository<TypeEvenement, Integer> {
    List<TypeEvenement> findByNom(String nom);
    List<TypeEvenement> findByNomLike(String nom);
}