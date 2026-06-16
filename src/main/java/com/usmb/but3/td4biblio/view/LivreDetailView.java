package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.EmprunterId;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EmprunterService;
import com.usmb.but3.td4biblio.service.LivreService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("livre/detail")
@PageTitle("Détail Livre — Bibliothèque")
public class LivreDetailView extends Main implements HasUrlParameter<Integer> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final String ACCENT   = "#0F766E";
    // Durée standard d'un prêt et prolongation max (en jours)
    private static final int    DUREE_PRET    = 35; // 5 semaines
    private static final int    PROLONGATION  = 7;

    private final LivreService    livreService;
    private final DocumentService documentService;
    private final EmprunterService emprunterService;

    public LivreDetailView(LivreService livreService,
                           DocumentService documentService,
                           EmprunterService emprunterService) {
        this.livreService     = livreService;
        this.documentService  = documentService;
        this.emprunterService = emprunterService;
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
        Livre livre = livreService.getLivreById(id);
        if (livre == null) {
            add(notFound("Livre introuvable."));
        } else {
            add(buildContent(livre));
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    private VerticalLayout buildContent(Livre livre) {

        // Récupération du Document via ISBN (unique)
        Document doc = null;
        if (livre.getDocument() != null && livre.getDocument().getCodeIsbn() != null) {
            List<Document> docs = documentService.getDocumentsByCodeIsbn(
                    livre.getDocument().getCodeIsbn());
            if (!docs.isEmpty()) doc = docs.get(0);
        }

        // Emprunt actif pour ce document (dateRetourReelle == null)
        Emprunter empruntActif = null;
        if (doc != null) {
            empruntActif = emprunterService.getEmpruntsByIdDocument(doc.getIdDocument())
                    .stream()
                    .filter(e -> e.getDateRetourReelle() == null)
                    .findFirst()
                    .orElse(null);
        }

        var layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        layout.add(buildHero(livre, doc, empruntActif));
        layout.add(buildBody(livre, doc, empruntActif));
        return layout;
    }

    // ── HERO ─────────────────────────────────────────────────────────────
    private Div buildHero(Livre livre, Document doc, Emprunter empruntActif) {
        var hero = new Div();
        hero.setWidthFull();
        hero.getStyle()
            .set("background",  "linear-gradient(135deg,#0F766E 0%,#14B8A6 100%)")
            .set("color",       "#fff")
            .set("padding",     "2.5rem 2.5rem 2rem")
            .set("box-sizing",  "border-box");

        // Badge type document
        if (doc != null && doc.getTypeDocument().getIdTypeDocument() != null
                && doc.getTypeDocument().getNomTypeDocument() != null) {
            var badge = new Span(doc.getTypeDocument().getNomTypeDocument());
            badge.getStyle()
                .set("font-size", "0.72rem").set("font-weight", "600")
                .set("background", "rgba(255,255,255,0.25)")
                .set("padding", "0.2rem 0.6rem").set("border-radius", "999px")
                .set("display", "inline-block").set("margin-bottom", "0.75rem");
            hero.add(badge);
        }

        var titre = new H1(livre.getTitreLivre() != null ? livre.getTitreLivre() : "Sans titre");
        titre.getStyle()
            .set("margin", "0 0 0.4rem 0")
            .set("font-size", "clamp(1.4rem,4vw,2.2rem)")
            .set("font-weight", "700");
        hero.add(titre);

        if (livre.getIdEditeur() != null && livre.getIdEditeur().getNom() != null) {
            var editeur = new Span("Éditeur : " + livre.getIdEditeur().getNom());
            editeur.getStyle()
                .set("font-size", "0.9rem").set("opacity", "0.85")
                .set("display", "block").set("margin-bottom", "0.6rem");
            hero.add(editeur);
        }

        // Badge non empruntable
        if (doc != null && Boolean.FALSE.equals(doc.getCodeEmpruntable())) {
            var nonEmp = new Span("⚠ Non empruntable");
            nonEmp.getStyle()
                .set("font-size", "0.75rem").set("font-weight", "600")
                .set("background", "rgba(239,68,68,0.85)")
                .set("padding", "0.2rem 0.7rem").set("border-radius", "999px")
                .set("display", "inline-block").set("margin-top", "0.3rem");
            hero.add(nonEmp);
        }

        return hero;
    }

    // ── CORPS ─────────────────────────────────────────────────────────────
    private Div buildBody(Livre livre, Document doc, Emprunter empruntActif) {
        var body = new Div();
        body.getStyle()
            .set("padding",    "2rem 2.5rem")
            .set("max-width",  "860px")
            .set("width",      "100%")
            .set("box-sizing", "border-box");

        // ── Section emprunt (dynamique selon contexte) ────────────────────
        body.add(buildEmpruntSection(doc, empruntActif));

        // ── Grille méta-infos ────────────────────────────────────────────
        var metaGrid = new Div();
        metaGrid.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "repeat(auto-fill, minmax(200px, 1fr))")
            .set("gap", "1rem")
            .set("margin-bottom", "2rem");

        if (livre.getDatePublication() != null)
            metaGrid.add(metaCard(VaadinIcon.CALENDAR, "Date de publication",
                    livre.getDatePublication().format(DATE_FMT), ACCENT));
        if (doc != null && doc.getDateAcquisition() != null)
            metaGrid.add(metaCard(VaadinIcon.PLUS_CIRCLE, "Acquis le",
                    doc.getDateAcquisition().format(DATE_FMT), ACCENT));
        if (livre.getNbPages() != null)
            metaGrid.add(metaCard(VaadinIcon.FILE_TEXT, "Pages",
                    livre.getNbPages() + " pages", ACCENT));
        if (doc != null && doc.getEtatDocument() != null && !doc.getEtatDocument().isBlank())
            metaGrid.add(metaCard(VaadinIcon.INFO_CIRCLE, "État",
                    doc.getEtatDocument(), ACCENT));
        if (doc != null && doc.getCodeEmplacement() != null && !doc.getCodeEmplacement().isBlank())
            metaGrid.add(metaCard(VaadinIcon.MAP_MARKER, "Emplacement",
                    doc.getCodeEmplacement(), ACCENT));
        if (livre.getIdEditeur() != null && livre.getIdEditeur().getSiteWeb() != null
                && !livre.getIdEditeur().getSiteWeb().isBlank())
            metaGrid.add(metaCard(VaadinIcon.GLOBE, "Site web éditeur",
                    livre.getIdEditeur().getSiteWeb(), ACCENT));

        body.add(metaGrid);

        // ── Description ──────────────────────────────────────────────────
        if (doc != null && doc.getDescriptionDocument() != null
                && !doc.getDescriptionDocument().isBlank()) {
            var descSection = new Div();
            descSection.getStyle()
                .set("background", "#F0FDF4").set("border", "1px solid #BBF7D0")
                .set("border-radius", "0.75rem").set("padding", "1.5rem")
                .set("margin-bottom", "2rem");
            var descTitle = new H2("Description");
            descTitle.getStyle().set("margin", "0 0 0.75rem 0").set("font-size", "1rem")
                .set("font-weight", "700").set("color", "#1E293B");
            var descText = new Paragraph(doc.getDescriptionDocument());
            descText.getStyle().set("margin", "0").set("line-height", "1.7").set("color", "#334155");
            descSection.add(descTitle, descText);
            body.add(descSection);
        }

        // ── Infos éditeur ────────────────────────────────────────────────
        if (livre.getIdEditeur() != null) {
            var ed = livre.getIdEditeur();
            var edSection = new Div();
            edSection.getStyle()
                .set("background", "#fff").set("border", "1px solid var(--border)")
                .set("border-radius", "0.75rem").set("padding", "1.5rem")
                .set("margin-bottom", "2rem");
            var edTitle = new H2("À propos de l'éditeur");
            edTitle.getStyle().set("margin", "0 0 0.75rem 0").set("font-size", "1rem")
                .set("font-weight", "700").set("color", "#1E293B");
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

        var btnBottom = new Button("← Retour à l'accueil");
        btnBottom.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnBottom.addClickListener(e -> UI.getCurrent().navigate(MainView.class));
        body.add(btnBottom);

        return body;
    }

    // ── SECTION EMPRUNT ───────────────────────────────────────────────────
    /**
     * Affiche selon le contexte :
     *  - Non connecté          → rien (ou message de connexion)
     *  - Bibliothécaire        → statut seulement
     *  - Emprunteur connecté :
     *      · Livre non empruntable → message
     *      · Disponible            → bouton Emprunter
     *      · Emprunté par lui      → info retour + bouton Prolonger (si pas déjà prolongé)
     *      · Emprunté par autre    → indisponible
     */
    private Div buildEmpruntSection(Document doc, Emprunter empruntActif) {
        var section = new Div();
        section.getStyle()
            .set("background",    "#fff")
            .set("border",        "1px solid var(--border)")
            .set("border-radius", "0.75rem")
            .set("padding",       "1.25rem 1.5rem")
            .set("margin-bottom", "2rem")
            .set("display",       "flex")
            .set("align-items",   "center")
            .set("gap",           "1rem")
            .set("flex-wrap",     "wrap");

        // Pas de document → impossible de gérer l'emprunt
        if (doc == null) {
            section.add(statusBadge("⚠ Information indisponible", "#92400E", "#FEF3C7"));
            return section;
        }

        // ── Non connecté ─────────────────────────────────────────────────
        if (!SessionManager.isLoggedIn()) {
            section.add(statusBadge("📚 Connectez-vous pour emprunter ce livre", "#1E3A8A", "#EFF6FF"));
            var btnLogin = new Button("Se connecter", new Icon(VaadinIcon.SIGN_IN));
            btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
            btnLogin.addClickListener(e -> UI.getCurrent().navigate("login"));
            section.add(btnLogin);
            return section;
        }

        // ── Bibliothécaire : affiche juste le statut ─────────────────────
        if (SessionManager.isBibliothecaire()) {
            if (empruntActif != null) {
                section.add(statusBadge("📕 Actuellement emprunté", "#B91C1C", "#FEE2E2"));
                if (empruntActif.getDateRetourPrevue() != null)
                    section.add(new Span("Retour prévu le " +
                            empruntActif.getDateRetourPrevue().format(DATE_FMT)));
            } else if (Boolean.FALSE.equals(doc.getCodeEmpruntable())) {
                section.add(statusBadge("🔒 Non empruntable", "#B91C1C", "#FEE2E2"));
            } else {
                section.add(statusBadge("✅ Disponible", "#166534", "#DCFCE7"));
            }
            return section;
        }

        // ── Emprunteur connecté ───────────────────────────────────────────
        Emprunteur moi = SessionManager.getEmprunteur();

        // Non empruntable
        if (Boolean.FALSE.equals(doc.getCodeEmpruntable())) {
            section.add(statusBadge("🔒 Ce document ne peut pas être emprunté", "#B91C1C", "#FEE2E2"));
            return section;
        }

        if (empruntActif == null) {
            // ── Disponible → bouton Emprunter ────────────────────────────
            section.add(statusBadge("✅ Disponible", "#166534", "#DCFCE7"));

            var btnEmprunter = new Button("Emprunter ce livre", new Icon(VaadinIcon.BOOK));
            btnEmprunter.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnEmprunter.getStyle().set("background", ACCENT);
            btnEmprunter.addClickListener(e -> emprunterLivre(doc, moi));
            section.add(btnEmprunter);

        } else if (empruntActif.getCarteEmprunteur().getCarteEmprunteur()
                .equals(moi.getCarteEmprunteur())) {
            // ── Emprunté par moi ─────────────────────────────────────────
            section.add(statusBadge("📗 Vous avez ce livre", "#166534", "#DCFCE7"));

            var infoRow = new VerticalLayout();
            infoRow.setPadding(false);
            infoRow.setSpacing(false);

            if (empruntActif.getDateEmprunt() != null) {
                var dateEmp = new Span("Emprunté le : " + empruntActif.getDateEmprunt().format(DATE_FMT));
                dateEmp.getStyle().set("font-size", "0.85rem").set("color", "var(--muted)");
                infoRow.add(dateEmp);
            }
            if (empruntActif.getDateRetourPrevue() != null) {
                boolean enRetard = empruntActif.getDateRetourPrevue().isBefore(LocalDate.now());
                String retourLabel = (enRetard ? "⚠ En retard — retour prévu le : " : "Retour prévu le : ")
                        + empruntActif.getDateRetourPrevue().format(DATE_FMT);
                var dateRetour = new Span(retourLabel);
                dateRetour.getStyle()
                    .set("font-size", "0.85rem")
                    .set("color", enRetard ? "#B91C1C" : "var(--muted)");
                infoRow.add(dateRetour);
            }
            section.add(infoRow);

            // Bouton prolonger (une seule fois : prolongationEmprunt doit être null ou 0)
            boolean dejaProlonge = empruntActif.getProlongationEmprunt() != null
                    && empruntActif.getProlongationEmprunt() > 0;

            if (!dejaProlonge) {
                var btnProlonger = new Button(
                        "Prolonger de " + PROLONGATION + " jours",
                        new Icon(VaadinIcon.CLOCK));
                btnProlonger.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                btnProlonger.addClickListener(e -> prolongerEmprunt(empruntActif));
                section.add(btnProlonger);
            } else {
                var dejaProlongeSpan = new Span("✓ Déjà prolongé");
                dejaProlongeSpan.getStyle()
                    .set("font-size", "0.78rem").set("color", "#64748B")
                    .set("font-style", "italic");
                section.add(dejaProlongeSpan);
            }

        } else {
            // ── Emprunté par quelqu'un d'autre ────────────────────────────
            section.add(statusBadge("📕 Indisponible — emprunté par un autre lecteur",
                    "#B91C1C", "#FEE2E2"));
            if (empruntActif.getDateRetourPrevue() != null)
                section.add(new Span("Retour prévu le " +
                        empruntActif.getDateRetourPrevue().format(DATE_FMT)));
        }

        return section;
    }

    // ── ACTIONS EMPRUNT ───────────────────────────────────────────────────

    private void emprunterLivre(Document doc, Emprunteur moi) {
        try {
            var emprunt = new Emprunter();
            emprunt.setIdDocument(doc);
            emprunt.setCarteEmprunteur(moi);
            emprunt.setDateEmprunt(LocalDate.now());
            emprunt.setDateRetourPrevue(LocalDate.now().plusDays(DUREE_PRET));
            emprunt.setProlongationEmprunt(0);
            emprunterService.saveEmprunt(emprunt);

            showSuccess("Livre emprunté ! Retour prévu le "
                    + LocalDate.now().plusDays(DUREE_PRET).format(DATE_FMT));
            // Recharger la page pour mettre à jour l'état
            UI.getCurrent().getPage().reload();
        } catch (Exception ex) {
            showError("Erreur lors de l'emprunt : " + ex.getMessage());
        }
    }

    private void prolongerEmprunt(Emprunter emprunt) {
        try {
            LocalDate nouvelleDateRetour = emprunt.getDateRetourPrevue() != null
                    ? emprunt.getDateRetourPrevue().plusDays(PROLONGATION)
                    : LocalDate.now().plusDays(PROLONGATION);

            emprunt.setDateRetourPrevue(nouvelleDateRetour);
            emprunt.setProlongationEmprunt(PROLONGATION);
            emprunterService.updateEmprunt(emprunt);

            showSuccess("Emprunt prolongé ! Nouveau retour prévu le "
                    + nouvelleDateRetour.format(DATE_FMT));
            UI.getCurrent().getPage().reload();
        } catch (Exception ex) {
            showError("Erreur lors de la prolongation : " + ex.getMessage());
        }
    }

    // ── HELPERS ───────────────────────────────────────────────────────────

    private Span statusBadge(String text, String color, String bg) {
        var s = new Span(text);
        s.getStyle()
            .set("font-size",     "0.82rem")
            .set("font-weight",   "600")
            .set("color",         color)
            .set("background",    bg)
            .set("padding",       "0.3rem 0.75rem")
            .set("border-radius", "999px");
        return s;
    }

    private Div metaCard(VaadinIcon icon, String label, String value, String color) {
        var card = new Div();
        card.getStyle()
            .set("background", "#fff").set("border", "1px solid var(--border)")
            .set("border-radius", "0.75rem").set("padding", "1rem 1.1rem")
            .set("display", "flex").set("gap", "0.75rem").set("align-items", "flex-start");
        var ico = new Icon(icon);
        ico.getStyle()
            .set("color", color).set("background", color + "18")
            .set("padding", "0.4rem").set("border-radius", "0.4rem")
            .set("width", "1.2rem").set("height", "1.2rem").set("flex-shrink", "0");
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

    private void showSuccess(String msg) {
        var n = Notification.show(msg, 4000, Notification.Position.BOTTOM_CENTER);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showError(String msg) {
        var n = Notification.show(msg, 4000, Notification.Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private Div notFound(String msg) {
        var d = new Div(new Span(msg));
        d.getStyle()
            .set("padding", "3rem").set("text-align", "center")
            .set("color", "var(--muted)").set("font-style", "italic");
        return d;
    }
}
