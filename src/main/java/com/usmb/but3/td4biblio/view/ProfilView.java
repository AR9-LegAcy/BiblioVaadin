package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.EmprunteurService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Route("profil")
@PageTitle("Mon profil — Bibliothèque")
public class ProfilView extends Main implements BeforeEnterObserver {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final String ACCENT = "#7C3AED";

    private final EmprunteurService emprunteurService;

    public ProfilView(EmprunteurService emprunteurService) {
        this.emprunteurService = emprunteurService;
        getStyle()
            .set("--surface",   "#FFFFFF")
            .set("--muted",     "#64748B")
            .set("--border",    "#E2E8F0")
            .set("font-family", "sans-serif")
            .set("display",     "flex")
            .set("flex-direction", "column");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Rediriger si pas emprunteur connecté
        if (!SessionManager.isEmprunteur()) {
            event.rerouteTo(MainView.class);
        }
    }

    @Override
    protected void onAttach(com.vaadin.flow.component.AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        removeAll();
        Emprunteur emp = SessionManager.getEmprunteur();
        if (emp == null) return;

        // Recharger depuis la BDD pour avoir les données à jour
        Emprunteur empFrais = emprunteurService.getEmprunteurById(emp.getCarteEmprunteur());
        if (empFrais != null) emp = empFrais;

        add(buildHero(emp));
        add(buildBody(emp));
    }

    // ── HERO ─────────────────────────────────────────────────────────────
    private Div buildHero(Emprunteur emp) {
        var hero = new Div();
        hero.setWidthFull();
        hero.getStyle()
            .set("background",   "linear-gradient(135deg,#4C1D95 0%,#7C3AED 60%,#A78BFA 100%)")
            .set("color",        "#fff")
            .set("padding",      "2.5rem 2.5rem 2rem")
            .set("box-sizing",   "border-box");

        // Bouton retour
        var btnRetour = new Button("← Retour à l'accueil");
        btnRetour.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnRetour.getStyle()
            .set("color", "#fff").set("opacity", "0.8")
            .set("padding", "0").set("margin-bottom", "1rem");
        btnRetour.addClickListener(e -> UI.getCurrent().navigate(MainView.class));
        hero.add(btnRetour);

        // Avatar initiales
        var avatar = new Div();
        avatar.setText(initiales(emp));
        avatar.getStyle()
            .set("width",         "4rem").set("height", "4rem")
            .set("border-radius", "50%")
            .set("background",    "rgba(255,255,255,0.2)")
            .set("display",       "flex").set("align-items", "center")
            .set("justify-content", "center")
            .set("font-size",     "1.5rem").set("font-weight", "700")
            .set("margin-bottom", "0.75rem");
        hero.add(avatar);

        var titre = new H1(emp.getPrenom() + " " + emp.getNom());
        titre.getStyle()
            .set("margin", "0 0 0.3rem 0")
            .set("font-size", "clamp(1.4rem,4vw,2rem)")
            .set("font-weight", "700");
        hero.add(titre);

        var carte = new Span("Carte n° " + String.format("%010d", emp.getCarteEmprunteur()));
        carte.getStyle()
            .set("font-size", "0.85rem").set("opacity", "0.85").set("display", "block");
        hero.add(carte);

        // Badge statut abonnement
        var statut = buildStatutBadge(emp);
        statut.getStyle().set("margin-top", "0.75rem").set("display", "inline-block");
        hero.add(statut);

        return hero;
    }

    // ── CORPS ─────────────────────────────────────────────────────────────
    private Div buildBody(Emprunteur emp) {
        var body = new Div();
        body.getStyle()
            .set("padding",    "2rem 2.5rem")
            .set("max-width",  "860px")
            .set("width",      "100%")
            .set("box-sizing", "border-box");

        // ── Section abonnement ───────────────────────────────────────────
        var abonSection = new Div();
        abonSection.getStyle()
            .set("background",    "#fff")
            .set("border",        "1px solid var(--border)")
            .set("border-radius", "0.75rem")
            .set("padding",       "1.5rem")
            .set("margin-bottom", "1.5rem");

        var abonTitle = new H2("Mon abonnement");
        abonTitle.getStyle()
            .set("margin", "0 0 1rem 0").set("font-size", "1rem")
            .set("font-weight", "700").set("color", "#1E293B");
        abonSection.add(abonTitle);

        // Grille méta dates
        var grid = new Div();
        grid.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "repeat(auto-fill, minmax(200px, 1fr))")
            .set("gap", "1rem").set("margin-bottom", "1.25rem");

