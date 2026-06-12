package com.usmb.but3.td4biblio.entity;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmprunterId implements Serializable {
    private Integer idDocument;
    private Integer carteEmprunteur;

    // equals et hashCode personnalisés (optionnels si Lombok gère @Data)
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        EmprunterId other = (EmprunterId) obj;
        if (idDocument == null ? other.idDocument != null : !idDocument.equals(other.idDocument))
            return false;
        if (carteEmprunteur == null ? other.carteEmprunteur != null : !carteEmprunteur.equals(other.carteEmprunteur))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (idDocument == null ? 0 : idDocument.hashCode());
        result = prime * result + (carteEmprunteur == null ? 0 : carteEmprunteur.hashCode());
        return result;
    }
}