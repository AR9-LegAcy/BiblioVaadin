package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Livre;
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
    final Grid<Livre> grid;
    final TextField filter;
    private final Button addNewBtn;
    final LivreEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public LivreView(LivreService livreService, LivreEditor editor) {
        this.livreService = livreService;
        this.editor = editor;
        this.grid = new Grid<>(Livre.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un livre", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("idDocument", "titreLivre", "codeIsbn", "nbPages", "datePublication", "dateAcquisition", "codeEmplacement");
        grid.getColumnByKey("idDocument").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par titre");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listLivres(e.getValue()));

        // Connect selected Livre to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editLivre(e.getValue());
        });

        // Instantiate and edit new Livre when the new button is clicked
        addNewBtn.addClickListener(e -> editor.editLivre(new Livre(null, null, null, null, "", "", null, null, "", "", null, "", "", false, "", null, null)));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listLivres(filter.getValue());
        });

        // Initialize listing
        listLivres(null);
    }

    void listLivres(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(livreService.getByTitreContainingIgnoreCase(filterText));
        } else {
            grid.setItems(livreService.getAllLivres());
        }
    }
}