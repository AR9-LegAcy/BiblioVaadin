package com.usmb.but3.td4biblio.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.usmb.but3.td4biblio.entity.Auteur;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class AuteurControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void setUp() {
        // This method can be used to set up any required data before all tests run
    }

    @AfterAll
    static void tearDown() {
        // This method can be used to clean up any data after all tests have run
    }

    @BeforeEach
    void init(TestInfo testInfo) {
        // This method can be used to reset the state before each test
        // Nettoyer les auteurs de test avant chaque test
        Auteur[] allAuteurs = this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/",
                Auteur[].class);
        
        if (allAuteurs != null) {
            for (Auteur auteur : allAuteurs) {
                if ("Balzac".equals(auteur.getNom()) || "TestAuteur".equals(auteur.getNom())) {
                    this.restTemplate.delete("http://localhost:" + port + "/biblio/auteur/" + auteur.getId());
                }
            }
        }
        System.out.println("Starting test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void cleanUp(TestInfo testInfo) {
        // This method can be used to clean up the state after each test
        // For example, you can delete any created Auteur objects
        Auteur[] allAuteurs = this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/",
                Auteur[].class);
        
        if (allAuteurs != null) {
            for (Auteur auteur : allAuteurs) {
                // Supprimer les auteurs de test (Balzac, TestAuteur)
                if ("Balzac".equals(auteur.getNom()) || "TestAuteur".equals(auteur.getNom())) {
                    this.restTemplate.delete("http://localhost:" + port + "/biblio/auteur/" + auteur.getId());
                }
            }
        }
        System.out.println("Completed test: " + testInfo.getDisplayName());
    }

    @Test
    void testGetAllAuteurs() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/",
                Auteur[].class)).satisfies(auteurs -> {
                    assertThat(auteurs).isNotEmpty();
                    assertThat(auteurs[0].getNom()).isNotNull();
                });
    }

    @Test
    void testGetAuteurById() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/1",
                Auteur.class)).satisfies(auteur -> {
                    assertThat(auteur).isNotNull();
                    assertThat(auteur.getId()).isEqualTo(1);
                    assertThat(auteur.getNom()).isEqualTo("Hugo");
                });
    }

    @Test
    void testGetAuteursByNom() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/nom/Hugo",
                Auteur[].class)).satisfies(auteurs -> {
                    assertThat(auteurs).isNotEmpty();
                    assertThat(auteurs[0].getNom()).isEqualTo("Hugo");
                });
    }

    @Test
    void testGetAuteursByNomAndPrenom() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/search?nom=Hugo&prenom=Victor",
                Auteur[].class)).satisfies(auteurs -> {
                    assertThat(auteurs).isNotEmpty();
                    assertThat(auteurs[0].getNom()).isEqualTo("Hugo");
                    assertThat(auteurs[0].getPrenom()).isEqualTo("Victor");
                });
    }

    @Test
    void testGetAuteursByNomLikeAndPrenomLike() {
        // Debug: afficher tous les auteurs disponibles
        Auteur[] allAuteurs = this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/",
                Auteur[].class);
        System.out.println("Total auteurs en base: " + (allAuteurs != null ? allAuteurs.length : 0));
        if (allAuteurs != null) {
            for (Auteur a : allAuteurs) {
                System.out.println("  - " + a.getNom() + " " + a.getPrenom());
            }
        }
    
        // Test LIKE search
        Auteur[] result = this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/searchLike?nom=Hug&prenom=Vict",
                Auteur[].class);
        System.out.println("Résultat searchLike: " + (result != null ? result.length : 0));
        
        assertThat(result).isNotEmpty();
        assertThat(result[0].getNom()).contains("Hug");
        assertThat(result[0].getPrenom()).contains("Vict");
    }

    @Test
    void testSaveAuteur() {
        Auteur newAuteur = new Auteur();
        newAuteur.setNom("Balzac");
        newAuteur.setPrenom("Honoré");
        newAuteur.setNationalite("Française");
        newAuteur.setDateNaissance(LocalDate.of(1799, 5, 20));
        newAuteur.setDateDeces(LocalDate.of(1850, 8, 18));

        Auteur savedAuteur = this.restTemplate.postForObject("http://localhost:" + port + "/biblio/auteur/",
                newAuteur, Auteur.class);

        assertThat(savedAuteur).satisfies(auteur -> {
            assertThat(auteur.getId()).isNotNull();
            assertThat(auteur.getNom()).isEqualTo("Balzac");
            assertThat(auteur.getPrenom()).isEqualTo("Honoré");
        });
    }

    @Test
    void testUpdateAuteur() {
        Auteur auteur = this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/1",
                Auteur.class);

        auteur.setNationalite("Française");
        Auteur updatedAuteur = this.restTemplate.postForObject("http://localhost:" + port + "/biblio/auteur/",
                auteur, Auteur.class);

        assertThat(updatedAuteur).satisfies(a -> {
            assertThat(a.getId()).isEqualTo(1);
            assertThat(a.getNationalite()).isEqualTo("Française");
        });
    }

    @Test
    void testDeleteAuteur() {
        // Create an auteur to delete
        Auteur newAuteur = new Auteur();
        newAuteur.setNom("TestAuteur");
        newAuteur.setPrenom("Test");
        newAuteur.setNationalite("Française");

        Auteur savedAuteur = this.restTemplate.postForObject("http://localhost:" + port + "/biblio/auteur/",
                newAuteur, Auteur.class);

        // Delete the auteur
        this.restTemplate.delete("http://localhost:" + port + "/biblio/auteur/" + savedAuteur.getId());

        // Verify deletion - the endpoint should return 404 or null
        Auteur deletedAuteur = this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/" + savedAuteur.getId(),
                Auteur.class);

        assertThat(deletedAuteur).isNull();
    }
}