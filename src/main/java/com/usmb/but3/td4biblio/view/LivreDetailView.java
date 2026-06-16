package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.LivreService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("livre/detail")
@PageTitle("Détail Livre — Bibliothèque")
public class LivreDetailView extends Main implements HasUrlParameter<Integer> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final String ACCENT = "#0F766E";
    private final DocumentService documentService;
    private final LivreService livreService;

    public LivreDetailView(LivreService livreService, DocumentService documentService) {
        this.livreService = livreService;
        this.documentService = documentService;
        getStyle()
                .set("--surface", "#FFFFFF")
                .set("--muted", "#64748B")
                .set("--border", "#E2E8F0")
                .set("font-family", "sans-serif")
                .set("display", "flex")
                .set("flex-direction", "column");
    }

    @Override
    public void setParameter(BeforeEvent event, Integer id) {
        removeAll();
        Livre livre = livreService.getLivreById(id);
        if (livre == null) {
            add(notFound("Livre introuvable."));
        } else {
            add(buildContent(livre));
        }
    }

    private VerticalLayout buildContent(Livre livre) {
        Document doc = null;
        if (livre.getDocument() != null && livre.getDocument().getCodeIsbn() != null) {
            List<Document> docs = documentService.getDocumentsByCodeIsbn(
                    livre.getDocument().getCodeIsbn());
            if (!docs.isEmpty())
                doc = docs.get(0);
        }
        var layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        // ── Bandeau ─────────────────────────────────────────────────────
        var hero = new Div();
        hero.setWidthFull();
        hero.getStyle()
                .set("background", "linear-gradient(135deg,#0F766E 0%,#14B8A6 100%)")
                .set("color", "#fff")
                .set("padding", "2.5rem 2.5rem 2rem")
                .set("box-sizing", "border-box");

        var titre = new H1(livre.getTitreLivre() != null ? livre.getTitreLivre() : "Sans titre");
        titre.getStyle()
                .set("margin", "0 0 0.4rem 0")
                .set("font-size", "clamp(1.4rem,4vw,2.2rem)")
                .set("font-weight", "700");

        // Éditeur sous le titre
        if (livre.getIdEditeur() != null && livre.getIdEditeur().getNom() != null) {
            var editeur = new Span("Éditeur : " + livre.getIdEditeur().getNom());
            editeur.getStyle()
                    .set("font-size", "0.9rem").set("opacity", "0.85").set("display", "block");
            hero.add(titre, editeur);
        } else {
            hero.add(titre);
        }
        // Badge empruntable
        if (Boolean.FALSE.equals(doc.getCodeEmpruntable())) {
            var nonEmp = new Span("⚠ Non empruntable");
            nonEmp.getStyle()
                    .set("font-size", "0.75rem").set("font-weight", "600")
                    .set("background", "rgba(239,68,68,0.85)")
                    .set("padding", "0.2rem 0.7rem").set("border-radius", "999px")
                    .set("display", "inline-block").set("margin-top", "0.6rem");
            hero.add(nonEmp);
        }

        layout.add(hero);

        // ── Corps ────────────────────────────────────────────────────────
        var body = new Div();
        body.getStyle()
                .set("padding", "2rem 2.5rem")
                .set("max-width", "860px")
                .set("width", "100%")
                .set("box-sizing", "border-box");

        // ── Grille méta-infos ────────────────────────────────────────────
        var metaGrid = new Div();
        metaGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fill, minmax(200px, 1fr))")
                .set("gap", "1rem")
                .set("margin-bottom", "2rem");

        // Date publication
        if (livre.getDatePublication() != null) {
            metaGrid.add(metaCard(VaadinIcon.CALENDAR, "Date de publication",
                    livre.getDatePublication().format(DATE_FMT), ACCENT));
        }
        // Date acquisition
        if (doc.getDateAcquisition() != null) {
            metaGrid.add(metaCard(VaadinIcon.PLUS_CIRCLE, "Acquis le",
                    doc.getDateAcquisition().format(DATE_FMT), ACCENT));
        }
        // Nombre de pages
        if (livre.getNbPages() != null) {
            metaGrid.add(metaCard(VaadinIcon.FILE_TEXT, "Pages",
                    livre.getNbPages() + " pages", ACCENT));
        }
        if (doc.getEtatDocument() != null && !doc.getEtatDocument().isBlank()) {
            metaGrid.add(metaCard(VaadinIcon.INFO_CIRCLE, "État",
                    doc.getEtatDocument(), ACCENT));
        }
        // Éditeur (site web & wikipedia)
        if (livre.getIdEditeur() != null) {
            var ed = livre.getIdEditeur();
            if (ed.getSiteWeb() != null && !ed.getSiteWeb().isBlank()) {
                metaGrid.add(metaCard(VaadinIcon.GLOBE, "Site web éditeur",
                        ed.getSiteWeb(), ACCENT));
            }
        }

        body.add(metaGrid);

        // ── Infos éditeur ────────────────────────────────────────────────
        if (livre.getIdEditeur() != null) {
            var ed = livre.getIdEditeur();
            var edSection = new Div();
            edSection.getStyle()
                    .set("background", "#fff")
                    .set("border", "1px solid var(--border)")
                    .set("border-radius", "0.75rem")
                    .set("padding", "1.5rem")
                    .set("margin-bottom", "2rem");

            var edTitle = new H2("À propos de l'éditeur");
            edTitle.getStyle()
                    .set("margin", "0 0 0.75rem 0")
                    .set("font-size", "1rem").set("font-weight", "700")
                    .set("color", "#1E293B");

            edSection.add(edTitle);

            if (ed.getNom() != null) {
                var nom = new Paragraph("📌 " + ed.getNom());
                nom.getStyle().set("margin", "0 0 0.3rem 0").set("font-weight", "600");
                edSection.add(nom);
            }
            if (ed.getAdresse() != null && !ed.getAdresse().isBlank()) {
                var addr = new Paragraph("📍 " + ed.getAdresse());
                addr.getStyle().set("margin", "0 0 0.3rem 0").set("font-size", "0.88rem")
                        .set("color", "var(--muted)");
                edSection.add(addr);
            }
            if (ed.getWikipedia() != null && !ed.getWikipedia().isBlank()) {
                var wiki = new Anchor(ed.getWikipedia(), "🔗 Page Wikipedia");
                wiki.setTarget("_blank");
                wiki.getStyle().set("font-size", "0.88rem").set("color", ACCENT);
                edSection.add(wiki);
            }

            body.add(edSection);
        }

        // Bouton retour bas
        var btnBottom = new Button("Retour à l'accueil");
        btnBottom.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnBottom.addClickListener(e -> UI.getCurrent().navigate(MainView.class));
        body.add(btnBottom);

        layout.add(body);
        return layout;
    }

    private Div metaCard(VaadinIcon icon, String label, String value, String color) {
        var card = new Div();
        card.getStyle()
                .set("background", "#fff")
                .set("border", "1px solid var(--border)")
                .set("border-radius", "0.75rem")
                .set("padding", "1rem 1.1rem")
                .set("display", "flex")
                .set("gap", "0.75rem")
                .set("align-items", "flex-start");

        var ico = new Icon(icon);
        ico.getStyle()
                .set("color", color).set("background", color + "18")
                .set("padding", "0.4rem").set("border-radius", "0.4rem")
                .set("width", "1.2rem").set("height", "1.2rem")
                .set("flex-shrink", "0");

        var info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        var lbl = new Span(label);
        lbl.getStyle()
                .set("font-size", "0.72rem").set("font-weight", "600")
                .set("color", "var(--muted)").set("text-transform", "uppercase")
                .set("letter-spacing", "0.05em");

        var val = new Span(value);
        val.getStyle()
                .set("font-size", "0.9rem").set("font-weight", "600")
                .set("color", "#1E293B").set("white-space", "pre-line");

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