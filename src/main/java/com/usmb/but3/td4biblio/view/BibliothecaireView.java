package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.service.BibliothecaireService;
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
@Route(value = "bibliothecaire")
@PageTitle("Les Bibliothecaires")
@Menu(title = "Les Bibliothecaires", order = 0, icon = "vaadin:clipboard-check")
public class BibliothecaireView extends VerticalLayout {

    private final BibliothecaireService bibliothecaireService;
    final Grid<Bibliothecaire> grid;
    final TextField filter;
    private final Button addNewBtn;
    final BibliothecaireEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public BibliothecaireView(BibliothecaireService bibliothecaireService, BibliothecaireEditor editor) {
        this.bibliothecaireService = bibliothecaireService;
        this.editor = editor;
        this.grid = new Grid<>(Bibliothecaire.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un bibliothecaire", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("pseudo", "id", "nom", "prenom", "adresseRue", "adresseVille", "adresseCP", "email",
                "dateNaissance");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par nom ou prénom");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listBibliothecaires(e.getValue()));

        // Connect selected Bibliothecaire to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editBibliothecaire(e.getValue());
        });

        // Instantiate and edit new Bibliothecaire when the new button is clicked
        addNewBtn.addClickListener(e -> editor
                .editBibliothecaire(new Bibliothecaire()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listBibliothecaires(filter.getValue());
        });

        // Initialize listing
        listBibliothecaires(null);
    }

    void listBibliothecaires(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(bibliothecaireService.getByNomContainingIgnoreCase(filterText));
        } else {
            grid.setItems(bibliothecaireService.getAllBibliothecaires());
        }
    }
}