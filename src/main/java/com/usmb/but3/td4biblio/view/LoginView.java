package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.repository.BibliothecaireRepo;
import com.usmb.but3.td4biblio.repository.EmprunteurRepo;
import com.usmb.but3.td4biblio.security.SessionManager;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Route("login")
@PageTitle("Connexion — Bibliothèque")
public class LoginView extends VerticalLayout {

    public LoginView(BibliothecaireRepo bibliothecaireRepo,
                     EmprunteurRepo emprunteurRepo,
                     PasswordEncoder passwordEncoder) {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "#F1F5F9");

        // ── Carte de connexion ────────────────────────────────────────────
        var card = new Div();
        card.getStyle()
            .set("background",    "#fff")
            .set("border-radius", "1rem")
            .set("box-shadow",    "0 4px 24px rgba(0,0,0,0.08)")
            .set("padding",       "2.5rem 2rem")
            .set("width",         "100%")
            .set("max-width",     "400px")
            .set("box-sizing",    "border-box");

        // Logo + titre
        var logoRow = new Div();
        logoRow.getStyle()
            .set("display", "flex").set("align-items", "center")
            .set("gap", "0.75rem").set("margin-bottom", "0.25rem");

        var logoIco = new Icon(VaadinIcon.CUBES);
        logoIco.getStyle().set("color", "#2563EB").set("width", "2rem").set("height", "2rem");

        var logoText = new H1("BiBlio");
        logoText.getStyle()
            .set("margin", "0").set("font-size", "1.5rem")
            .set("font-weight", "700").set("color", "#1E293B");

        logoRow.add(logoIco, logoText);

        var subtitle = new Paragraph("Connectez-vous à votre espace");
        subtitle.getStyle()
            .set("margin", "0 0 2rem 0")
            .set("color", "#64748B").set("font-size", "0.9rem");

        // Champs
        var usernameField = new TextField("Identifiant");
        usernameField.setPlaceholder("N° de carte ou pseudo bibliothécaire");
        usernameField.setWidthFull();
        usernameField.setPrefixComponent(new Icon(VaadinIcon.USER));

        var passwordField = new PasswordField("Mot de passe");
        passwordField.setPlaceholder("ddMMyyyy");
        passwordField.setWidthFull();
        passwordField.setPrefixComponent(new Icon(VaadinIcon.LOCK));

        // Message d'aide discret
        var hint = new Span("Le mot de passe est votre date de naissance (ex: 15061990)");
        hint.getStyle()
            .set("font-size", "0.75rem").set("color", "#94A3B8")
            .set("display", "block").set("margin-bottom", "1.5rem");

        // Bouton connexion
        var btnLogin = new Button("Se connecter", new Icon(VaadinIcon.SIGN_IN));
        btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        btnLogin.setWidthFull();

        // Bouton retour accueil
        var btnRetour = new Button("← Retour à l'accueil");
        btnRetour.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnRetour.setWidthFull();
        btnRetour.addClickListener(e -> UI.getCurrent().navigate(MainView.class));

        // ── Logique de connexion ──────────────────────────────────────────
        Runnable doLogin = () -> {
            String username = usernameField.getValue().trim();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                showError("Veuillez remplir tous les champs.");
                return;
            }

            // Essai bibliothécaire (pseudo = String)
            List<Bibliothecaire> bibs = bibliothecaireRepo.findByPseudo(username);
            if (!bibs.isEmpty()) {
                Bibliothecaire bib = bibs.get(0);
                if (passwordEncoder.matches(password, bib.getMotDePasse())) {
                    SessionManager.loginBibliothecaire(bib);
                    UI.getCurrent().navigate(MainView.class);
                    return;
                } else {
                    showError("Mot de passe incorrect.");
                    return;
                }
            }

            // Essai emprunteur (carte = entier)
            try {
                Integer carte = Integer.parseInt(username);
                Optional<Emprunteur> empOpt = emprunteurRepo.findByCarteEmprunteur(carte);
                if (empOpt.isPresent()) {
                    Emprunteur emp = empOpt.get();
                    if (passwordEncoder.matches(password, emp.getMotDePasse())) {
                        SessionManager.loginEmprunteur(emp);
                        UI.getCurrent().navigate(MainView.class);
                        return;
                    } else {
                        showError("Mot de passe incorrect.");
                        return;
                    }
                }
            } catch (NumberFormatException ignored) {}

            showError("Identifiant introuvable.");
        };

        btnLogin.addClickListener(e -> doLogin.run());
        // Connexion aussi sur Entrée
        passwordField.addKeyPressListener(
            com.vaadin.flow.component.Key.ENTER, e -> doLogin.run());

        card.add(logoRow, subtitle, usernameField, passwordField, hint, btnLogin, btnRetour);
        add(card);
    }

    private void showError(String msg) {
        var n = Notification.show(msg, 3000, Notification.Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}