        if (emp.getDebutAbonnement() != null)
            grid.add(metaCard(VaadinIcon.CALENDAR, "Début", emp.getDebutAbonnement().format(DATE_FMT)));
        if (emp.getExpirationAbonnement() != null)
            grid.add(metaCard(VaadinIcon.CALENDAR_CLOCK, "Expiration", emp.getExpirationAbonnement().format(DATE_FMT)));

        // Jours restants
        if (emp.getExpirationAbonnement() != null) {
            long jours = ChronoUnit.DAYS.between(LocalDate.now(), emp.getExpirationAbonnement());
            String joursLabel = jours > 0 ? jours + " jour(s) restant(s)" : "Expiré";
            grid.add(metaCard(VaadinIcon.CLOCK, "Durée restante", joursLabel));
        }
        abonSection.add(grid);

        // Bouton renouveler
        abonSection.add(buildBoutonRenouveler(emp));
        body.add(abonSection);

        // ── Infos personnelles ───────────────────────────────────────────
        var infoSection = new Div();
        infoSection.getStyle()
            .set("background",    "#fff")
            .set("border",        "1px solid var(--border)")
            .set("border-radius", "0.75rem")
            .set("padding",       "1.5rem")
            .set("margin-bottom", "1.5rem");

        var infoTitle = new H2("Mes informations");
        infoTitle.getStyle()
            .set("margin", "0 0 1rem 0").set("font-size", "1rem")
            .set("font-weight", "700").set("color", "#1E293B");
        infoSection.add(infoTitle);

