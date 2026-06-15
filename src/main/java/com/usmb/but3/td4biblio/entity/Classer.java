package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Une classe entité qui représente la table CLASSER de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "classer", schema = "biblio")
@IdClass(ClasserId.class)
public class Classer {

    @Column(name = "created_at_classer")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_classer")
    private LocalDateTime updatedAt;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "id_auteur")
    private Auteur idAuteur;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_type_auteur")
    private TypeAuteur idTypeAuteur;

    public boolean isEqualTo(Classer classer) {
        if (classer == null) return false;
        return this.idAuteur.equals(classer.idAuteur) && this.idTypeAuteur.equals(classer.idTypeAuteur);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Classer classer = (Classer) obj;
        return isEqualTo(classer);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (idAuteur != null ? idAuteur.hashCode() : 0);
        result = 31 * result + (idTypeAuteur != null ? idTypeAuteur.hashCode() : 0);
        return result;
    }
}