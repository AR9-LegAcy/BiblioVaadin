package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.DocumentService;

import java.util.List;

/**
 * Controller for handling Document-related HTTP requests.
 */

@RestController
@RequestMapping("/biblio/document")
@RequiredArgsConstructor
@Validated
public class DocumentController {
    private final DocumentService documentService;

    /**
     * GET all documents.
     * URL: localhost:8080/biblio/document/
     */
    @GetMapping("/")
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok().body(documentService.getAllDocuments());
    }

    /**
     * GET document by id.
     * URL: localhost:8080/biblio/document/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable("id") Integer id) {
        Document document = documentService.getDocumentById(id);
        return document != null ? ResponseEntity.ok().body(document) : ResponseEntity.notFound().build();
    }

    /**
     * GET documents by codeIsbn.
     * URL: localhost:8080/biblio/document/isbn/{codeIsbn}
     */
    @GetMapping("/isbn/{codeIsbn}")
    public ResponseEntity<List<Document>> getDocumentsByCodeIsbn(@PathVariable("codeIsbn") String codeIsbn) {
        return ResponseEntity.ok().body(documentService.getDocumentsByCodeIsbn(codeIsbn));
    }

    /**
     * POST a new document.
     * URL: localhost:8080/biblio/document/
     */
    @PostMapping("/")
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
    
        Bibliothecaire bib = SessionManager.getBibliothecaire();
    
        Document savedDocument = documentService.saveDocument(
            document,
            bib.getBibliotheque()
        );
    
        return ResponseEntity.ok(savedDocument);
    }

    /**
     * PUT update an existing document.
     * URL: localhost:8080/biblio/document/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable("id") Integer id, @RequestBody Document document) {
        Document existingDocument = documentService.getDocumentById(id);
        if (existingDocument == null) {
            return ResponseEntity.notFound().build();
        }
        document.setIdDocument(id); // Ensure the ID is set for update
        Document updatedDocument = documentService.updateDocument(document);
        return ResponseEntity.ok().body(updatedDocument);
    }

    /**
     * DELETE a document by id.
     * URL: localhost:8080/biblio/document/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable("id") Integer id) {
        Document existingDocument = documentService.getDocumentById(id);
        if (existingDocument == null) {
            return ResponseEntity.notFound().build();
        }
        documentService.deleteDocumentById(id);
        return ResponseEntity.noContent().build();
    }
}
