package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;
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
@Route(value = "auteur")
@PageTitle("Auteurs")
@Menu(title = "Auteurs", order = 0, icon = "vaadin:male")
public class AuteurView extends VerticalLayout {

    private final AuteurService auteurService;
    final Grid<Auteur> grid;
    final TextField filter;
    private final Button addNewBtn;
    final AuteurEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public AuteurView(AuteurService auteurService, AuteurEditor editor) {
        this.auteurService = auteurService;
        this.editor = editor;
        this.grid = new Grid<>(Auteur.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un auteur", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns( "nom", "prenom", "nationalite", "paysAuteur", "dateNaissance", "dateDeces");

        filter.setPlaceholder("Filtrer par nom ou prénom");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listAuteurs(e.getValue()));

        // Connect selected Auteur to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editAuteur(e.getValue());
        });

        // Instantiate and edit new Auteur when the new button is clicked
        addNewBtn.addClickListener(e -> editor.editAuteur(new Auteur(null, "", "", null, null, "", "", "", "", null, null)));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listAuteurs(filter.getValue());
        });

        editor.setCancelHandler(() -> {
            grid.deselectAll();
        });

        // Initialize listing
        listAuteurs(null);
    }

    void listAuteurs(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(auteurService.getByNomContainingIgnoreCase(filterText));
        } else {
            grid.setItems(auteurService.getAllAuteurs());
        }
    }
}