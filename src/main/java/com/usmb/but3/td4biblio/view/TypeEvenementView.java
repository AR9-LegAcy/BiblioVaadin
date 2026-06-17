package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.service.TypeEvenementService;
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
@Route("type-evenement")
@PageTitle("Types d'événements")
@Menu(title = "Types d'événements", order = 3, icon = "vaadin:calendar-briefcase")
public class TypeEvenementView extends VerticalLayout {
    private final TypeEvenementService typeEvenementService;
    final Grid<TypeEvenement> grid;
    final TextField filter;
    final Button addNewBtn;
    final TypeEvenementEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public TypeEvenementView(TypeEvenementService typeEvenementService, TypeEvenementEditor editor) {
        this.typeEvenementService = typeEvenementService;
        this.editor = editor;
        this.grid = new Grid<>(TypeEvenement.class);
        this.filter = new TextField();
        this.addNewBtn =
                new Button("Ajouter un type", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "nom");
        grid.getColumnByKey("id").setWidth("60px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par nom");

        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listTypeEvenements(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editTypeEvenement(e.getValue()));

        addNewBtn.addClickListener(e -> editor.editTypeEvenement(new TypeEvenement()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listTypeEvenements(filter.getValue());
        });

        listTypeEvenements(null);
    }

    void listTypeEvenements(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(typeEvenementService.getTypeEvenementsByNomLike("%" + filterText + "%"));
        } else {
            grid.setItems(typeEvenementService.getAllTypeEvenements());
        }
    }
}