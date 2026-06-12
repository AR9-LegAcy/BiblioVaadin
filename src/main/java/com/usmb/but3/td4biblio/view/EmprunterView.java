package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.service.EmprunterService;
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
@Route(value = "emprunter")
@PageTitle("Les Emprunts")
@Menu(title = "Les Emprunts", order = 1, icon = "vaadin:clipboard-check")
public class EmprunterView extends VerticalLayout {
    private final EmprunterService emprunterService;
    final Grid<Emprunter> grid;
    final TextField filter;
    private final Button addNewBtn;
    final EmprunterEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public EmprunterView(EmprunterService emprunterService, EmprunterEditor editor) {
        this.emprunterService = emprunterService;
        this.editor = editor;
        this.grid = new Grid<>(Emprunter.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un emprunt", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("idDocument", "carteEmprunteur", "dateEmprunt", "dateRetourPrevue", "dateRetourReelle", "prolongationEmprunt");
        grid.getColumnByKey("idDocument").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par nom ou prénom");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listEmprunts(e.getValue()));

        // Connect selected Auteur to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editEmprunter(e.getValue());
        });

        // Instantiate and edit new Auteur when the new button is clicked
        addNewBtn.addClickListener(e -> editor.editEmprunter(new Emprunter()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listEmprunts(filter.getValue());
        });

        // Initialize listing
        listEmprunts(null);
    }

    void listEmprunts(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(emprunterService.getEmpruntsByCarteEmprunteur(Integer.parseInt(filterText)));
        } else {
            grid.setItems(emprunterService.getAllEmprunts());
        }
    }
}
