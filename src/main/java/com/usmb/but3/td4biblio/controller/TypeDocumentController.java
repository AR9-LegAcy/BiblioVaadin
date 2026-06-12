package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.service.TypeDocumentService;

import java.util.List;

@RestController
@RequestMapping("/biblio/type-document")
@RequiredArgsConstructor
@Validated
public class TypeDocumentController {
    private final TypeDocumentService typeDocumentService;

    @GetMapping("/")
    public ResponseEntity<List<TypeDocument>> getAllTypeDocuments() {
        return ResponseEntity.ok().body(typeDocumentService.getAllTypeDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDocument> getTypeDocumentById(@PathVariable("id") Integer id) {
        TypeDocument typeDocument = typeDocumentService.getTypeDocumentById(id);
        return typeDocument != null ? ResponseEntity.ok().body(typeDocument) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/nom/{nomTypeDocument}")
    public ResponseEntity<List<TypeDocument>> getTypeDocumentsByNomTypeDocument(@PathVariable("nomTypeDocument") String nomTypeDocument) {
        return ResponseEntity.ok().body(typeDocumentService.getTypeDocumentsByNomTypeDocument(nomTypeDocument));
    }

    @GetMapping("/searchLike")
    public ResponseEntity<List<TypeDocument>> getTypeDocumentsByNomTypeDocumentLike(@RequestParam(name = "nomTypeDocument") String nomTypeDocument) {
        return ResponseEntity.ok().body(typeDocumentService.getTypeDocumentsByNomTypeDocumentLike(nomTypeDocument));
    }

    @PostMapping("/")
    public ResponseEntity<TypeDocument> createTypeDocument(@RequestBody TypeDocument typeDocument) {
        return ResponseEntity.ok().body(typeDocumentService.saveTypeDocument(typeDocument));
    }

    @PutMapping("/")
    public ResponseEntity<TypeDocument> updateTypeDocument(@RequestBody TypeDocument typeDocument) {
        return ResponseEntity.ok().body(typeDocumentService.updateTypeDocument(typeDocument));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeDocument(@PathVariable("id") Integer id) {
        TypeDocument existingTypeDocument = typeDocumentService.getTypeDocumentById(id);
        if (existingTypeDocument == null) {
            return ResponseEntity.notFound().build();
        }
        typeDocumentService.deleteTypeDocumentById(id);
        return ResponseEntity.noContent().build();
    }
}
