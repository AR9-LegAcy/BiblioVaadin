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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "emprunteur", schema = "biblio")
public class Emprunteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carte_emprunteur", nullable = false)
    private Integer carteEmprunteur;

    @Column(name = "nom_emprunteur", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom_emprunteur", nullable = false, length = 100)
    private String prenom;

    @Column(name = "adresse_rue_emprunteur", length = 200, nullable = false)
    private String adresseRue;

    @Column(name = "adresse_ville_emprunteur", length = 100, nullable = false)
    private String adresseVille;

    @Column(name = "adresse_code_postal_emprunteur", length = 5, nullable = false)
    private String adresseCodePostal;

    @Column(name = "email_emprunteur", length = 250, nullable = false)
    private String email;

    @Column(name = "date_naissance_emprunteur")
    private LocalDate dateNaissance;

    @Column(name = "debut_abonnement")
    private LocalDate debutAbonnement;

    @Column(name = "expiration_abonnement")
    private LocalDate expirationAbonnement;

    @Column(name = "mot_de_passe", length = 255, nullable = false)
    private String motDePasse;

    @Column(name = "created_at_emprunteur")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_emprunteur")
    private LocalDateTime updatedAt;

    public boolean isEqualTo(Emprunteur emprunteur) {
        if (this == emprunteur) return true;
        if (emprunteur == null || getClass() != emprunteur.getClass()) return false;

        Emprunteur that = (Emprunteur) emprunteur;

        if (!carteEmprunteur.equals(that.carteEmprunteur)) return false;
        if (!nom.equals(that.nom)) return false;
        if (!prenom.equals(that.prenom)) return false;
        if (adresseRue != null ? !adresseRue.equals(that.adresseRue) : that.adresseRue != null) return false;
        if (adresseVille != null ? !adresseVille.equals(that.adresseVille) : that.adresseVille != null) return false;
        if (adresseCodePostal != null ? !adresseCodePostal.equals(that.adresseCodePostal) : that.adresseCodePostal != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (dateNaissance != null ? !dateNaissance.equals(that.dateNaissance) : that.dateNaissance != null)
            return false;
        if (debutAbonnement != null ? !debutAbonnement.equals(that.debutAbonnement) : that.debutAbonnement != null)
            return false;
        if (expirationAbonnement != null ? !expirationAbonnement.equals(that.expirationAbonnement) : that.expirationAbonnement != null)
            return false;
        if (!motDePasse.equals(that.motDePasse)) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        return updatedAt != null ? updatedAt.equals(that.updatedAt) : that.updatedAt == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Emprunteur that = (Emprunteur) obj;
        return isEqualTo(that);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + carteEmprunteur.hashCode();
        result = 31 * result + nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + (adresseRue != null ? adresseRue.hashCode() : 0);
        result = 31 * result + (adresseVille != null ? adresseVille.hashCode() : 0);
        result = 31 * result + (adresseCodePostal != null ? adresseCodePostal.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (dateNaissance != null ? dateNaissance.hashCode() : 0);
        result = 31 * result + (debutAbonnement != null ? debutAbonnement.hashCode() : 0);
        result = 31 * result + (expirationAbonnement != null ? expirationAbonnement.hashCode() : 0);
        result = 31 * result + motDePasse.hashCode();
        return result;
    }
}
