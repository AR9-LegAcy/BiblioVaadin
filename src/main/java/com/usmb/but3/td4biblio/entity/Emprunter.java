package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Une classe entité qui représente la table EMPRUNTER de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "emprunter", schema = "biblio")
@IdClass(EmprunterId.class)
public class Emprunter {
    @Id
    @Column(name = "id_document", nullable = false)
    private Integer idDocument;

    @Id
    @Column(name = "carte_emprunteur", nullable = false)
    private Integer carteEmprunteur;

    @Column(name = "date_emprunt", nullable = false)
    private LocalDate dateEmprunt;

    @Column(name = "date_retour_prevue")
    private LocalDate dateRetourPrevue;

    @Column(name = "date_retour_reelle")
    private LocalDate dateRetourReelle;

    @Column(name = "prolongation_emprunt")
    private int prolongationEmprunt;

    @Column(name = "created_at_emprunter")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_emprunter")
    private LocalDateTime updatedAt;

    public boolean isEqualTo(Emprunter emprunter) {
        if (emprunter == null) return false;
        return this.idDocument.equals(emprunter.idDocument) && this.carteEmprunteur.equals(emprunter.carteEmprunteur);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Emprunter emprunter = (Emprunter) obj;
        return isEqualTo(emprunter);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (idDocument != null ? idDocument.hashCode() : 0);
        result = 31 * result + (carteEmprunteur != null ? carteEmprunteur.hashCode() : 0);
        return result;
    }
}
