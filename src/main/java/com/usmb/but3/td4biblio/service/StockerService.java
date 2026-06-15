package com.usmb.but3.td4biblio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.entity.StockerId;
import com.usmb.but3.td4biblio.repository.StockerRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockerService {
    private final StockerRepo stockerRepo;

    public List<Stocker> getAllStocks() {
        return stockerRepo.findAll(Sort.by(Sort.Direction.ASC, "idBibliotheque"));
    }

    public Stocker getStockById(StockerId id) {
        return stockerRepo.findById(id).orElse(null);
    }

    public Stocker saveStocker(Stocker stock) {
        if (stock.getCreatedAt() == null)
            stock.setCreatedAt(LocalDateTime.now());
        stock.setUpdatedAt(LocalDateTime.now());
        return stockerRepo.save(stock);
    }

    public Stocker updateStocker(Stocker stock) {
        stock.setUpdatedAt(LocalDateTime.now());
        return stockerRepo.save(stock);
    }

    public void deleteStockById(StockerId id) {
        stockerRepo.deleteById(id);
    }

    public List<Stocker> getEmpruntsByIdBibliotheque(Integer idBibliotheque) {
        return stockerRepo.findByIdBibliotheque(idBibliotheque);
    }
    
    public List<Stocker> getEmpruntsByIdDocument(Integer idDocument) {
        return stockerRepo.findByIdDocument(idDocument);
    }
}
