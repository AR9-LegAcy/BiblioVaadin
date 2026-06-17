package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.entity.StockerId;

public interface StockerRepo extends JpaRepository<Stocker, StockerId> {

    List<Stocker> findByIdBibliotheque(Integer idBibliotheque);

    List<Stocker> findByIdDocument(Integer idDocument);

    void deleteByIdDocument(Integer idDocument);
}