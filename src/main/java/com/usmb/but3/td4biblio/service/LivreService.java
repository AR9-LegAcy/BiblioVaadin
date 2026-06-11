package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.repository.LivreRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivreService {

    private final LivreRepo livreRepo;

    public List<Livre> getAllLivres() {
        return livreRepo.findAll(Sort.by(Sort.Direction.ASC, "titreLivre"));
    }

    public Livre getLivreById(Integer id) {
        return livreRepo.findById(id).orElse(null);
    }

    public Livre saveLivre(Livre livre) {
        if (livre.getCreatedAt() == null) {
            livre.setCreatedAt(LocalDateTime.now());
        }
        livre.setUpdatedAt(LocalDateTime.now());
        return livreRepo.save(livre);
    }

    public Livre updateLivre(Livre livre) {
        livre.setUpdatedAt(LocalDateTime.now());
        return livreRepo.save(livre);
    }

    public void deleteLivreById(Integer id) {
        livreRepo.deleteById(id);
    }

    public List<Livre> getByTitreContainingIgnoreCase(String titre) {
        return livreRepo.findByTitreLivreContainingIgnoreCase(titre);
    }

    public List<Livre> getByIdEditeur(Integer idEditeur) {
        return livreRepo.findByIdEditeur(idEditeur);
    }

    public List<Livre> getByIdTypeDocument(Integer idTypeDocument) {
        return livreRepo.findByIdTypeDocument(idTypeDocument);
    }
}