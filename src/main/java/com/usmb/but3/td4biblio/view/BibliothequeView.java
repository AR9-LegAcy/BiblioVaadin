package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.security.SessionManager;
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
@PageTitle("Bibliotheques")
@Menu(title = "Bibliotheques", order = 0, icon = "vaadin:building")
public class BibliothequeView extends VerticalLayout {

    private final BibliothequeService bibliothequeService;
    final Grid<Bibliotheque> grid;
    final TextField filter;
    private final Button addNewBtn;
    final BibliothequeEditor editor;
    boolean isBib = SessionManager.isBibliothecaire();

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public BibliothequeView(BibliothequeService bibliothequeService, BibliothequeEditor editor) {
        this.bibliothequeService = bibliothequeService;
        this.editor = editor;
        this.grid = new Grid<>(Bibliotheque.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter une bibliotheque", VaadinIcon.PLUS.create());
        addNewBtn.getStyle().set("visibility", "hidden");

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "nom", "adresseRue", "adresseVille", "adresseCP", "horaires");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par nom");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listBibliotheques(e.getValue()));

        if (isBib) {
            addNewBtn.getStyle().set("visibility", "visible");
            // Connect selected Livre to editor or hide if none is selected
            grid.asSingleSelect().addValueChangeListener(e -> {
                editor.editBibliotheque(e.getValue());
            });

            // Instantiate and edit new Bibliotheque when the new button is clicked
            addNewBtn.addClickListener(e -> editor.editBibliotheque(new Bibliotheque()));

            // Listen changes made by the editor, refresh data from backend
            editor.setChangeHandler(() -> {
                editor.setVisible(false);
                listBibliotheques(filter.getValue());
            });
        }
        // Initialize listing
        listBibliotheques(null);
    }

    void listBibliotheques(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(bibliothequeService.getByNomContainingIgnoreCase(filterText));
        } else {
            grid.setItems(bibliothequeService.getAllBibliotheques());
        }
    }
}