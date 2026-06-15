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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route
@PageTitle("Accueil — Bibliothèque")
public final class MainView extends Main {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    MainView(EvenementService evenementService, LivreService livreService) {
        getStyle()
            .set("--accent",       "#2563EB")
            .set("--teal",         "#0F766E")
            .set("--surface",      "#FFFFFF")
            .set("--muted",        "#64748B")
            .set("--border",       "#E2E8F0")
            .set("font-family",    "sans-serif")
            .set("display",        "flex")
            .set("flex-direction", "column")
            .set("gap",            "0");

        add(buildHero());

        // ── Rangée 1 : Événements en cours | Événements à venir (pleine largeur) ──
        add(buildEvenementsRow(evenementService));

        // ── Rangée 2 : Nouvelles acquisitions (pleine largeur) ───────────────────
        add(buildAcquisitionsRow(livreService));
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
            .set("font-size", "0.75rem").set("font-weight", "600")
            .set("letter-spacing", "0.12em").set("text-transform", "uppercase")
            .set("opacity", "0.75").set("display", "block").set("margin-bottom", "0.5rem");

        var title = new H1("Bienvenue à la bibliothèque");
        title.getStyle()
            .set("margin", "0 0 0.75rem 0")
            .set("font-size", "clamp(1.6rem,4vw,2.4rem)")
            .set("font-weight", "700").set("line-height", "1.2");

        var sub = new Paragraph(
            "Découvrez nos nouvelles acquisitions, consultez les événements à venir "
          + "et gérez vos emprunts en ligne.");
        sub.getStyle()
            .set("margin", "0 0 1.5rem 0").set("opacity", "0.85")
            .set("max-width", "520px").set("font-size", "1rem");

        var btnRecherche = new Button("Rechercher un livre", new Icon(VaadinIcon.SEARCH));
        btnRecherche.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        btnRecherche.getStyle()
            .set("background", "#fff").set("color", "#1E3A8A")
            .set("font-weight", "600").set("border-radius", "0.5rem");
        btnRecherche.addClickListener(e -> UI.getCurrent().navigate("livre"));

        section.add(eyebrow, title, sub, btnRecherche);
        return section;
    }

    // ── RANGÉE ÉVÉNEMENTS (pleine largeur, grille responsive) ────────────

    private VerticalLayout buildEvenementsRow(EvenementService evenementService) {
        var wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        wrapper.getStyle().set("padding", "0 1rem 1.5rem").set("gap", "1.5rem");

        LocalDate today = LocalDate.now();

        List<Evenement> enCours = List.of();
        List<Evenement> futurs  = List.of();

        try {
            enCours = evenementService.getAllEvenements().stream()
                    .filter(ev -> ev.getDateDebut() != null && ev.getDateFin() != null)
                    .filter(ev -> !ev.getDateDebut().isAfter(today)
                               && !ev.getDateFin().isBefore(today))
                    .toList();

            final List<Evenement> enCoursRef = enCours;
            futurs = evenementService.getEvenementsFuturs().stream()
                    .filter(ev -> !enCoursRef.contains(ev))
                    .toList();
        } catch (Exception ignored) {}

        // ── Section "En cours" ──────────────────────────────────────────
        var secEnCours = new VerticalLayout();
        secEnCours.setPadding(false);
        secEnCours.setSpacing(false);
        secEnCours.setWidthFull();
        secEnCours.getStyle()
            .set("background",    "var(--surface)")
            .set("border",        "1px solid var(--border)")
            .set("border-radius", "1rem")
            .set("overflow",      "hidden");

        secEnCours.add(buildSectionHeader(VaadinIcon.CLOCK,
                "Événements en cours", "#16A34A", "evenement"));

        if (enCours.isEmpty()) {
            secEnCours.add(emptyState("Aucun événement en cours."));
        } else {
            // Grille CSS responsive : 4 par ligne max, descend à 1 sur mobile
            var grid = eventGrid();
            enCours.forEach(ev -> grid.add(buildEventCard(ev, true)));
            secEnCours.add(grid);
        }

        // ── Section "À venir" ───────────────────────────────────────────
        var secFuturs = new VerticalLayout();
        secFuturs.setPadding(false);
        secFuturs.setSpacing(false);
        secFuturs.setWidthFull();
        secFuturs.getStyle()
            .set("background",    "var(--surface)")
            .set("border",        "1px solid var(--border)")
            .set("border-radius", "1rem")
            .set("overflow",      "hidden");

        secFuturs.add(buildSectionHeader(VaadinIcon.CALENDAR,
                "Événements à venir", "#2563EB", "evenement"));

        if (futurs.isEmpty()) {
            secFuturs.add(emptyState("Aucun événement à venir."));
        } else {
            var grid = eventGrid();
            futurs.forEach(ev -> grid.add(buildEventCard(ev, false)));
            secFuturs.add(grid);
        }

        wrapper.add(secEnCours, secFuturs);
        return wrapper;
    }

