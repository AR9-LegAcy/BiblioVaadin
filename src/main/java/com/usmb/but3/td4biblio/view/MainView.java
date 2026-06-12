// package com.usmb.but3.td4biblio.view;

// import com.usmb.but3.td4biblio.service.EvenementService;
// import com.vaadin.flow.component.UI;
// import com.vaadin.flow.component.html.Div;
// import com.vaadin.flow.component.html.H2;
// import com.vaadin.flow.component.html.Main;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import com.vaadin.flow.router.Route;
// import com.vaadin.flow.theme.lumo.LumoUtility;

// /**
//  * This view shows up when a user navigates to the root ('/') of the application.
//  */
// @Route
// public final class MainView extends Main {

//     public MainView (EvenementService evenementService) {
//         addClassName(LumoUtility.Padding.MEDIUM);
//         //add(new ViewToolbar("Main"));
//         add(new Div("Choisissez une option dans le menu à gauche."));
        
//         //les evenement
//         add(new H2("Événements à venir"));

//         VerticalLayout listeEvenements = new VerticalLayout();

//         evenementService.getEvenementsFuturs().forEach(e -> {

//             listeEvenements.add(
//                 new Div(
//                     e.getDateDebut()
//                     + " - "
//                     + e.getTitre()
//                 )
//             );

//         });

//         add(listeEvenements);
//     }

//     /**
//      * Navigates to the main view.
//      */
//     public static void showMainView() {
//         UI.getCurrent().navigate(MainView.class);
//     }
// }

package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.EvenementService;
import com.usmb.but3.td4biblio.service.LivreService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route
@PageTitle("Accueil — Bibliothèque")
public final class MainView extends Main {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    MainView(EvenementService evenementService, LivreService livreService) {

        // Variables CSS globales portées par le composant racine
        getStyle()
            .set("--accent",    "#2563EB")
            .set("--teal",      "#0F766E")
            .set("--surface",   "#FFFFFF")
            .set("--muted",     "#64748B")
            .set("--border",    "#E2E8F0")
            .set("font-family", "sans-serif")
            .set("display",     "flex")
            .set("flex-direction", "column")
            .set("gap",         "0");

        add(buildHero());
        add(buildTwoCol(evenementService, livreService));
    }

    // ── HERO ──────────────────────────────────────────────────────────────

    private Section buildHero() {
        var section = new Section();
        section.getStyle()
            .set("background",    "linear-gradient(135deg,#1E3A8A 0%,#2563EB 60%,#3B82F6 100%)")
            .set("color",         "#fff")
            .set("padding",       "3rem 2.5rem 2.5rem")
            .set("border-radius", "0 0 1.5rem 1.5rem")
            .set("margin-bottom", "2rem");

        var eyebrow = new Span("Réseau des Bibliothèques");
        eyebrow.getStyle()
            .set("font-size",      "0.75rem")
            .set("font-weight",    "600")
            .set("letter-spacing", "0.12em")
            .set("text-transform", "uppercase")
            .set("opacity",        "0.75")
            .set("display",        "block")
            .set("margin-bottom",  "0.5rem");

        var title = new H1("Bienvenue à la bibliothèque");
        title.getStyle()
            .set("margin",      "0 0 0.75rem 0")
            .set("font-size",   "clamp(1.6rem,4vw,2.4rem)")
            .set("font-weight", "700")
            .set("line-height", "1.2");

        var sub = new Paragraph(
            "Découvrez nos nouvelles acquisitions, consultez les événements à venir "
          + "et gérez vos emprunts en ligne.");
        sub.getStyle()
            .set("margin",    "0 0 1.5rem 0")
            .set("opacity",   "0.85")
            .set("max-width", "520px")
            .set("font-size", "1rem");

        var btnRecherche = new Button("Rechercher un livre",
                new Icon(VaadinIcon.SEARCH));
        btnRecherche.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                                      ButtonVariant.LUMO_LARGE);
        btnRecherche.getStyle()
            .set("background",    "#fff")
            .set("color",         "#1E3A8A")
            .set("font-weight",   "600")
            .set("border-radius", "0.5rem");
        btnRecherche.addClickListener(e -> UI.getCurrent().navigate("livre"));

