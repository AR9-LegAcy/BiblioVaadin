package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.service.EmprunteurService;
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
@Route(value = "emprunteur")
@PageTitle("Emprunteurs")
@Menu(title = "Emprunteurs", order = 1, icon = "vaadin:male")
public class EmprunteurView extends VerticalLayout {

    private final EmprunteurService emprunteurService;
    final Grid<Emprunteur> grid;
    final TextField filter;
    private final Button addNewBtn;
    final EmprunteurEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public EmprunteurView(EmprunteurService emprunteurService, EmprunteurEditor editor) {
        this.emprunteurService = emprunteurService;
        this.editor = editor;
        this.grid = new Grid<>(Emprunteur.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un emprunteur", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns(
            "carteEmprunteur","nom","prenom","email","adresseRue","adresseVille","adresseCodePostal","dateNaissance","debutAbonnement","expirationAbonnement"
        );        
        grid.getColumnByKey("carteEmprunteur").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par nom ou prénom");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listEmprunteurs(e.getValue()));

        // Connect selected Emprunteur to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editEmprunteur(e.getValue());
        });

        // Initialize listing
        listEmprunteurs(null);
    }

    void listEmprunteurs(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(emprunteurService.getAllEmprunteurs());
        }
        else {
            grid.setItems(emprunteurService.getAllEmprunteurs());
        }
    }
}
