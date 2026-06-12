package com.usmb.but3.td4biblio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.EmprunterId;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmprunterRepo;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmprunterService {

    private final EmprunterRepo emprunterRepo;
    private final DocumentRepo documentRepo;
    private final EmprunteurRepo emprunteurRepo;

    // --- CRUD Emprunter ---
    public List<Emprunter> getAllEmprunts() {
        return emprunterRepo.findAll(Sort.by(Sort.Direction.ASC, "dateEmprunt"));
    }

    public Emprunter getEmpruntById(EmprunterId id) {
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

    public void deleteEmpruntById(EmprunterId id) {
        emprunterRepo.deleteById(id);
    }

    // --- Helper methods pour controller ---
    public Document getDocumentById(Integer idDocument) {
        return documentRepo.findById(idDocument).orElse(null);
    }

    public Emprunteur getEmprunteurById(Integer carteEmprunteur) {
        return emprunteurRepo.findById(carteEmprunteur).orElse(null);
    }

    public List<Emprunter> getEmpruntsByCarteEmprunteur(Integer carteEmprunteur) {
        Emprunteur e = emprunteurRepo.findById(carteEmprunteur).orElse(null);
        return e == null ? List.of() : emprunterRepo.findByCarteEmprunteur(e);
    }
    
    public List<Emprunter> getEmpruntsByIdDocument(Integer idDocument) {
        Document d = documentRepo.findById(idDocument).orElse(null);
        return d == null ? List.of() : emprunterRepo.findByIdDocument(d);
    }
}