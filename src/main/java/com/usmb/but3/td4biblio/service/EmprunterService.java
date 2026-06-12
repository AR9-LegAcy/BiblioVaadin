package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.repository.EmprunterRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmprunterService {
    private final EmprunterRepo emprunterRepo;

    public List<Emprunter> getAllEmprunts() {
        return emprunterRepo.findAll(Sort.by(Sort.Direction.ASC, "dateEmprunt"));
    }

    public Emprunter getEmpruntById(Integer id) {
        return emprunterRepo.findById(id).orElse(null);
    }

    public Emprunter saveEmprunt(Emprunter emprunt) {
        if (emprunt.getCreatedAt() == null) {
            emprunt.setCreatedAt(LocalDateTime.now());
        }
        emprunt.setUpdatedAt(LocalDateTime.now());
        return emprunterRepo.save(emprunt);
    }

    public Emprunter updateEmprunt(Emprunter emprunt) {
        emprunt.setUpdatedAt(LocalDateTime.now());
        return emprunterRepo.save(emprunt);
    }

    public void deleteEmpruntById(Integer id) {
        emprunterRepo.deleteById(id);
    }

    public List<Emprunter> getEmpruntsByCarteEmprunteur(Integer carteEmprunteur) {
        return emprunterRepo.findByCarteEmprunteur(carteEmprunteur);
    }
    
    public List<Emprunter> getEmpruntsByIdDocument(Integer idDocument) {
        return emprunterRepo.findByIdDocument(idDocument);
    }
}
