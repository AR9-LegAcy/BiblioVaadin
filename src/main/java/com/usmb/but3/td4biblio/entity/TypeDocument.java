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
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "type_document", schema = "biblio")
public class TypeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_document")
    private Integer idTypeDocument;

    @Column(name = "nom_type_document", nullable = false, length = 100)
    private String nomTypeDocument;

    @Column(name = "created_at_type_document")
    private LocalDateTime createdAtTypeDocument;

    @Column(name = "updated_at_type_document")
    private LocalDateTime updatedAtTypeDocument;

    @OneToMany(mappedBy = "typeDocument")
    private List<Document> documents;

    @OneToMany(mappedBy = "idTypeDocument")
    private List<Livre> livres;

    public boolean isEqualTo(TypeDocument typeDocument) {
        if (typeDocument == null) {
            return false;
        }
        if (this.idTypeDocument == null || typeDocument.idTypeDocument == null) {
            return false;
        }
        return this.idTypeDocument.equals(typeDocument.idTypeDocument);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TypeDocument that = (TypeDocument) obj;

        return idTypeDocument != null ? idTypeDocument.equals(that.idTypeDocument) : that.idTypeDocument == null;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (idTypeDocument != null ? idTypeDocument.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return nomTypeDocument;
    }
}