        section.add(eyebrow, title, sub, btnRecherche);
        return section;
    }

    // ── DEUX COLONNES ─────────────────────────────────────────────────────

    private HorizontalLayout buildTwoCol(EvenementService evenementService,
                                         LivreService livreService) {
        var col1 = buildEvenementsSection(evenementService);
        var col2 = buildAcquisitionsSection(livreService);

        var layout = new HorizontalLayout(col1, col2);
        layout.setWidthFull();
        layout.getStyle()
            .set("padding",   "0 1rem 2rem")
            .set("gap",       "1.5rem")
            .set("flex-wrap", "wrap");
        layout.setFlexGrow(1, col1);
        layout.setFlexGrow(1, col2);
        return layout;
    }

    // ── ÉVÉNEMENTS ────────────────────────────────────────────────────────

    private HorizontalLayout buildEvenementsSection(EvenementService evenementService) {
        var layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setSpacing(true);
        layout.getStyle().set("gap", "1.5rem");
    
        // ── Containers pour chaque colonne ───────────────────────
        var colEnCours = new VerticalLayout();
        colEnCours.setPadding(false);
        colEnCours.setSpacing(false);
        colEnCours.setWidthFull();
    
        var colFuturs = new VerticalLayout();
        colFuturs.setPadding(false);
        colFuturs.setSpacing(false);
        colFuturs.setWidthFull();
    
        // ── Headers ─────────────────────────────
        colEnCours.add(buildSectionHeader(VaadinIcon.CLOCK, "Événements en cours", "#16A34A", "evenement"));
        colFuturs.add(buildSectionHeader(VaadinIcon.CALENDAR, "Événements à venir", "#2563EB", "evenement"));
    
        LocalDate today = LocalDate.now();
    
        try {
            // Événements en cours
            List<Evenement> enCours = evenementService.getAllEvenements().stream()
                    .filter(ev -> ev.getDateDebut() != null && ev.getDateFin() != null)
                    .filter(ev -> !ev.getDateDebut().isAfter(today) && !ev.getDateFin().isBefore(today))
                    .toList();
    
            if (enCours.isEmpty()) {
                colEnCours.add(emptyState("Aucun événement en cours."));
            } else {
                enCours.forEach(ev -> colEnCours.add(buildEventCard(ev, today)));
            }
    
            // Événements futurs
            List<Evenement> futurs = evenementService.getEvenementsFuturs();
            List<Evenement> futursFiltrés = futurs.stream()
                    .filter(ev -> !enCours.contains(ev))
                    .toList();
    
            if (futursFiltrés.isEmpty()) {
                colFuturs.add(emptyState("Aucun événement à venir."));
            } else {
                futursFiltrés.forEach(ev -> colFuturs.add(buildEventCard(ev, today)));
            }
    
        } catch (Exception ex) {
            colEnCours.add(emptyState("Impossible de charger les événements en cours."));
            colFuturs.add(emptyState("Impossible de charger les événements à venir."));
        }
    
        // ── Ajout des deux colonnes côte à côte ───────────────
        layout.add(colEnCours, colFuturs);
        layout.setFlexGrow(1, colEnCours);
        layout.setFlexGrow(1, colFuturs);
    
        return layout;
    }

    private Div buildEventCard(Evenement ev, LocalDate today) {
        var card = new Div();
        card.getStyle()
            .set("padding",       "0.9rem 1.1rem")
            .set("border-bottom", "1px solid var(--border)");

        // Badge "En cours" ou date
        boolean enCours = ev.getDateDebut() != null && ev.getDateFin() != null
                && !ev.getDateDebut().isAfter(today)
                && !ev.getDateFin().isBefore(today);

        Span badge;
        if (enCours) {
            badge = new Span("● En cours");
            badge.getStyle()
                .set("font-size",     "0.7rem")
                .set("font-weight",   "600")
                .set("color",         "#fff")
                .set("background",    "#16A34A")
                .set("padding",       "0.15rem 0.5rem")
                .set("border-radius", "999px")
                .set("display",       "inline-block")
                .set("margin-bottom", "0.35rem");
        } else {
            String dateLabel = ev.getDateDebut() != null
                    ? ev.getDateDebut().format(DATE_FMT) : "Date inconnue";
            if (ev.getDateFin() != null && ev.getDateDebut() != null
                    && !ev.getDateFin().equals(ev.getDateDebut())) {
                dateLabel += " → " + ev.getDateFin().format(DATE_FMT);
            }
            badge = new Span(dateLabel);
            badge.getStyle()
                .set("font-size",     "0.7rem")
                .set("font-weight",   "600")
                .set("color",         "#2563EB")
                .set("background",    "#EFF6FF")
                .set("padding",       "0.15rem 0.5rem")
                .set("border-radius", "999px")
                .set("display",       "inline-block")
                .set("margin-bottom", "0.35rem");
        }

        var titre = new H3(ev.getTitre() != null ? ev.getTitre() : "Événement sans titre");
        titre.getStyle()
            .set("margin", "0 0 0.25rem 0").set("font-size", "0.95rem").set("font-weight", "600");

        card.add(badge, titre);

        if (ev.getTypeEvenement() != null && ev.getTypeEvenement().getNom() != null) {
            var typeSpan = new Span(ev.getTypeEvenement().getNom());
            typeSpan.getStyle()
                .set("font-size", "0.72rem").set("color", "#fff")
                .set("background", "#6366F1").set("padding", "0.1rem 0.45rem")
                .set("border-radius", "999px").set("display", "inline-block")
                .set("margin-bottom", "0.3rem");
            card.add(typeSpan);
        }

        if (ev.getBibliotheque() != null && ev.getBibliotheque().getNom() != null) {
            var lieuRow = new Div();
            var ico = new Icon(VaadinIcon.MAP_MARKER);
            ico.getStyle().set("width", "0.9rem").set("height", "0.9rem").set("color", "var(--muted)");
            var lieuText = new Span(ev.getBibliotheque().getNom());
            lieuText.getStyle().set("font-size", "0.8rem").set("color", "var(--muted)");
            lieuRow.add(ico, lieuText);
            lieuRow.getStyle()
                .set("display", "flex").set("align-items", "center")
                .set("gap", "0.25rem").set("margin-bottom", "0.3rem");
            card.add(lieuRow);
        }

        if (ev.getDescription() != null && !ev.getDescription().isBlank()) {
            var desc = new Paragraph(truncate(ev.getDescription(), 90));
            desc.getStyle().set("margin", "0").set("font-size", "0.82rem").set("color", "var(--muted)");
            card.add(desc);
        }

        return card;
    }

    // ── NOUVELLES ACQUISITIONS ────────────────────────────────────────────

    private VerticalLayout buildAcquisitionsSection(LivreService livreService) {
        var section = card();
        section.add(buildSectionHeader(VaadinIcon.BOOK,
                "Nouvelles acquisitions", "#0F766E", "livre"));

        List<Livre> livres;
        try {
            // getAllLivres() trie par titre ASC ; on prend les 6 premiers
            // (si tu ajoutes findRecentAcquisitions() au service, utilise-la)
            livres = livreService.getAllLivres();
        } catch (Exception ex) {
            livres = List.of();
        }

        if (livres.isEmpty()) {
            section.add(emptyState("Aucune acquisition récente."));
        } else {
            livres.stream().limit(6).forEach(l -> section.add(buildLivreCard(l)));
        }

        return section;
    }

    private Div buildLivreCard(Livre livre) {
        var card = new Div();
        card.getStyle()
            .set("padding",      "0.9rem 1.1rem")
            .set("border-bottom","1px solid var(--border)")
            .set("display",      "flex")
            .set("gap",          "0.75rem")
            .set("align-items",  "flex-start");

        // Icône livre
        var ico = new Icon(VaadinIcon.BOOK);
        ico.getStyle()
            .set("color",         "#0F766E")
            .set("background",    "#0F766E18")
            .set("padding",       "0.45rem")
            .set("border-radius", "0.4rem")
            .set("min-width",     "2rem")
            .set("height",        "2rem")
            .set("flex-shrink",   "0");

        var info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        // Titre — livre.getTitreLivre()
        var titre = new H3(livre.getTitreLivre() != null
                ? livre.getTitreLivre() : "Sans titre");
        titre.getStyle()
            .set("margin",      "0 0 0.15rem 0")
            .set("font-size",   "0.93rem")
            .set("font-weight", "600");

        // Méta : éditeur + date publication
        // Editeur : livre.getIdEditeur().getNom()  (le champ JPA s'appelle idEditeur)
        var meta = new Div();
        meta.getStyle()
            .set("display", "flex").set("gap", "0.4rem").set("flex-wrap", "wrap");

        if (livre.getIdEditeur() != null && livre.getIdEditeur().getNom() != null) {
            var editeurSpan = new Span(livre.getIdEditeur().getNom());
            editeurSpan.getStyle()
                .set("font-size", "0.75rem").set("color", "var(--muted)");
            meta.add(editeurSpan);
        }

        if (livre.getDatePublication() != null) {
            var dateSpan = new Span("· " + livre.getDatePublication().format(DATE_FMT));
            dateSpan.getStyle()
                .set("font-size", "0.75rem").set("color", "var(--muted)");
            meta.add(dateSpan);
        }

        // Badge ISBN
        if (livre.getCodeIsbn() != null) {
            var isbn = new Span("ISBN " + livre.getCodeIsbn());
            isbn.getStyle()
                .set("font-size",     "0.7rem")
                .set("color",         "#0F766E")
                .set("background",    "#0F766E12")
                .set("padding",       "0.1rem 0.35rem")
                .set("border-radius", "4px");
            meta.add(isbn);
        }

        // Badge empruntable
        if (Boolean.FALSE.equals(livre.getCodeEmpruntable())) {
            var nonEmp = new Span("Non empruntable");
            nonEmp.getStyle()
                .set("font-size",     "0.7rem")
                .set("color",         "#B91C1C")
                .set("background",    "#FEE2E2")
                .set("padding",       "0.1rem 0.35rem")
                .set("border-radius", "4px");
            meta.add(nonEmp);
        }

        info.add(titre, meta);
        card.add(ico, info);
        return card;
    }

    // ── HELPERS ───────────────────────────────────────────────────────────

    /** Conteneur carte avec bordure et coins arrondis. */
    private VerticalLayout card() {
        var v = new VerticalLayout();
        v.setPadding(false);
        v.setSpacing(false);
        v.getStyle()
            .set("background",    "var(--surface)")
            .set("border",        "1px solid var(--border)")
            .set("border-radius", "1rem")
            .set("overflow",      "hidden")
            .set("min-width",     "280px");
        return v;
    }

    /** En-tête coloré d'une section avec titre + bouton "Voir tout". */
    private Div buildSectionHeader(VaadinIcon icon, String label,
                                   String color, String navTarget) {
        var header = new Div();
        header.getStyle()
            .set("display",         "flex")
            .set("align-items",     "center")
            .set("justify-content", "space-between")
            .set("padding",         "1rem 1.1rem 0.8rem")
            .set("border-bottom",   "2px solid " + color)
            .set("background",      color + "0D");

        var left = new Div();
        left.getStyle()
            .set("display", "flex").set("align-items", "center").set("gap", "0.5rem");

        var ico = new Icon(icon);
        ico.getStyle()
            .set("color", color).set("width", "1.1rem").set("height", "1.1rem");

        var title = new H2(label);
        title.getStyle()
            .set("margin", "0").set("font-size", "1rem")
            .set("font-weight", "700").set("color", "#1E293B");

        left.add(ico, title);

        var btn = new Button("Voir tout");
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        btn.getStyle().set("color", color).set("font-size", "0.78rem");
        btn.addClickListener(e -> UI.getCurrent().navigate(navTarget));

        header.add(left, btn);
        return header;
    }

    private Div emptyState(String msg) {
        var d = new Div(new Span(msg));
        d.getStyle()
            .set("padding",     "2rem 1rem")
            .set("text-align",  "center")
            .set("color",       "var(--muted)")
            .set("font-size",   "0.85rem")
            .set("font-style",  "italic");
        return d;
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }

    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}