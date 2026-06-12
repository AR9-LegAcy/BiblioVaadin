package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Une classe entité qui représente la table Bibliotheque de la base de données
 */

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

    @Column(name = "nom_bibliotheque", nullable = false, length = 100)
    private String nom;

    @Column(name = "adresse_ville_bibliotheque", nullable = false, length = 100)
    private String adresseVille;

    @Column(name = "adresse_rue_bibliotheque", nullable = false, length = 100)
    private String adresseRue;

    @Column(name = "adresse_codepostal_bibliotheque", nullable = false, length = 100)
    private String adresseCP;

    @Column(name = "horaires_bibliotheque")
    private String horaires;

    @Column(name = "created_at_bibliotheque")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_bibliotheque")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "bibliotheque")
    private List<Bibliothecaire> bibliothecaires;

    @OneToMany(mappedBy = "bibliotheque")
    private List<Evenement> evenements;

    public boolean isEqualTo(Bibliotheque bibliotheque) {
        if (this == bibliotheque)
            return true;
        if (bibliotheque == null)
            return false;
        if (id != null ? !id.equals(bibliotheque.id) : bibliotheque.id != null)
            return false;
        if (nom != null ? !nom.equals(bibliotheque.nom) : bibliotheque.nom != null)
            return false;
        if (adresseVille != null ? !adresseVille.equals(bibliotheque.adresseVille) : bibliotheque.adresseVille != null)
            return false;
        if (adresseRue != null ? !adresseRue.equals(bibliotheque.adresseRue) : bibliotheque.adresseRue != null)
            return false;
        if (adresseCP != null ? !adresseCP.equals(bibliotheque.adresseCP) : bibliotheque.adresseCP != null)
            return false;
        return horaires != null ? horaires.equals(bibliotheque.horaires) : bibliotheque.horaires == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bibliotheque other = (Bibliotheque) obj;
        return isEqualTo(other);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((adresseVille == null) ? 0 : adresseVille.hashCode());
        result = prime * result + ((adresseRue == null) ? 0 : adresseRue.hashCode());
        result = prime * result + ((adresseCP == null) ? 0 : adresseCP.hashCode());
        result = prime * result + ((horaires == null) ? 0 : horaires.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return nom;
    }
}