    /**
     * Conteneur CSS grid responsive :
     *  ≥1200px → 4 colonnes   |   ≥800px → 3   |   ≥500px → 2   |   <500px → 1
     */
    private Div eventGrid() {
        var grid = new Div();
        grid.getStyle()
            .set("display",               "grid")
            .set("grid-template-columns",
                 "repeat(auto-fill, minmax(min(100%, 260px), 1fr))")
            .set("width",                 "100%");
        return grid;
    }

    private Div buildEventCard(Evenement ev, boolean isEnCours) {
        var card = new Div();
        card.getStyle()
            .set("padding",      "1rem 1.1rem")
            .set("border-right", "1px solid var(--border)")
            .set("border-bottom","1px solid var(--border)")
            .set("cursor",       "pointer")
            .set("transition",   "background 0.15s");
        card.getElement().addEventListener("mouseover",
            e -> card.getStyle().set("background", "#F8FAFC"));
        card.getElement().addEventListener("mouseout",
            e -> card.getStyle().set("background", "transparent"));
        card.addClickListener(e -> UI.getCurrent().navigate("evenement/detail/" + ev.getId()));

        // Badge
        Span badge;
        if (isEnCours) {
            badge = new Span("● En cours");
            badge.getStyle()
                .set("font-size", "0.7rem").set("font-weight", "600")
                .set("color", "#fff").set("background", "#16A34A")
                .set("padding", "0.15rem 0.5rem").set("border-radius", "999px")
                .set("display", "inline-block").set("margin-bottom", "0.35rem");
        } else {
            String dateLabel = ev.getDateDebut() != null
                    ? ev.getDateDebut().format(DATE_FMT) : "Date inconnue";
            if (ev.getDateFin() != null && ev.getDateDebut() != null
                    && !ev.getDateFin().equals(ev.getDateDebut())) {
                dateLabel += " → " + ev.getDateFin().format(DATE_FMT);
            }
            badge = new Span(dateLabel);
            badge.getStyle()
                .set("font-size", "0.7rem").set("font-weight", "600")
                .set("color", "#2563EB").set("background", "#EFF6FF")
                .set("padding", "0.15rem 0.5rem").set("border-radius", "999px")
                .set("display", "inline-block").set("margin-bottom", "0.35rem");
        }

        var titre = new H3(ev.getTitre() != null ? ev.getTitre() : "Événement sans titre");
        titre.getStyle()
            .set("margin", "0 0 0.25rem 0")
            .set("font-size", "0.95rem").set("font-weight", "600");

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
            ico.getStyle().set("width", "0.9rem").set("height", "0.9rem")
               .set("color", "var(--muted)");
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
            desc.getStyle()
                .set("margin", "0").set("font-size", "0.82rem")
                .set("color", "var(--muted)");
            card.add(desc);
        }

