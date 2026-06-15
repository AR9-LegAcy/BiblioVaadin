package com.usmb.but3.td4biblio.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "evenement", schema = "biblio")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evenement")
    private Integer id;

    @Column(name = "titre_evenement")
    private String titre;

    @Column(name = "description_evenement")
    private String description;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "created_at_evenement")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_evenement")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_type_evenement")
    private TypeEvenement typeEvenement;

    @ManyToOne
    @JoinColumn(name = "id_bibliotheque")
    private Bibliotheque bibliotheque;

    public boolean isEqualTo(Evenement evenement) {
        if (this == evenement) return true;
        if (evenement == null) return false;
        if (id != null ? !id.equals(evenement.id) : evenement.id != null) return false;
        if (titre != null ? !titre.equals(evenement.titre) : evenement.titre != null) return false;
        if (description != null ? !description.equals(evenement.description) : evenement.description != null) return false;
        if (dateDebut != null ? !dateDebut.equals(evenement.dateDebut) : evenement.dateDebut != null) return false;
        if (dateFin != null ? !dateFin.equals(evenement.dateFin) : evenement.dateFin != null) return false;
        if (createdAt != null ? !createdAt.equals(evenement.createdAt) : evenement.createdAt != null) return false;
        return updatedAt != null ? updatedAt.equals(evenement.updatedAt) : evenement.updatedAt == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Evenement other = (Evenement) obj;
        return isEqualTo(other);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (titre != null ? titre.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateDebut != null ? dateDebut.hashCode() : 0);
        result = 31 * result + (dateFin != null ? dateFin.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return titre;
    }
}