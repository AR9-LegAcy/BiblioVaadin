package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.TypeDocument;

public interface TypeDocumentRepo extends JpaRepository<TypeDocument, Integer> {
    List<TypeDocument> findByNomTypeDocument(String nomTypeDocument);
    List<TypeDocument> findByNomTypeDocumentLike(String nomTypeDocument);
    List<TypeDocument> findByNomTypeDocumentStartsWithIgnoreCase(String filterText);
    List<TypeDocument> findByNomTypeDocumentContainingIgnoreCase(String filter);
}