        var infoGrid = new Div();
        infoGrid.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "repeat(auto-fill, minmax(200px, 1fr))")
            .set("gap", "1rem");

        if (emp.getEmail() != null)
            infoGrid.add(metaCard(VaadinIcon.ENVELOPE, "Email", emp.getEmail()));
        if (emp.getDateNaissance() != null)
            infoGrid.add(metaCard(VaadinIcon.GIFT, "Date de naissance", emp.getDateNaissance().format(DATE_FMT)));
        if (emp.getAdresseRue() != null)
            infoGrid.add(metaCard(VaadinIcon.MAP_MARKER, "Adresse",
                    emp.getAdresseRue() + "\n" + emp.getAdresseCodePostal() + " " + emp.getAdresseVille()));

        infoSection.add(infoGrid);
        body.add(infoSection);

        return body;
    }

    // ── BOUTON RENOUVELER ─────────────────────────────────────────────────
    private Div buildBoutonRenouveler(Emprunteur emp) {
        var container = new Div();
        container.getStyle().set("display", "flex").set("align-items", "center").set("gap", "1rem").set("flex-wrap", "wrap");

        var btn = new Button("Renouveler mon abonnement (1 an)", new Icon(VaadinIcon.REFRESH));
        btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btn.getStyle().set("background", ACCENT);

        btn.addClickListener(e -> {
            // Dialog de confirmation
            var dialog = new Dialog();
            dialog.setHeaderTitle("Confirmer le renouvellement");

            var msg = new Paragraph("Votre abonnement sera renouvelé pour 1 an à partir d'aujourd'hui. Confirmer ?");
            dialog.add(msg);

            var btnConfirm = new Button("Confirmer", new Icon(VaadinIcon.CHECK));
            btnConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnConfirm.getStyle().set("background", ACCENT);
            btnConfirm.addClickListener(ce -> {
                try {
                    LocalDate debut = LocalDate.now();
                    LocalDate fin   = debut.plusYears(1);
                    emp.setDebutAbonnement(debut);
                    emp.setExpirationAbonnement(fin);
                    Emprunteur saved = emprunteurService.updateEmprunteur(emp);
                    // Mettre à jour la session
                    SessionManager.loginEmprunteur(saved);
                    dialog.close();
                    var n = Notification.show(
                            "Abonnement renouvelé jusqu'au " + fin.format(DATE_FMT),
                            4000, Notification.Position.BOTTOM_CENTER);
                    n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    UI.getCurrent().getPage().reload();
                } catch (Exception ex) {
                    dialog.close();
                    var n = Notification.show("Erreur : " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });

            var btnAnnuler = new Button("Annuler");
            btnAnnuler.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            btnAnnuler.addClickListener(ce -> dialog.close());

            var btnRow = new HorizontalLayout(btnConfirm, btnAnnuler);
            dialog.getFooter().add(btnRow);
            dialog.open();
        });

        container.add(btn);

        // Message si abonnement expiré
        if (emp.getExpirationAbonnement() != null && emp.getExpirationAbonnement().isBefore(LocalDate.now())) {
            var expired = new Span("⚠ Votre abonnement est expiré");
            expired.getStyle()
                .set("font-size", "0.82rem").set("font-weight", "600")
                .set("color", "#B91C1C").set("background", "#FEE2E2")
                .set("padding", "0.3rem 0.75rem").set("border-radius", "999px");
            container.add(expired);
        }

        return container;
    }

    // ── BADGE STATUT ──────────────────────────────────────────────────────
    private Span buildStatutBadge(Emprunteur emp) {
        LocalDate exp = emp.getExpirationAbonnement();
        LocalDate now = LocalDate.now();

        if (exp == null) {
            return colorBadge("Pas d'abonnement", "#92400E", "#FEF3C7");
        } else if (exp.isBefore(now)) {
            return colorBadge("Abonnement expiré", "#B91C1C", "#FEE2E2");
        } else if (ChronoUnit.DAYS.between(now, exp) <= 14) {
            return colorBadge("Expire bientôt", "#92400E", "#FEF3C7");
        } else {
            return colorBadge("Abonnement actif", "#166534", "#DCFCE7");
        }
    }

    private Span colorBadge(String text, String color, String bg) {
        var s = new Span(text);
        s.getStyle()
            .set("font-size", "0.75rem").set("font-weight", "600")
            .set("color", color).set("background", bg)
            .set("padding", "0.25rem 0.7rem").set("border-radius", "999px");
        return s;
    }

    private Div metaCard(VaadinIcon icon, String label, String value) {
        var card = new Div();
        card.getStyle()
            .set("background", "#F8FAFC").set("border", "1px solid var(--border)")
            .set("border-radius", "0.6rem").set("padding", "0.85rem 1rem")
            .set("display", "flex").set("gap", "0.6rem").set("align-items", "flex-start");

        var ico = new Icon(icon);
        ico.getStyle()
            .set("color", ACCENT).set("background", ACCENT + "18")
            .set("padding", "0.35rem").set("border-radius", "0.35rem")
            .set("width", "1rem").set("height", "1rem").set("flex-shrink", "0");

        var info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        var lbl = new Span(label);
        lbl.getStyle()
            .set("font-size", "0.7rem").set("font-weight", "600")
            .set("color", "var(--muted)").set("text-transform", "uppercase")
            .set("letter-spacing", "0.05em");

        var val = new Span(value);
        val.getStyle()
            .set("font-size", "0.88rem").set("font-weight", "600")
            .set("color", "#1E293B").set("white-space", "pre-line");

        info.add(lbl, val);
        card.add(ico, info);
        return card;
    }

    private String initiales(Emprunteur emp) {
        String p = emp.getPrenom() != null && !emp.getPrenom().isEmpty()
                ? String.valueOf(emp.getPrenom().charAt(0)) : "";
        String n = emp.getNom() != null && !emp.getNom().isEmpty()
                ? String.valueOf(emp.getNom().charAt(0)) : "";
        return (p + n).toUpperCase();
    }
}
