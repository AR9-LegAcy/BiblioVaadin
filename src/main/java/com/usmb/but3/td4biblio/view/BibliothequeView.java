package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.service.BibliothequeService;
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
@Route(value = "bibliotheque")
@PageTitle("Les Bibliothèques")
@Menu(title = "Les Bibliothèques", order = 1, icon = "vaadin:building")
public class BibliothequeView extends VerticalLayout {

    private final BibliothequeService bibliothequeService;
    final Grid<Bibliotheque> grid;
    final TextField filter;
    final Button addNewBtn;
    final BibliothequeEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public BibliothequeView(BibliothequeService bibliothequeService, BibliothequeEditor editor) {
        this.bibliothequeService = bibliothequeService;
        this.editor = editor;
        this.grid = new Grid<>(Bibliotheque.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter une bibliothèque", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "nom", "adresseRue", "adresseVille", "codePostal", "horaires");
        grid.getColumnByKey("id").setWidth("60px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par nom");

        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listBibliotheques(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editBibliotheque(e.getValue()));

        addNewBtn.addClickListener(e -> editor.editBibliotheque(new Bibliotheque()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listBibliotheques(filter.getValue());
        });

        listBibliotheques(null);
    }

    void listBibliotheques(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(bibliothequeService.getBibliothequesByNom(filterText));
        } else {
            grid.setItems(bibliothequeService.getAllBibliotheques());
        }
    }
}