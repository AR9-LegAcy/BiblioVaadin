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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "document", schema = "biblio")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document")
    private Integer idDocument;

    @Column(name = "gif_document", length = 300)
    private String gifDocument;

    @Column(name = "description_document", length = 250)
    private String descriptionDocument;

    @Column(name = "format_taille", length = 100)
    private String formatTaille;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;

    @Column(name = "code_emplacement", length = 10)
    private String codeEmplacement;

    @Column(name = "code_isbn", length = 17, unique = true)
    private String codeIsbn;

    @Column(name = "code_empruntable", nullable = false)
    private Boolean codeEmpruntable;

    @Column(name = "etat_document", length = 100)
    private String etatDocument;

    @Column(name = "created_at_document")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_document")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_type_document")
    private TypeDocument typeDocument;

    @OneToMany(mappedBy = "idDocument")
    private List<Livre> livres;

    @OneToMany(mappedBy = "idDocument")
    private List<Emprunter> emprunters;

    public boolean isEqualTo(Document document) {
        if (this == document) return true;
        if (document == null || getClass() != document.getClass()) return false;
        if (idDocument == null ? !idDocument.equals(document.idDocument) : document.idDocument != null) return false;
        if (dateAcquisition == null ? dateAcquisition.equals(document.dateAcquisition) : document.dateAcquisition != null) return false;
        if (codeEmplacement == null ? codeEmplacement.equals(document.codeEmplacement) : document.codeEmplacement != null) return false;
        if (codeIsbn == null ? codeIsbn.equals(document.codeIsbn) : document.codeIsbn != null) return false;
        if (codeEmpruntable == null ? codeEmpruntable.equals(document.codeEmpruntable) : document.codeEmpruntable != null) return false;
        if (etatDocument == null ? etatDocument.equals(document.etatDocument) : document.etatDocument != null) return false;

        Document that = (Document) document;

        return idDocument != null ? idDocument.equals(that.idDocument) : that.idDocument == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Document that = (Document) obj;

        return idDocument != null ? idDocument.equals(that.idDocument) : that.idDocument == null;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (idDocument != null ? idDocument.hashCode() : 0);
        result = 31 * result + (dateAcquisition != null ? dateAcquisition.hashCode() : 0);
        result = 31 * result + (codeEmplacement != null ? codeEmplacement.hashCode() : 0);
        result = 31 * result + (codeIsbn != null ? codeIsbn.hashCode() : 0);
        result = 31 * result + (codeEmpruntable != null ? codeEmpruntable.hashCode() : 0);
        result = 31 * result + (etatDocument != null ? etatDocument.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return codeIsbn;
    }
}
