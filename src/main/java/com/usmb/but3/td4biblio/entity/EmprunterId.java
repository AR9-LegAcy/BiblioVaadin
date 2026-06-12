package com.usmb.but3.td4biblio.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprunterId implements Serializable{
    private Integer idDocument;
    private Integer carteEmprunteur;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmprunterId other = (EmprunterId) obj;
        if (idDocument == null) {
            if (other.idDocument != null)
                return false;
        } else if (!idDocument.equals(other.idDocument))
            return false;
        if (carteEmprunteur == null) {
            if (other.carteEmprunteur != null)
                return false;
        } else if (!carteEmprunteur.equals(other.carteEmprunteur))
            return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idDocument == null) ? 0 : idDocument.hashCode());
        result = prime * result + ((carteEmprunteur == null) ? 0 : carteEmprunteur.hashCode());
        return result;
    }
}
