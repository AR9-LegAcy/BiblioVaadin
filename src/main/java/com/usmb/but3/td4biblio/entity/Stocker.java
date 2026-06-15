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
@Table(name = "stocker", schema = "biblio")
@IdClass(StockerId.class)
public class Stocker {
    @Id
    @Column(name = "id_bibliotheque", nullable = false)
    private Integer idBibliotheque;

    @Id
    @Column(name = "id_document", nullable = false)
    private Integer idDocument;

    @Column(name = "created_at_stocker")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_stocker")
    private LocalDateTime updatedAt;

    public boolean isEqualTo(Stocker stocker) {
        if (stocker == null) return false;
        return this.idBibliotheque.equals(stocker.idBibliotheque) && 
               this.idDocument.equals(stocker.idDocument);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Stocker stocker = (Stocker) obj;
        return isEqualTo(stocker);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (idBibliotheque != null ? idBibliotheque.hashCode() : 0);
        result = 31 * result + (idDocument != null ? idDocument.hashCode() : 0);
        return result;
    }
}