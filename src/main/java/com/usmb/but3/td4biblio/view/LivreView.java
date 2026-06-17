package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.usmb.but3.td4biblio.service.LivreService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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
@PageTitle("Les Livres")
@Menu(title = "Les Livres", order = 1, icon = "vaadin:book")
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

        // Passe les services à l'éditeur
        this.editor = new LivreEditor(livreService, editeurService);

        this.grid = new Grid<>(Livre.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un livre", VaadinIcon.PLUS.create());
        addNewBtn.getStyle().set("visibility", "hidden");
        
        // Construction de la mise en page
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);
        
        grid.setHeight("300px");
        grid.setColumns("idDocument", "titreLivre", "nbPages", "datePublication");
        grid.addColumn(livre -> livre.getIdEditeur() != null ? livre.getIdEditeur().getNom() : "Aucun")
        .setHeader("Éditeur");
        
        grid.getColumnByKey("idDocument").setWidth("50px").setFlexGrow(0);
        
        filter.setPlaceholder("Filtrer par titre");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listLivres(e.getValue()));
        
        if (isBib) {
            addNewBtn.getStyle().set("visibility", "visible");
            grid.asSingleSelect().addValueChangeListener(e -> editor.editLivre(e.getValue()));
            
            addNewBtn.addClickListener(e -> editor.editLivre(new Livre()));

            editor.setChangeHandler(() -> {
                editor.setVisible(false);
                listLivres(filter.getValue());
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