package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.repository.EditeurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditeurService {

    private final EditeurRepo editeurRepo;

    public List<Editeur> getAllEditeurs() {
        return editeurRepo.findAll(Sort.by(Sort.Direction.ASC, "nom"));
    }

    public Editeur getEditeurById(Integer id) {
        return editeurRepo.findById(id).orElse(null);
    }

    public Editeur saveEditeur(Editeur editeur) {
        if (editeur.getCreatedAt() == null) {
            editeur.setCreatedAt(LocalDateTime.now());
        }
        editeur.setUpdatedAt(LocalDateTime.now());
        return editeurRepo.save(editeur);
    }

    public Editeur updateEditeur(Editeur editeur) {
        editeur.setUpdatedAt(LocalDateTime.now());
        return editeurRepo.save(editeur);
    }

    public void deleteEditeurById(Integer id) {
        editeurRepo.deleteById(id);
    }

    public List<Editeur> getEditeursByNom(String nom) {
        return editeurRepo.findByNom(nom);
    }

    public List<Editeur> getEditeursByNomStartWithIgnoreCase(String filter) {
        return editeurRepo.findByNomStartsWithIgnoreCase(filter);
    }

    public List<Editeur> getByNomContainingIgnoreCase(String filter) {
        return editeurRepo.findByNomContainingIgnoreCase(filter);
    }
}