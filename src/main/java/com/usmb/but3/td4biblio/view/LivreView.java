package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.usmb.but3.td4biblio.service.LivreService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope("prototype")
@Route(value = "livre")
@PageTitle("Livres")
@Menu(title = "Livres", order = 1, icon = "vaadin:book")
public class LivreView extends VerticalLayout {

    private final LivreService livreService;
    private final EditeurService editeurService;

    final Grid<Livre> grid;
    final TextField filter;
    private final Button addNewBtn;
    final LivreEditor editor;
    boolean isBib = SessionManager.isBibliothecaire();

    public LivreView(LivreService livreService, EditeurService editeurService) {
        this.livreService = livreService;
        this.editeurService = editeurService;

        this.editor   = new LivreEditor(livreService, editeurService);
        this.grid     = new Grid<>(Livre.class);
        this.filter   = new TextField();
        this.addNewBtn = new Button("Ajouter un livre", VaadinIcon.PLUS.create());
        addNewBtn.getStyle().set("visibility", "hidden");

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("titreLivre", "nbPages", "datePublication");
        grid.addColumn(livre -> livre.getIdEditeur() != null
                ? livre.getIdEditeur().getNom() : "Aucun")
            .setHeader("Éditeur");

        filter.setPlaceholder("Filtrer par titre");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listLivres(e.getValue()));

        if (isBib) {
            // ── Bibliothécaire : comportement d'édition inchangé ─────────
            addNewBtn.getStyle().set("visibility", "visible");
            grid.asSingleSelect().addValueChangeListener(
                    e -> editor.editLivre(e.getValue()));
            addNewBtn.addClickListener(e -> editor.editLivre(new Livre()));
            editor.setChangeHandler(() -> {
                editor.setVisible(false);
                listLivres(filter.getValue());
            });
            editor.setCancelHandler(() -> grid.deselectAll());

        } else {
            // ── Emprunteur / non connecté : clic → page détail ───────────
            grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
            // Curseur pointeur pour indiquer que la ligne est cliquable
            grid.getStyle().set("cursor", "pointer");

            grid.addItemClickListener(event -> {
                Livre livre = event.getItem();
                if (livre != null) {
                    UI.getCurrent().navigate("livre/detail/" + livre.getIdDocument());
                }
            });
        }

        listLivres(null);
    }

    void listLivres(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(livreService.getByTitreContainingIgnoreCase(filterText));
        } else {
            grid.setItems(livreService.getAllLivres());
        }
    }

    public Button getAddNewBtn() {
        return addNewBtn;
    }
}