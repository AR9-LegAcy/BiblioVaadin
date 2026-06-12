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

/**
 * Entité représentant un type d'auteur.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "type_auteur", schema = "biblio")
public class TypeAuteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_auteur")
    private Integer id;

    @Column(name = "nom_type_auteur", nullable = false, length = 100)
    private String nom;

    @Column(name = "created_at_type_auteur")
    private LocalDateTime createdAt;

    @Column(name = "updated_at_type_auteur")
    private LocalDateTime updatedAt;

    public boolean isEqualTo(TypeAuteur typeAuteur) {
        if (this == typeAuteur) return true;
        if (typeAuteur == null) return false;

        if (id != null ? !id.equals(typeAuteur.id) : typeAuteur.id != null)
            return false;

        if (nom != null ? !nom.equals(typeAuteur.nom) : typeAuteur.nom != null)
            return false;

        if (createdAt != null ? !createdAt.equals(typeAuteur.createdAt) : typeAuteur.createdAt != null)
            return false;

        return updatedAt != null
                ? updatedAt.equals(typeAuteur.updatedAt)
                : typeAuteur.updatedAt == null;
    }

    public String getDesc() {
        return nom;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TypeAuteur other = (TypeAuteur) obj;
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