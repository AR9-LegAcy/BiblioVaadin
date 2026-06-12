package com.usmb.but3.td4biblio.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "type_evenement", schema = "biblio")
public class TypeEvenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_evenement")
    private Integer id;

    @Column(name = "nom_type_event")
    private String nom;

    @Column(name = "created_at_type_evenement")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_type_evenement")
    private LocalDateTime updatedAt;

    public boolean isEqualTo(TypeEvenement typeEvenement) {
        if (this == typeEvenement) return true;
        if (typeEvenement == null) return false;
        if (id != null ? !id.equals(typeEvenement.id) : typeEvenement.id != null) return false;
        if (nom != null ? !nom.equals(typeEvenement.nom) : typeEvenement.nom != null) return false;
        if (createdAt != null ? !createdAt.equals(typeEvenement.createdAt) : typeEvenement.createdAt != null) return false;
        return updatedAt != null ? updatedAt.equals(typeEvenement.updatedAt) : typeEvenement.updatedAt == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TypeEvenement other = (TypeEvenement) obj;
        return isEqualTo(other);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
}