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
 * Une classe entité qui représente la table Bibliothecaire de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bibliothecaire", schema = "biblio")
public class Bibliothecaire {

    @Id
    @Column(name = "pseudo_bibliothecaire")
    private String pseudo;

    @Column(name = "id_bibliotheque", nullable = false, length = 100)
    private Integer id;

    @Column(name = "nom_bibliothecaire", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom_bibliothecaire", nullable = false, length = 100)
    private String prenom;

    @Column(name = "adresse_rue_bibliothecaire", nullable = false, length = 100)
    private String adresseRue;

    @Column(name = "adresse_ville_bibliothecaire", nullable = false, length = 100)
    private String adresseVille;

    @Column(name = "adresse_codepostal_bibliothecaire", nullable = false, length = 100)
    private String adresseCP;

    @Column(name = "email_bibliothecaire")
    private String email;

    @Column(name = "date_naissance_bibliothecaire")
    private LocalDate dateNaissance;

    @Column(name = "mot_de_passe")
    private String motDePasse;
    
    @Column(name = "created_at_bibliothecaire")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_bibliothecaire")
    private LocalDateTime updatedAt;

    public boolean isEqualTo(Bibliothecaire bibliothecaire) {
        if (this == bibliothecaire)
            return true;
        if (bibliothecaire == null)
            return false;
        if (pseudo != null ? !pseudo.equals(bibliothecaire.pseudo) : bibliothecaire.pseudo != null)
            return false;
        if (id != null ? !id.equals(bibliothecaire.id) : bibliothecaire.id != null)
            return false;
        if (nom != null ? !nom.equals(bibliothecaire.nom) : bibliothecaire.nom != null)
            return false;
        if (prenom != null ? !prenom.equals(bibliothecaire.prenom) : bibliothecaire.prenom != null)
            return false;
        if (adresseRue != null ? !adresseRue.equals(bibliothecaire.adresseRue) : bibliothecaire.adresseRue != null)
            return false;
        if (adresseVille != null ? !adresseVille.equals(bibliothecaire.adresseVille) : bibliothecaire.adresseVille != null)
            return false;
        if (adresseCP != null ? !adresseCP.equals(bibliothecaire.adresseCP) : bibliothecaire.adresseCP != null)
            return false;
        if (email != null ? !email.equals(bibliothecaire.email) : bibliothecaire.email != null)
            return false;
        if (dateNaissance != null ? !dateNaissance.equals(bibliothecaire.dateNaissance) : bibliothecaire.dateNaissance != null)
            return false;
        return motDePasse != null ? motDePasse.equals(bibliothecaire.motDePasse) : bibliothecaire.motDePasse == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bibliothecaire other = (Bibliothecaire) obj;
        return isEqualTo(other);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pseudo == null) ? 0 : pseudo.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
        result = prime * result + ((adresseRue == null) ? 0 : adresseRue.hashCode());
        result = prime * result + ((adresseVille == null) ? 0 : adresseVille.hashCode());
        result = prime * result + ((adresseCP == null) ? 0 : adresseCP.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((dateNaissance == null) ? 0 : dateNaissance.hashCode());
        result = prime * result + ((motDePasse == null) ? 0 : motDePasse.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        return result;
    }

}