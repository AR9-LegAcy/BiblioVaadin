package com.usmb.but3.td4biblio.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.BibliothecaireRepo;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmprunteurRepo emprunteurRepository;
    private final BibliothecaireRepo bibliothecaireRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(String username, String password) {

        System.out.println("========== LOGIN ==========");
        System.out.println("Username : " + username);
        System.out.println("Password : " + password);

        // -----------------------------
        // Bibliothécaire
        // -----------------------------
        List<Bibliothecaire> liste = bibliothecaireRepository.findByPseudo(username);

        if (!liste.isEmpty()) {

            Bibliothecaire b = liste.getFirst();

            System.out.println("Bibliothécaire trouvé");
            System.out.println("Hash en base : " + b.getMotDePasse());

            boolean ok = passwordEncoder.matches(password, b.getMotDePasse());

            System.out.println("Mot de passe valide ? " + ok);

            if (ok) {
                return "BIBLIOTHECAIRE";
            }
        }

        // -----------------------------
        // Emprunteur
        // -----------------------------
        try {

            Integer carte = Integer.parseInt(username);

            Optional<Emprunteur> emp =
                    emprunteurRepository.findByCarteEmprunteur(carte);

            if (emp.isPresent()) {

                System.out.println("Emprunteur trouvé");
                System.out.println("Hash en base : " + emp.get().getMotDePasse());

                boolean ok =
                        passwordEncoder.matches(
                                password,
                                emp.get().getMotDePasse());

                System.out.println("Mot de passe valide ? " + ok);

                if (ok) {
                    return "EMPRUNTEUR";
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Le username n'est pas une carte emprunteur.");
        }

        System.out.println("ECHEC LOGIN");
        return null;
    }
}