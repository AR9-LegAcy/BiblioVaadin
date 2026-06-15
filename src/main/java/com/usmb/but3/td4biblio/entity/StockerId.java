package com.usmb.but3.td4biblio.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockerId implements Serializable {
    private Integer idBibliotheque;
    private Integer idDocument;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StockerId other = (StockerId) obj;
        if (idBibliotheque == null) {
            if (other.idBibliotheque != null) return false;
        } else if (!idBibliotheque.equals(other.idBibliotheque)) return false;
        if (idDocument == null) {
            if (other.idDocument != null) return false;
        } else if (!idDocument.equals(other.idDocument)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idBibliotheque == null) ? 0 : idBibliotheque.hashCode());
        result = prime * result + ((idDocument == null) ? 0 : idDocument.hashCode());
        return result;
    }
}