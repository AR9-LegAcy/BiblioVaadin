package com.usmb.but3.td4biblio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Ecrire;
import com.usmb.but3.td4biblio.entity.EcrireId;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.repository.AuteurRepo;
import com.usmb.but3.td4biblio.repository.EcrireRepo;
import com.usmb.but3.td4biblio.repository.DocumentRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EcrireService {
    private final EcrireRepo ecrireRepo;
    private final DocumentRepo documentRepo;
    private final AuteurRepo auteurRepo;

    // --- CRUD Ecrire ---
    public List<Ecrire> getAllEcrires() {
        return ecrireRepo.findAll(Sort.by(Sort.Direction.ASC, "idAuteur"));
    }

    public Ecrire getEcrireById(EcrireId id) {
        return ecrireRepo.findById(id).orElse(null);
    }

    public Ecrire saveEcrire(Ecrire ecrire) {
        if (ecrire.getCreatedAt() == null) {
            ecrire.setCreatedAt(LocalDateTime.now());
        }
        ecrire.setUpdatedAt(LocalDateTime.now());
        return ecrireRepo.save(ecrire);
    }

    public Ecrire updateEcrire(Ecrire ecrire) {
        ecrire.setUpdatedAt(LocalDateTime.now());
        return ecrireRepo.save(ecrire);
    }

    public void deleteEcrireById(EcrireId id) {
        ecrireRepo.deleteById(id);
    }

    // --- Helper methods pour controller ---
    public Document getDocumentById(Integer idLivre) {
        return documentRepo.findById(idLivre).orElse(null);
    }

    public Auteur getAuteurById(Integer carteAuteur) {
        return auteurRepo.findById(carteAuteur).orElse(null);
    }

    public List<Ecrire> getEcriresByCarteAuteur(Integer carteAuteur) {
        Auteur e = auteurRepo.findById(carteAuteur).orElse(null);
        return e == null ? List.of() : ecrireRepo.findByIdAuteur(e);
    }
    
    public List<Ecrire> getEcriresByIdDocument(Integer idLivre) {
        Livre d = documentRepo.findById(idLivre).orElse(null);
        return d == null ? List.of() : ecrireRepo.findByIdLivre(d);
    }
}