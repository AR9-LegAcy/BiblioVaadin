package com.usmb.but3.td4biblio.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Une classe entité qui représente la table ECRIRE de la base de données
 */

@Entity
@Table(name = "ecrire", schema = "biblio")
@IdClass(EcrireId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ecrire {

    @Column(name = "created_at_ecrire")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_ecrire")
    private LocalDateTime updatedAt;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_auteur")
    private Auteur idAuteur;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_document")
    private Document idDocument;

    public boolean isEqualTo(Ecrire ecrire) {
        if (ecrire == null) return false;

        return idAuteur.equals(ecrire.idAuteur)
                && idDocument.equals(ecrire.idDocument);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ecrire)) return false;

        return isEqualTo((Ecrire) obj);
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result
                + (idAuteur != null ? idAuteur.hashCode() : 0);

        result = 31 * result
                + (idDocument != null ? idDocument.hashCode() : 0);

        return result;
    }
}
