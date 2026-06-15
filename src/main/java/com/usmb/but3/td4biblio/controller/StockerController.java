package com.usmb.but3.td4biblio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.entity.StockerId;
import com.usmb.but3.td4biblio.service.StockerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("biblio/stocker")
@RequiredArgsConstructor
@Validated
public class StockerController {
    private final StockerService stockerService;

    @GetMapping("/")
    public ResponseEntity<List<Stocker>> getAllStocks() {
        return ResponseEntity.ok().body(stockerService.getAllStocks());
    }

    @GetMapping("{idBibliotheque}/{idDocument}")
    public ResponseEntity<Stocker> getStockById(@PathVariable("idBibliotheque") Integer idBibliotheque,
                                                @PathVariable("idDocument") Integer idDocument) {
        StockerId id = new StockerId(idBibliotheque, idDocument);
        Stocker stock = stockerService.getStockById(id);
        return stock != null ? ResponseEntity.ok().body(stock) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<Stocker> createStock(@RequestBody Stocker stock) {
        Stocker savedStock = stockerService.saveStocker(stock);
        return ResponseEntity.ok().body(savedStock);
    }   

    @PutMapping("/{idBibliotheque}/{idDocument}")
    public ResponseEntity<Stocker> updateStock(@PathVariable("idBibliotheque") Integer idBibliotheque,
                                                    @PathVariable("idDocument") Integer idDocument,
                                                    @RequestBody Stocker stock) {
        StockerId id = new StockerId(idBibliotheque, idDocument);
        Stocker existingStock = stockerService.getStockById(id);
        if (existingStock == null) {
            return ResponseEntity.notFound().build();
        }
        stock.setIdBibliotheque(idBibliotheque);
        stock.setIdDocument(idDocument);
        Stocker updatedStock = stockerService.updateStocker(stock);
        return ResponseEntity.ok().body(updatedStock);
    }

    @DeleteMapping("/{idBibliotheque}/{idDocument}")
    public ResponseEntity<Void> deleteStock(@PathVariable("idBibliotheque") Integer idBibliotheque,
                                              @PathVariable("idDocument") Integer idDocument) {
        StockerId id = new StockerId(idBibliotheque, idDocument);
        Stocker existingStock = stockerService.getStockById(id);
        if (existingStock == null) {
            return ResponseEntity.notFound().build();
        }
        stockerService.deleteStockById(id);
        return ResponseEntity.noContent().build();
    }
}
