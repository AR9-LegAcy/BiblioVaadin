package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.service.EvenementService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Route("evenement/detail")
@PageTitle("Détail Événement — Bibliothèque")
public class EvenementDetailView extends Main implements HasUrlParameter<Integer> {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private final EvenementService evenementService;

    public EvenementDetailView(EvenementService evenementService) {
        this.evenementService = evenementService;
        getStyle()
            .set("--surface",   "#FFFFFF")
            .set("--muted",     "#64748B")
            .set("--border",    "#E2E8F0")
            .set("font-family", "sans-serif")
            .set("display",     "flex")
            .set("flex-direction", "column");
    }

    @Override
    public void setParameter(BeforeEvent event, Integer id) {
        removeAll();
        Evenement ev = evenementService.getEvenementById(id);
        if (ev == null) {
            add(notFound("Événement introuvable."));
        } else {
            add(buildContent(ev));
        }
    }

    private VerticalLayout buildContent(Evenement ev) {
        var layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        // ── Bandeau couleur en haut ──────────────────────────────────────
        LocalDate today = LocalDate.now();
        boolean enCours = ev.getDateDebut() != null && ev.getDateFin() != null
                && !ev.getDateDebut().isAfter(today)
                && !ev.getDateFin().isBefore(today);
        String accentColor = enCours ? "#16A34A" : "#2563EB";

        var hero = new Div();
        hero.setWidthFull();
        hero.getStyle()
            .set("background",    "linear-gradient(135deg," + accentColor + " 0%, " +
                                  (enCours ? "#4ADE80" : "#3B82F6") + " 100%)")
            .set("color",         "#fff")
            .set("padding",       "2.5rem 2.5rem 2rem")
            .set("box-sizing",    "border-box");

        // Badge statut
        String badgeLabel = enCours ? "● En cours" :
                (ev.getDateDebut() != null && ev.getDateDebut().isAfter(today))
                        ? "À venir" : "Terminé";
        var badge = new Span(badgeLabel);
        badge.getStyle()
            .set("font-size", "0.72rem").set("font-weight", "600")
            .set("background", "rgba(255,255,255,0.25)")
            .set("padding", "0.2rem 0.6rem").set("border-radius", "999px")
            .set("display", "inline-block").set("margin-bottom", "0.75rem");

        var titre = new H1(ev.getTitre() != null ? ev.getTitre() : "Événement sans titre");
        titre.getStyle()
            .set("margin", "0 0 0.5rem 0")
            .set("font-size", "clamp(1.4rem,4vw,2.2rem)")
            .set("font-weight", "700");

        // Type d'événement
        if (ev.getTypeEvenement() != null && ev.getTypeEvenement().getNom() != null) {
            var type = new Span(ev.getTypeEvenement().getNom());
            type.getStyle()
                .set("font-size", "0.82rem").set("opacity", "0.9")
                .set("display", "block").set("margin-top", "0.25rem");
            hero.add(badge, titre, type);
        } else {
            hero.add(badge, titre);
        }
        layout.add(hero);

        // ── Corps de la page ─────────────────────────────────────────────
        var body = new Div();
        body.getStyle()
            .set("padding",    "2rem 2.5rem")
            .set("max-width",  "860px")
            .set("width",      "100%")
            .set("box-sizing", "border-box");

        // ── Grille de méta-infos ─────────────────────────────────────────
        var metaGrid = new Div();
        metaGrid.getStyle()
            .set("display",               "grid")
            .set("grid-template-columns", "repeat(auto-fill, minmax(200px, 1fr))")
            .set("gap",                   "1rem")
            .set("margin-bottom",         "2rem");

        // Date début
        if (ev.getDateDebut() != null) {
            metaGrid.add(metaCard(VaadinIcon.CALENDAR, "Date de début",
                    ev.getDateDebut().format(DATE_FMT), accentColor));
        }
        // Date fin
        if (ev.getDateFin() != null) {
            metaGrid.add(metaCard(VaadinIcon.CALENDAR_CLOCK, "Date de fin",
                    ev.getDateFin().format(DATE_FMT), accentColor));
        }
        // Lieu / Bibliothèque
        if (ev.getBibliotheque() != null) {
            var bib = ev.getBibliotheque();
            String adresse = bib.getNom();
            if (bib.getAdresseRue() != null)
                adresse += "\n" + bib.getAdresseRue();
            if (bib.getAdresseCP() != null && bib.getAdresseVille() != null)
                adresse += "\n" + bib.getAdresseCP() + " " + bib.getAdresseVille();
            metaGrid.add(metaCard(VaadinIcon.MAP_MARKER, "Lieu", adresse, accentColor));

            if (bib.getHoraires() != null) {
                metaGrid.add(metaCard(VaadinIcon.CLOCK, "Horaires", bib.getHoraires(), accentColor));
            }
        }

        body.add(metaGrid);

        // ── Description ──────────────────────────────────────────────────
        if (ev.getDescription() != null && !ev.getDescription().isBlank()) {
            var descSection = new Div();
            descSection.getStyle()
                .set("background",    "#F8FAFC")
                .set("border",        "1px solid var(--border)")
                .set("border-radius", "0.75rem")
                .set("padding",       "1.5rem")
                .set("margin-bottom", "2rem");

            var descTitle = new H2("Description");
            descTitle.getStyle()
                .set("margin", "0 0 0.75rem 0")
                .set("font-size", "1rem").set("font-weight", "700")
                .set("color", "#1E293B");

            var descText = new Paragraph(ev.getDescription());
            descText.getStyle()
                .set("margin", "0").set("line-height", "1.7")
                .set("color", "#334155");

            descSection.add(descTitle, descText);
            body.add(descSection);
        }

        // ── Bouton retour en bas ─────────────────────────────────────────
        var btnBottom = new Button(new Icon(VaadinIcon.ARROW_LEFT), e ->
                UI.getCurrent().navigate(MainView.class));
        btnBottom.setText("Retour à l'accueil");
        btnBottom.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        body.add(btnBottom);

        layout.add(body);
        return layout;
    }

    /** Carte méta-info (icône + label + valeur). */
    private Div metaCard(VaadinIcon icon, String label, String value, String color) {
        var card = new Div();
        card.getStyle()
            .set("background",    "#fff")
            .set("border",        "1px solid var(--border)")
            .set("border-radius", "0.75rem")
            .set("padding",       "1rem 1.1rem")
            .set("display",       "flex")
            .set("gap",           "0.75rem")
            .set("align-items",   "flex-start");

        var ico = new Icon(icon);
        ico.getStyle()
            .set("color",         color)
            .set("background",    color + "18")
            .set("padding",       "0.4rem")
            .set("border-radius", "0.4rem")
            .set("width",         "1.2rem")
            .set("height",        "1.2rem")
            .set("flex-shrink",   "0");

        var info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        var lbl = new Span(label);
        lbl.getStyle()
            .set("font-size",   "0.72rem").set("font-weight", "600")
            .set("color",       "var(--muted)").set("text-transform", "uppercase")
            .set("letter-spacing", "0.05em");

        var val = new Span(value);
        val.getStyle()
            .set("font-size",   "0.9rem").set("font-weight", "600")
            .set("color",       "#1E293B").set("white-space", "pre-line");

        info.add(lbl, val);
        card.add(ico, info);
        return card;
    }

    private Div notFound(String msg) {
        var d = new Div(new Span(msg));
        d.getStyle()
            .set("padding", "3rem").set("text-align", "center")
            .set("color", "var(--muted)").set("font-style", "italic");
        return d;
    }
}