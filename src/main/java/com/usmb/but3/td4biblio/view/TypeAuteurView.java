package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.service.TypeAuteurService;
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

@Component
@Scope("prototype")
@Route("typeAuteur")
@PageTitle("Types d'auteurs")
@Menu(
    title = "Types d'auteurs",
    order = 6,
    icon = "vaadin:user-card"
)
public class TypeAuteurView extends VerticalLayout {

    private final TypeAuteurService typeAuteurService;
    final Grid<TypeAuteur> grid;
    final TextField filter;
    final Button addNewBtn;
    final TypeAuteurEditor editor;
    public TypeAuteurView(
            TypeAuteurService typeAuteurService,
            TypeAuteurEditor editor) {

        this.typeAuteurService = typeAuteurService;
        this.editor = editor;

        grid = new Grid<>(TypeAuteur.class);

        filter = new TextField();

        addNewBtn =
                new Button("Ajouter", VaadinIcon.PLUS.create());

        HorizontalLayout actions =
                new HorizontalLayout(filter, addNewBtn);

        add(actions, grid, editor);

        grid.setColumns("id", "nom");

        grid.getColumnByKey("id")
                .setWidth("70px")
                .setFlexGrow(0);

        filter.setPlaceholder("Filtrer par nom");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listTypeAuteurs(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editTypeAuteur(e.getValue()));
        addNewBtn.addClickListener(e -> editor.editTypeAuteur(new TypeAuteur()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listTypeAuteurs(filter.getValue());
        });

        listTypeAuteurs(null);
    }

    void listTypeAuteurs(String filterText) {

        if (StringUtils.hasText(filterText)) {
            grid.setItems(typeAuteurService.getTypeAuteursByNomLike("%" + filterText + "%"));
        } else {
            grid.setItems(typeAuteurService.getAllTypeAuteurs());
        }
    }
}