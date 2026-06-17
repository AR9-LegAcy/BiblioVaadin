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
public class EcrireId implements Serializable {

    private Integer idAuteur;

    private Integer idDocument;

    // equals et hashCode personnalisés (optionnels si Lombok gère @Data)
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        EcrireId other = (EcrireId) obj;
        if (idAuteur == null ? other.idAuteur != null : !idAuteur.equals(other.idAuteur))
            return false;
        if (idDocument == null ? other.idDocument != null : !idDocument.equals(other.idDocument))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (idAuteur == null ? 0 : idAuteur.hashCode());
        result = prime * result + (idDocument == null ? 0 : idDocument.hashCode());
        return result;
    }
}