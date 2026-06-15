// package com.usmb.but3.td4biblio.config;

// import java.time.format.DateTimeFormatter;
// import java.util.List;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;

// import com.usmb.but3.td4biblio.entity.Bibliothecaire;
// import com.usmb.but3.td4biblio.entity.Emprunteur;
// import com.usmb.but3.td4biblio.repository.BibliothecaireRepo;
// import com.usmb.but3.td4biblio.repository.EmprunteurRepo;

// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class DataMigration implements CommandLineRunner {

//     private final BibliothecaireRepo emprunteurRepo;
//     private final PasswordEncoder passwordEncoder;

//     @Override
//     public void run(String... args) {

//         List<Bibliothecaire> emprunteurs = emprunteurRepo.findAll();

//         for (Bibliothecaire e : emprunteurs) {

//             if (e.getMotDePasse() == null || e.getMotDePasse().isBlank()) {

//                 String mdp = e.getDateNaissance()
//                         .format(DateTimeFormatter.ofPattern("ddMMyyyy"));

//                 e.setMotDePasse(passwordEncoder.encode(mdp));
//             }
//         }

//         emprunteurRepo.saveAll(emprunteurs);

//         System.out.println("Migration des mots de passe terminée.");
//     }
// }