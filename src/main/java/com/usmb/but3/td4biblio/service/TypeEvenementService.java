package com.usmb.but3.td4biblio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.repository.TypeEvenementRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TypeEvenementService {

    private final TypeEvenementRepo typeEvenementRepo;

    public List<TypeEvenement> getAllTypeEvenements() {
        return typeEvenementRepo.findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    public TypeEvenement getTypeEvenementById(Integer id) {
        return typeEvenementRepo.findById(id).orElse(null);
    }

    public TypeEvenement saveTypeEvenement(TypeEvenement typeEvenement) {
        if (typeEvenement.getCreatedAt() == null) {
            typeEvenement.setCreatedAt(LocalDateTime.now());
        }
        typeEvenement.setUpdatedAt(LocalDateTime.now());
        return typeEvenementRepo.save(typeEvenement);
    }

    public TypeEvenement updateTypeEvenement(TypeEvenement typeEvenement) {
        typeEvenement.setUpdatedAt(LocalDateTime.now());
        return typeEvenementRepo.save(typeEvenement);
    }

    public void deleteTypeEvenementById(Integer id) {
        typeEvenementRepo.deleteById(id);
    }

    public List<TypeEvenement> getTypeEvenementsByNom(String nom) {
        return typeEvenementRepo.findByNom(nom);
    }

    public List<TypeEvenement> getTypeEvenementsByNomLike(String nom) {
        return typeEvenementRepo.findByNomLike(nom);
    }
}