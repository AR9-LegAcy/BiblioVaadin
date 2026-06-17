package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.repository.DocumentRepo;

import org.springframework.stereotype.Service;

@Service
public class IsbnGeneratorService {

    private final DocumentRepo documentRepository;

    public IsbnGeneratorService(DocumentRepo documentRepository) {
        this.documentRepository = documentRepository;
    }

    public String generateNextIsbn() {

        String lastIsbn = documentRepository.findLastIsbn();

        if (lastIsbn == null || lastIsbn.isBlank()) {
            return "978-0-000-00000-1";
        }

        // garder seulement les chiffres
        String digits = lastIsbn.replace("-", "");

        // sécurité longueur attendue
        if (digits.length() != 13) {
            throw new IllegalStateException("ISBN invalide en base: " + lastIsbn);
        }

        // split logique :
        String base = digits.substring(0, 12);
        int lastDigit = Character.getNumericValue(digits.charAt(12));

        lastDigit++;

        if (lastDigit > 9) {
            lastDigit = 0;

            long baseNum = Long.parseLong(base);
            baseNum++;

            base = String.format("%012d", baseNum);
        }

        String newDigits = base + lastDigit;

        return formatIsbn(newDigits);
    }

    private String formatIsbn(String raw) {
        // force 13 chiffres
        raw = String.format("%013d", Long.parseLong(raw));
    
        // format ISBN-13 standard : 978-0-12345-678-9
        return raw.substring(0, 3) + "-" +
               raw.substring(3, 4) + "-" +
               raw.substring(4, 9) + "-" +
               raw.substring(9, 12) + "-" +
               raw.substring(12);
    }
}