package com.usmb.but3.td4biblio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Classer;
import com.usmb.but3.td4biblio.entity.ClasserId;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.repository.AuteurRepo;
import com.usmb.but3.td4biblio.repository.ClasserRepo;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClasserService {

    private final ClasserRepo classerRepo;
    private final TypeAuteurRepo typeAuteurRepo;
    private final AuteurRepo auteurRepo;

    // --- CRUD Classer ---
    public List<Classer> getAllClassements() {
        return classerRepo.findAll(Sort.by(Sort.Direction.ASC, "idAuteur"));
    }

    public Classer getClassementById(ClasserId id) {
        return classerRepo.findById(id).orElse(null);
    }

    public Classer saveClassement(Classer classer) {
        if (classer.getCreatedAt() == null) {
            classer.setCreatedAt(LocalDateTime.now());
        }
        classer.setUpdatedAt(LocalDateTime.now());
        return classerRepo.save(classer);
    }

    public Classer updateClassement(Classer classer) {
        classer.setUpdatedAt(LocalDateTime.now());
        return classerRepo.save(classer);
    }

    public void deleteClassementById(ClasserId id) {
        classerRepo.deleteById(id);
    }

    // --- Helper methods pour controller ---
    public TypeAuteur getTypeAuteurById(Integer idTypeAuteur) {
        return typeAuteurRepo.findById(idTypeAuteur).orElse(null);
    }

    public Auteur getAuteurById(Integer carteAuteur) {
        return auteurRepo.findById(carteAuteur).orElse(null);
    }

    public List<Classer> getClassementsByCarteAuteur(Integer carteAuteur) {
        Auteur e = auteurRepo.findById(carteAuteur).orElse(null);
        return e == null ? List.of() : classerRepo.findByIdAuteur(e);
    }
    
    public List<Classer> getClassementsByIdTypeAuteur(Integer idTypeAuteur) {
        TypeAuteur d = typeAuteurRepo.findById(idTypeAuteur).orElse(null);
        return d == null ? List.of() : classerRepo.findByIdTypeAuteur(d);
    }
}