package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Une classe entité qui représente la table AUTEUR de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "auteur", schema = "biblio")
public class Auteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auteur")
    private Integer id;

    @Column(name = "nom_auteur", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom_auteur", nullable = false, length = 100)
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_mort")
    private LocalDate dateDeces;

    @Column(name = "pays_auteur", length = 100)
    private String paysAuteur;

    @Column(name = "ville_auteur", length = 100)
    private String villeAuteur;

    @Column(name = "nationnalite_auteur", length = 100)
    private String nationalite;

    @Column(name = "wikipedia_auteur", length = 150)
    private String wikipediaAuteur;

    @Column(name = "created_at_auteur")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_auteur")
    private LocalDateTime updatedAt;

    

    public boolean isEqualTo(Auteur auteur) {
        if (this == auteur) return true;
        if (auteur == null) return false;
        if (id != null ? !id.equals(auteur.id) : auteur.id != null) return false;
        if (nom != null ? !nom.equals(auteur.nom) : auteur.nom != null) return false;
        if (prenom != null ? !prenom.equals(auteur.prenom) : auteur.prenom != null) return false;
        if (nationalite != null ? !nationalite.equals(auteur.nationalite) : auteur.nationalite != null)
            return false;
        if (dateNaissance != null ? !dateNaissance.equals(auteur.dateNaissance) : auteur.dateNaissance != null)
            return false;
        return dateDeces != null ? dateDeces.equals(auteur.dateDeces) : auteur.dateDeces == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Auteur other = (Auteur) obj;
        return isEqualTo(other);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (nationalite != null ? nationalite.hashCode() : 0);
        result = 31 * result + (dateNaissance != null ? dateNaissance.hashCode() : 0);
        result = 31 * result + (dateDeces != null ? dateDeces.hashCode() : 0);
        return result;
    }

    public String getDesc() {
        return prenom + " " + nom + " (" + (dateNaissance != null ? dateNaissance.getYear() : "?") + 
               "-" + (dateDeces != null ? dateDeces.getYear() : "en vie") + ")";
    }

    @Override
    public String toString() {
        return nom;
    }
}