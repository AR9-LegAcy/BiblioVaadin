package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bibliotheque", schema = "biblio")
public class Bibliotheque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bibliotheque")
    private Integer id;

    @Column(name = "nom_bibliotheque", nullable = false)
    private String nom;

    @Column(name = "adresse_rue_bibliotheque", nullable = false)
    private String adresseRue;

    @Column(name = "adresse_ville_bibliotheque", nullable = false)
    private String adresseVille;

    @Column(name = "adresse_codepostal_bibliotheque", nullable = false)
    private String codePostal;

    @Column(name = "horaires_bibliotheque")
    private String horaires;

    @Column(name = "created_at_bibliotheque")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_bibliotheque")
    private LocalDateTime updatedAt;

    public boolean isEqualTo(Bibliotheque bibliotheque) {
        if (this == bibliotheque) return true;
        if (bibliotheque == null) return false;
        if (id != null ? !id.equals(bibliotheque.id) : bibliotheque.id != null) return false;
        if (nom != null ? !nom.equals(bibliotheque.nom) : bibliotheque.nom != null) return false;
        if (adresseRue != null ? !adresseRue.equals(bibliotheque.adresseRue) : bibliotheque.adresseRue != null) return false;
        if (adresseVille != null ? !adresseVille.equals(bibliotheque.adresseVille) : bibliotheque.adresseVille != null) return false;
        if (codePostal != null ? !codePostal.equals(bibliotheque.codePostal) : bibliotheque.codePostal != null) return false;
        if (horaires != null ? !horaires.equals(bibliotheque.horaires) : bibliotheque.horaires != null) return false;
        if (createdAt != null ? !createdAt.equals(bibliotheque.createdAt) : bibliotheque.createdAt != null) return false;
        return updatedAt != null ? updatedAt.equals(bibliotheque.updatedAt) : bibliotheque.updatedAt == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Bibliotheque other = (Bibliotheque) obj;
        return isEqualTo(other);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (adresseRue != null ? adresseRue.hashCode() : 0);
        result = 31 * result + (adresseVille != null ? adresseVille.hashCode() : 0);
        result = 31 * result + (codePostal != null ? codePostal.hashCode() : 0);
        result = 31 * result + (horaires != null ? horaires.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
}