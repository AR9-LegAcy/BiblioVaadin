package com.usmb.but3.td4biblio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TypeAuteurService {

    private final TypeAuteurRepo typeAuteurRepo;

    public List<TypeAuteur> getAllTypeAuteurs() {
        return typeAuteurRepo.findAll(Sort.by(Sort.Direction.ASC, "nom", "prenom"));
    }

    public TypeAuteur getTypeAuteurById(Integer id) {
        return typeAuteurRepo.findById(id).orElse(null);
    }

    public TypeAuteur saveTypeAuteur(TypeAuteur typeAuteur) {
        if (typeAuteur.getCreatedAt() == null) {
            typeAuteur.setCreatedAt(LocalDateTime.now());
        }
        typeAuteur.setUpdatedAt(LocalDateTime.now());
        return typeAuteurRepo.save(typeAuteur);
    }

    public TypeAuteur updateTypeAuteur(TypeAuteur typeAuteur) {
        typeAuteur.setUpdatedAt(LocalDateTime.now());
        return typeAuteurRepo.save(typeAuteur);
    }

    public void deleteTypeAuteurById(Integer id) {
        typeAuteurRepo.deleteById(id);
    }

    public List<TypeAuteur> getTypeAuteursByNom(String nom) {
        return typeAuteurRepo.findByNom(nom);
    }

    public List<TypeAuteur> getTypeAuteursByNomLike(String nom) {
        return typeAuteurRepo.findByNomLike(nom);
    }
}