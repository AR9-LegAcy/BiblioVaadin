package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EmprunterService;
import com.usmb.but3.td4biblio.service.EvenementService;
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
    private static final int PROLONGATION_JOURS = 7;

    MainView(EvenementService evenementService,
             LivreService livreService,
             DocumentService documentService,
             EmprunterService emprunterService) {
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

        // ── Section "Mes emprunts" uniquement pour un emprunteur connecté ──
        if (SessionManager.isEmprunteur()) {
            add(buildMesEmpruntsRow(emprunterService, livreService, documentService));
        }

        add(buildEvenementsRow(evenementService));
        add(buildAcquisitionsRow(livreService, documentService));
    }

    // ── HERO ──────────────────────────────────────────────────────────────

    private Section buildHero() {
        var section = new Section();
        section.getStyle()
            .set("background",    "linear-gradient(135deg,#1E3A8A 0%,#2563EB 60%,#3B82F6 100%)")
            .set("color",         "#fff")
            .set("padding",       "3rem 2.5rem 2.5rem")
            .set("border-radius", "0 0 1.5rem 1.5rem")
            .set("margin-bottom", "2rem")
            .set("position",      "relative");

        var eyebrow = new Span("Réseau des Bibliothèques");
        eyebrow.getStyle()
            .set("font-size", "0.75rem").set("font-weight", "600")
            .set("letter-spacing", "0.12em").set("text-transform", "uppercase")
            .set("opacity", "0.75").set("display", "block").set("margin-bottom", "0.5rem");

        String titleText = "Bienvenue à la bibliothèque";
        if (SessionManager.isLoggedIn()) {
            titleText = "Bienvenue, " + SessionManager.getDisplayName();
        }
        var title = new H1(titleText);
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

        var btnRow = new HorizontalLayout();
        btnRow.setSpacing(true);
        btnRow.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        btnRow.getStyle().set("gap", "0.75rem").set("flex-wrap", "wrap");

        var btnRecherche = new Button("Rechercher un livre", new Icon(VaadinIcon.SEARCH));
        btnRecherche.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        btnRecherche.getStyle()
            .set("background", "#fff").set("color", "#1E3A8A")
            .set("font-weight", "600").set("border-radius", "0.5rem");
        btnRecherche.addClickListener(e -> UI.getCurrent().navigate("livre"));

        if (SessionManager.isLoggedIn()) {
            var btnLogout = new Button("Se déconnecter", new Icon(VaadinIcon.SIGN_OUT));
            btnLogout.addThemeVariants(ButtonVariant.LUMO_LARGE);
            btnLogout.getStyle()
                .set("background", "rgba(255,255,255,0.15)").set("color", "#fff")
                .set("border", "2px solid rgba(255,255,255,0.6)")
                .set("font-weight", "600").set("border-radius", "0.5rem");
            btnLogout.addClickListener(e -> {
                SessionManager.logout();
                UI.getCurrent().navigate(MainView.class);
                UI.getCurrent().getPage().reload();
            });
            btnRow.add(btnRecherche, btnLogout);
        } else {
            var btnLogin = new Button("Se connecter", new Icon(VaadinIcon.SIGN_IN));
            btnLogin.addThemeVariants(ButtonVariant.LUMO_LARGE);
            btnLogin.getStyle()
                .set("background", "rgba(255,255,255,0.15)").set("color", "#fff")
                .set("border", "2px solid rgba(255,255,255,0.6)")
                .set("font-weight", "600").set("border-radius", "0.5rem");
            btnLogin.addClickListener(e -> UI.getCurrent().navigate("login"));
            btnRow.add(btnRecherche, btnLogin);
        }

        section.add(eyebrow, title, sub, btnRow);
        return section;
    }

    // ── MES EMPRUNTS ──────────────────────────────────────────────────────

    private VerticalLayout buildMesEmpruntsRow(EmprunterService emprunterService,
                                               LivreService livreService,
                                               DocumentService documentService) {
        var wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        wrapper.setWidthFull();
        wrapper.getStyle().set("padding", "0 1rem 1.5rem");

        var section = sectionCard();
        section.add(buildSectionHeader(VaadinIcon.PACKAGE, "Mes emprunts en cours", "#7C3AED", null));

        List<Emprunter> emprunts = List.of();
        try {
            int carte = SessionManager.getEmprunteur().getCarteEmprunteur();
            emprunts = emprunterService.getEmpruntsByCarteEmprunteur(carte)
                    .stream()
                    .filter(e -> e.getDateRetourReelle() == null) // actifs uniquement
                    .toList();
        } catch (Exception ignored) {}

        if (emprunts.isEmpty()) {
            section.add(emptyState("Vous n'avez aucun emprunt en cours."));
        } else {
            var grid = new Div();
            grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fill, minmax(min(100%, 280px), 1fr))")
                .set("width", "100%");

            for (Emprunter emprunt : emprunts) {
                grid.add(buildEmpruntCard(emprunt, emprunterService, livreService, documentService));
            }
            section.add(grid);
        }

        wrapper.add(section);
        return wrapper;
    }

    private Div buildEmpruntCard(Emprunter emprunt,
                                  EmprunterService emprunterService,
                                  LivreService livreService,
                                  DocumentService documentService) {
        var card = new Div();
        card.getStyle()
            .set("padding",      "1rem 1.1rem")
            .set("border-right", "1px solid var(--border)")
            .set("border-bottom","1px solid var(--border)")
            .set("display",      "flex")
            .set("flex-direction","column")
            .set("gap",          "0.5rem");

        // Retrouver le livre à partir du document
        Document doc = emprunt.getIdDocument();
        Livre livre = null;
        if (doc != null) {
            livre = livreService.getLivreById(doc.getIdDocument());
        }

        // ── En-tête : icône + titre cliquable ────────────────────────────
        var header = new Div();
        header.getStyle()
            .set("display", "flex").set("gap", "0.6rem").set("align-items", "flex-start")
            .set("cursor", "pointer");

        var ico = new Icon(VaadinIcon.BOOK);
        ico.getStyle()
            .set("color", "#7C3AED").set("background", "#7C3AED18")
            .set("padding", "0.4rem").set("border-radius", "0.4rem")
            .set("min-width", "2rem").set("height", "2rem").set("flex-shrink", "0");

        String titreLivre = livre != null && livre.getTitreLivre() != null
                ? livre.getTitreLivre()
                : (doc != null ? "Document #" + doc.getIdDocument() : "Livre inconnu");

        var titreEl = new H3(titreLivre);
        titreEl.getStyle()
            .set("margin", "0").set("font-size", "0.93rem").set("font-weight", "600")
            .set("color", "#1E293B");

        header.add(ico, titreEl);

        // Clic → page détail du livre
        final Livre livreFinal = livre;
        header.addClickListener(e -> {
            if (livreFinal != null)
                UI.getCurrent().navigate("livre/detail/" + livreFinal.getIdDocument());
        });
        card.add(header);

        // ── Dates ────────────────────────────────────────────────────────
        var datesDiv = new Div();
        datesDiv.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "0.2rem");

        if (emprunt.getDateEmprunt() != null) {
            var de = new Span("📅 Emprunté le : " + emprunt.getDateEmprunt().format(DATE_FMT));
            de.getStyle().set("font-size", "0.8rem").set("color", "var(--muted)");
            datesDiv.add(de);
        }

        if (emprunt.getDateRetourPrevue() != null) {
            boolean enRetard = emprunt.getDateRetourPrevue().isBefore(LocalDate.now());
            String retourTxt = (enRetard ? "⚠ En retard — retour prévu le : " : "🔄 Retour prévu le : ")
                    + emprunt.getDateRetourPrevue().format(DATE_FMT);
            var dr = new Span(retourTxt);
            dr.getStyle()
                .set("font-size", "0.8rem")
                .set("font-weight", enRetard ? "600" : "400")
                .set("color", enRetard ? "#B91C1C" : "var(--muted)");
            datesDiv.add(dr);
        }
        card.add(datesDiv);

        // ── Prolongation ─────────────────────────────────────────────────
        boolean dejaProlonge = emprunt.getProlongationEmprunt() != null
                && emprunt.getProlongationEmprunt() > 0;

        if (dejaProlonge) {
            var deja = new Span("✓ Déjà prolongé de " + emprunt.getProlongationEmprunt() + " j");
            deja.getStyle()
                .set("font-size", "0.75rem").set("color", "#64748B")
                .set("font-style", "italic");
            card.add(deja);
        } else {
            var btnProlonger = new Button(
                    "Prolonger de " + PROLONGATION_JOURS + " j",
                    new Icon(VaadinIcon.CLOCK));
            btnProlonger.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btnProlonger.getStyle()
                .set("background", "#7C3AED").set("color", "#fff")
                .set("font-size", "0.78rem").set("border-radius", "0.4rem");
            btnProlonger.addClickListener(e -> {
                try {
                    LocalDate nouvelleDateRetour = emprunt.getDateRetourPrevue() != null
                            ? emprunt.getDateRetourPrevue().plusDays(PROLONGATION_JOURS)
                            : LocalDate.now().plusDays(PROLONGATION_JOURS);
                    emprunt.setDateRetourPrevue(nouvelleDateRetour);
                    emprunt.setProlongationEmprunt(PROLONGATION_JOURS);
                    emprunterService.updateEmprunt(emprunt);
                    var n = Notification.show(
                            "Emprunt prolongé ! Retour prévu le "
                            + nouvelleDateRetour.format(DATE_FMT),
                            3500, Notification.Position.BOTTOM_CENTER);
                    n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    UI.getCurrent().getPage().reload();
                } catch (Exception ex) {
                    var n = Notification.show("Erreur : " + ex.getMessage(),
                            3000, Notification.Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
            card.add(btnProlonger);
        }

        return card;
    }

    // ── RANGÉE ÉVÉNEMENTS ────────────────────────────────────────────────

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
                    .filter(ev -> !ev.getDateDebut().isAfter(today) && !ev.getDateFin().isBefore(today))
                    .toList();
            final List<Evenement> enCoursRef = enCours;
            futurs = evenementService.getEvenementsFuturs().stream()
                    .filter(ev -> !enCoursRef.contains(ev)).toList();
        } catch (Exception ignored) {}

        var secEnCours = sectionCard();
        secEnCours.add(buildSectionHeader(VaadinIcon.CLOCK, "Événements en cours", "#16A34A", "evenement"));
        if (enCours.isEmpty()) {
            secEnCours.add(emptyState("Aucun événement en cours."));
        } else {
            var grid = eventGrid();
            enCours.forEach(ev -> grid.add(buildEventCard(ev, true)));
            secEnCours.add(grid);
        }

        var secFuturs = sectionCard();
        secFuturs.add(buildSectionHeader(VaadinIcon.CALENDAR, "Événements à venir", "#2563EB", "evenement"));
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

    private Div eventGrid() {
        var grid = new Div();
        grid.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "repeat(auto-fill, minmax(min(100%, 260px), 1fr))")
            .set("width", "100%");
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

        Span badge;
        if (isEnCours) {
            badge = new Span("● En cours");
            badge.getStyle()
                .set("font-size", "0.7rem").set("font-weight", "600")
                .set("color", "#fff").set("background", "#16A34A")
                .set("padding", "0.15rem 0.5rem").set("border-radius", "999px")
                .set("display", "inline-block").set("margin-bottom", "0.35rem");
        } else {
            String dateLabel = ev.getDateDebut() != null ? ev.getDateDebut().format(DATE_FMT) : "Date inconnue";
            if (ev.getDateFin() != null && ev.getDateDebut() != null && !ev.getDateFin().equals(ev.getDateDebut()))
                dateLabel += " → " + ev.getDateFin().format(DATE_FMT);
            badge = new Span(dateLabel);
            badge.getStyle()
                .set("font-size", "0.7rem").set("font-weight", "600")
                .set("color", "#2563EB").set("background", "#EFF6FF")
                .set("padding", "0.15rem 0.5rem").set("border-radius", "999px")
                .set("display", "inline-block").set("margin-bottom", "0.35rem");
        }

        var titre = new H3(ev.getTitre() != null ? ev.getTitre() : "Événement sans titre");
        titre.getStyle().set("margin", "0 0 0.25rem 0").set("font-size", "0.95rem").set("font-weight", "600");
        card.add(badge, titre);

        if (ev.getTypeEvenement() != null && ev.getTypeEvenement().getNom() != null) {
            var typeSpan = new Span(ev.getTypeEvenement().getNom());
            typeSpan.getStyle()
                .set("font-size", "0.72rem").set("color", "#fff").set("background", "#6366F1")
                .set("padding", "0.1rem 0.45rem").set("border-radius", "999px")
                .set("display", "inline-block").set("margin-bottom", "0.3rem");
            card.add(typeSpan);
        }
        if (ev.getBibliotheque() != null && ev.getBibliotheque().getNom() != null) {
            var lieuRow = new Div();
            var ico = new Icon(VaadinIcon.MAP_MARKER);
            ico.getStyle().set("width", "0.9rem").set("height", "0.9rem").set("color", "var(--muted)");
            var lieuText = new Span(ev.getBibliotheque().getNom());
            lieuText.getStyle().set("font-size", "0.8rem").set("color", "var(--muted)");
            lieuRow.add(ico, lieuText);
            lieuRow.getStyle().set("display", "flex").set("align-items", "center")
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

    // ── RANGÉE ACQUISITIONS ──────────────────────────────────────────────

    private VerticalLayout buildAcquisitionsRow(LivreService livreService,
                                                DocumentService documentService) {
        var wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        wrapper.setWidthFull();
        wrapper.getStyle().set("padding", "0 1rem 2rem");

        var section = sectionCard();
        section.setWidthFull();
        section.getStyle().set("box-sizing", "border-box");
        section.add(buildSectionHeader(VaadinIcon.BOOK, "Nouvelles acquisitions", "#0F766E", "livre"));

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
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fill, minmax(220px, 1fr))")
                .set("gap", "0").set("width", "100%");
            livres.stream().limit(6).forEach(l -> grid.add(buildLivreCard(l, documentService)));
            section.add(grid);
        }

        wrapper.add(section);
        return wrapper;
    }

    private Div buildLivreCard(Livre livre, DocumentService documentService) {
        var card = new Div();
        card.getStyle()
            .set("padding",       "0.9rem 1.1rem")
            .set("border-right",  "1px solid var(--border)")
            .set("border-bottom", "1px solid var(--border)")
            .set("display",       "flex").set("gap", "0.75rem")
            .set("align-items",   "flex-start")
            .set("cursor",        "pointer").set("transition", "background 0.15s");
        card.getElement().addEventListener("mouseover",
            e -> card.getStyle().set("background", "#F0FDF4"));
        card.getElement().addEventListener("mouseout",
            e -> card.getStyle().set("background", "transparent"));
        card.addClickListener(e -> UI.getCurrent().navigate("livre/detail/" + livre.getIdDocument()));

        Document doc = null;
        if (livre.getDocument() != null && livre.getDocument().getCodeIsbn() != null) {
            List<Document> docs = documentService.getDocumentsByCodeIsbn(
                    livre.getDocument().getCodeIsbn());
            if (!docs.isEmpty()) doc = docs.get(0);
        }

        var ico = new Icon(VaadinIcon.BOOK);
        ico.getStyle()
            .set("color", "#0F766E").set("background", "#0F766E18")
            .set("padding", "0.45rem").set("border-radius", "0.4rem")
            .set("min-width", "2rem").set("height", "2rem").set("flex-shrink", "0");

        var info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        var titre = new H3(livre.getTitreLivre() != null ? livre.getTitreLivre() : "Sans titre");
        titre.getStyle().set("margin", "0 0 0.15rem 0").set("font-size", "0.93rem").set("font-weight", "600");

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
        if (doc != null) {
            if (Boolean.FALSE.equals(doc.getCodeEmpruntable())) {
                var nonEmp = new Span("Non empruntable");
                nonEmp.getStyle()
                    .set("font-size", "0.7rem").set("color", "#B91C1C")
                    .set("background", "#FEE2E2").set("padding", "0.1rem 0.35rem")
                    .set("border-radius", "4px");
                meta.add(nonEmp);
            }
            if (doc.getEtatDocument() != null && !doc.getEtatDocument().isBlank()) {
                var etat = new Span(doc.getEtatDocument());
                etat.getStyle()
                    .set("font-size", "0.7rem").set("color", "#92400E")
                    .set("background", "#FEF3C7").set("padding", "0.1rem 0.35rem")
                    .set("border-radius", "4px");
                meta.add(etat);
            }
        }

        info.add(titre, meta);
        card.add(ico, info);
        return card;
    }

    // ── HELPERS ───────────────────────────────────────────────────────────

    private VerticalLayout sectionCard() {
        var v = new VerticalLayout();
        v.setPadding(false);
        v.setSpacing(false);
        v.setWidthFull();
        v.getStyle()
            .set("background",    "var(--surface)")
            .set("border",        "1px solid var(--border)")
            .set("border-radius", "1rem")
            .set("overflow",      "hidden");
        return v;
    }

    /**
     * @param navTarget null → pas de bouton "Voir tout"
     */
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

        header.add(left);

        if (navTarget != null) {
            var btn = new Button("Voir tout");
            btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            btn.getStyle().set("color", color).set("font-size", "0.78rem");
            btn.addClickListener(e -> UI.getCurrent().navigate(navTarget));
            header.add(btn);
        }

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