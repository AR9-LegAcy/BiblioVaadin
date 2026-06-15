package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
 * Une classe entité qui représente la table Editeur de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "editeur", schema = "biblio")
public class Editeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_editeur")
    private Integer id;

    @Column(name = "nom_societe", nullable = false, length = 100)
    private String nom;

    @Column(name = "adresse_editeur", nullable = false, length = 100)
    private String adresse;

    @Column(name = "siteweb_editeur")
    private String siteWeb;

    @Column(name = "wikipedia_editeur")
    private String wikipedia;

    @Column(name = "created_at_editeur")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_editeur")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "idEditeur")
    private List<Livre> livres;

    public boolean isEqualTo(Editeur editeur) {
        if (this == editeur) return true;
        if (editeur == null) return false;
        if (id != null ? !id.equals(editeur.id) : editeur.id != null) return false;
        if (nom != null ? !nom.equals(editeur.nom) : editeur.nom != null) return false;
        if (adresse != null ? !adresse.equals(editeur.adresse) : editeur.adresse != null) return false;
        if (siteWeb != null ? !siteWeb.equals(editeur.siteWeb) : editeur.siteWeb != null)
            return false;
        return wikipedia != null ? wikipedia.equals(editeur.wikipedia) : editeur.wikipedia == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Editeur other = (Editeur) obj;
        return isEqualTo(other);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
        result = prime * result + ((siteWeb == null) ? 0 : siteWeb.hashCode());
        result = prime * result + ((wikipedia == null) ? 0 : wikipedia.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        return result;
    }

    // public String getDesc() {
    //     return prenom + " " + nom + " (" + (dateNaissance != null ? dateNaissance.getYear() : "?") + 
    //            "-" + (dateDeces != null ? dateDeces.getYear() : "en vie") + ")";
    // }
    @Override
    public String toString() {
        return nom;
    }
}