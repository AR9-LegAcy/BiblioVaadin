package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.TypeDocumentRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TypeDocumentService {
    private final TypeDocumentRepo typeDocumentRepo;

    public List<TypeDocument> getAllTypeDocuments() {
        return typeDocumentRepo.findAll(Sort.by(Sort.Direction.ASC, "nomTypeDocument"));
    }

    public TypeDocument getTypeDocumentById(Integer id) {
        return typeDocumentRepo.findById(id).orElse(null);
    }

    public TypeDocument saveTypeDocument(TypeDocument typeDocument) {
        if (typeDocument.getCreatedAtTypeDocument() == null) {
            typeDocument.setCreatedAtTypeDocument(LocalDateTime.now());
        }
        typeDocument.setUpdatedAtTypeDocument(LocalDateTime.now());
        return typeDocumentRepo.save(typeDocument);
    }

    public TypeDocument updateTypeDocument(TypeDocument typeDocument) {
        typeDocument.setUpdatedAtTypeDocument(LocalDateTime.now());
        return typeDocumentRepo.save(typeDocument);
    }

    public void deleteTypeDocumentById(Integer id) {
        typeDocumentRepo.deleteById(id);
    }

    public List<TypeDocument> getTypeDocumentsByNomTypeDocument(String nomTypeDocument) {
        return typeDocumentRepo.findByNomTypeDocument(nomTypeDocument);
    }

    public List<TypeDocument> getTypeDocumentsByNomTypeDocumentLike(String nomTypeDocument) {
        return typeDocumentRepo.findByNomTypeDocumentLike(nomTypeDocument);
    }

    public List<TypeDocument> getTypeDocumentsByNomTypeDocumentStartWithIgnoreCase(String filter) {
        return typeDocumentRepo.findByNomTypeDocumentStartsWithIgnoreCase(filter);
    }

    public List<TypeDocument> getTypeDocumentsByNomTypeDocumentContainingIgnoreCase(String filter) {
        return typeDocumentRepo.findByNomTypeDocumentContainingIgnoreCase(filter);
    }
}
