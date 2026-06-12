package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Une classe entité qui représente la table LIVRE de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "livre", schema = "biblio")
public class Livre {
    @Id
    @Column(name = "id_livre")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLivre;

    @Column(name = "gif_document", length = 200)
    private String gifDocument;

    @Column(name = "titre_livre", nullable = false, length = 250)
    private String titreLivre;

    @Column(name = "nb_pages")
    private Integer nbPages;

    @Column(name = "date_publication")
    private LocalDate datePublication;

    @Column(name = "format_taille", length = 100)
    private String formatTaille;

    @Column(name = "description_document", length = 250)
    private String descriptionDocument;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;

    @Column(name = "code_emplacement", length = 10)
    private String codeEmplacement;

    @Column(name = "code_isbn", nullable = false, length = 17, unique = true)
    private String codeIsbn;

    @Column(name = "code_empruntable")
    private Boolean codeEmpruntable;

    @Column(name = "etat_document", length = 100)
    private String etatDocument;

    @Column(name = "created_at_document")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_document")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_document")
    private Document idDocument;
    
    @ManyToOne
    @JoinColumn(name = "id_editeur")
    private Editeur idEditeur;
    
    @ManyToOne
    @JoinColumn(name = "id_type_document")
    private TypeDocument idTypeDocument;

    public boolean isEqualTo(Livre livre) {
        if (this == livre) return true;
        if (livre == null) return false;
        if (idDocument != null ? !idDocument.equals(livre.idDocument) : livre.idDocument != null) return false;
        if (titreLivre != null ? !titreLivre.equals(livre.titreLivre) : livre.titreLivre != null) return false;
        if (codeIsbn != null ? !codeIsbn.equals(livre.codeIsbn) : livre.codeIsbn != null) return false;
        if (idEditeur != null ? !idEditeur.equals(livre.idEditeur) : livre.idEditeur != null) return false;
        if (nbPages != null ? !nbPages.equals(livre.nbPages) : livre.nbPages != null) return false;
        if (datePublication != null ? !datePublication.equals(livre.datePublication) : livre.datePublication != null)
            return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Livre other = (Livre) obj;
        return isEqualTo(other);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (idDocument != null ? idDocument.hashCode() : 0);
        result = 31 * result + (titreLivre != null ? titreLivre.hashCode() : 0);
        result = 31 * result + (codeIsbn != null ? codeIsbn.hashCode() : 0);
        result = 31 * result + (idEditeur != null ? idEditeur.hashCode() : 0);
        result = 31 * result + (nbPages != null ? nbPages.hashCode() : 0);
        result = 31 * result + (datePublication != null ? datePublication.hashCode() : 0);
        return result;
    }

    public String getDesc() {
        return titreLivre + " (ISBN: " + codeIsbn + ")";
    }

    @Override
    public String toString() {
        return "Livre{" +
                "idDocument=" + idDocument +
                ", titreLivre='" + titreLivre + '\'' +
                ", codeIsbn='" + codeIsbn + '\'' +
                ", nbPages=" + nbPages +
                ", datePublication=" + datePublication +
                '}';
    }
}