        return card;
    }

    // ── RANGÉE ACQUISITIONS (pleine largeur) ──────────────────────────────

    private VerticalLayout buildAcquisitionsRow(LivreService livreService) {
        // Même wrapper que buildEvenementsRow : padding latéral géré ici,
        // pas de margin sur l'enfant pour éviter width:100% + margin = débordement
        var wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        wrapper.setWidthFull();
        wrapper.getStyle().set("padding", "0 1rem 2rem");

        var section = card();
        section.setWidthFull();
        section.getStyle().set("box-sizing", "border-box");
        section.add(buildSectionHeader(VaadinIcon.BOOK,
                "Nouvelles acquisitions", "#0F766E", "livre"));

        List<Livre> livres;
        try {
            livres = livreService.getAllLivres();
        } catch (Exception ex) {
            livres = List.of();
        }

        if (livres.isEmpty()) {
            section.add(emptyState("Aucune acquisition récente."));
        } else {
            var grid = new Div();
            grid.getStyle()
                .set("display",               "grid")
                .set("grid-template-columns", "repeat(auto-fill, minmax(220px, 1fr))")
                .set("gap",                   "0")
                .set("width",                 "100%");
            livres.stream().limit(6).forEach(l -> grid.add(buildLivreCard(l)));
            section.add(grid);
        }

        wrapper.add(section);
        return wrapper;
    }

    private Div buildLivreCard(Livre livre) {
        var card = new Div();
        card.getStyle()
            .set("padding",       "0.9rem 1.1rem")
            .set("border-right",  "1px solid var(--border)")
            .set("border-bottom", "1px solid var(--border)")
            .set("display",       "flex")
            .set("gap",           "0.75rem")
            .set("align-items",   "flex-start")
            .set("cursor",        "pointer")
            .set("transition",    "background 0.15s");
        card.getElement().addEventListener("mouseover",
            e -> card.getStyle().set("background", "#F0FDF4"));
        card.getElement().addEventListener("mouseout",
            e -> card.getStyle().set("background", "transparent"));
        card.addClickListener(e -> UI.getCurrent().navigate("livre/detail/" + livre.getIdLivre()));

        var ico = new Icon(VaadinIcon.BOOK);
        ico.getStyle()
            .set("color", "#0F766E").set("background", "#0F766E18")
            .set("padding", "0.45rem").set("border-radius", "0.4rem")
            .set("min-width", "2rem").set("height", "2rem").set("flex-shrink", "0");

        var info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        var titre = new H3(livre.getTitreLivre() != null ? livre.getTitreLivre() : "Sans titre");
        titre.getStyle()
            .set("margin", "0 0 0.15rem 0")
            .set("font-size", "0.93rem").set("font-weight", "600");

        var meta = new Div();
        meta.getStyle().set("display", "flex").set("gap", "0.4rem").set("flex-wrap", "wrap");

        if (livre.getIdEditeur() != null && livre.getIdEditeur().getNom() != null) {
            var editeurSpan = new Span(livre.getIdEditeur().getNom());
            editeurSpan.getStyle().set("font-size", "0.75rem").set("color", "var(--muted)");
            meta.add(editeurSpan);
        }

        if (livre.getDatePublication() != null) {
            var dateSpan = new Span("· " + livre.getDatePublication().format(DATE_FMT));
            dateSpan.getStyle().set("font-size", "0.75rem").set("color", "var(--muted)");
            meta.add(dateSpan);
        }

        if (livre.getCodeIsbn() != null) {
            var isbn = new Span("ISBN " + livre.getCodeIsbn());
            isbn.getStyle()
                .set("font-size", "0.7rem").set("color", "#0F766E")
                .set("background", "#0F766E12").set("padding", "0.1rem 0.35rem")
                .set("border-radius", "4px");
            meta.add(isbn);
        }

        if (Boolean.FALSE.equals(livre.getCodeEmpruntable())) {
            var nonEmp = new Span("Non empruntable");
            nonEmp.getStyle()
                .set("font-size", "0.7rem").set("color", "#B91C1C")
                .set("background", "#FEE2E2").set("padding", "0.1rem 0.35rem")
                .set("border-radius", "4px");
            meta.add(nonEmp);
        }

        info.add(titre, meta);
        card.add(ico, info);
        return card;
    }

    // ── HELPERS ───────────────────────────────────────────────────────────

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

    private Div buildSectionHeader(VaadinIcon icon, String label,
                                   String color, String navTarget) {
        var header = new Div();
        header.setWidthFull();
        header.getStyle()
            .set("display", "flex").set("align-items", "center")
            .set("justify-content", "space-between")
            .set("padding", "1rem 1.1rem 0.8rem")
            .set("border-bottom", "2px solid " + color)
            .set("background", color + "0D")
            .set("box-sizing", "border-box");

        var left = new Div();
        left.getStyle().set("display", "flex").set("align-items", "center").set("gap", "0.5rem");

        var ico = new Icon(icon);
        ico.getStyle().set("color", color).set("width", "1.1rem").set("height", "1.1rem");

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
            .set("padding", "2rem 1rem").set("text-align", "center")
            .set("color", "var(--muted)").set("font-size", "0.85rem")
            .set("font-style", "italic");
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