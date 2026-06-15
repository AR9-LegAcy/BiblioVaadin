package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.usmb.but3.td4biblio.entity.Livre;

public interface LivreRepo extends JpaRepository<Livre, Integer> {
    List<Livre> findByTitreLivreContainingIgnoreCase(String titre);
    
    @Query("SELECT l FROM Livre l WHERE l.idEditeur = :idEditeur")
    List<Livre> findByIdEditeur(@Param("idEditeur") Integer idEditeur);
}