package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.usmb.but3.td4biblio.entity.Classer;
import com.usmb.but3.td4biblio.service.ClasserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Component
@Scope("prototype")
@Route("classer")
@PageTitle("Classement auteurs")
@Menu(
    title = "Classement auteurs",
    order = 7,
    icon = "vaadin:tags"
)
public class ClasserView extends VerticalLayout {

    private final ClasserService classerService;

    final Grid<Classer> grid;

    final Button addNewBtn;

    final ClasserEditor editor;

    public ClasserView(
            ClasserService classerService,
            ClasserEditor editor) {

        this.classerService = classerService;
        this.editor = editor;

        grid = new Grid<>();

        addNewBtn =
                new Button("Ajouter",
                        VaadinIcon.PLUS.create());

        add(
                new HorizontalLayout(addNewBtn),
                grid,
                editor);

        grid.addColumn(
                c -> c.getIdAuteur().getPrenom()
                        + " "
                        + c.getIdAuteur().getNom())
                .setHeader("Auteur");

        grid.addColumn(
                c -> c.getIdTypeAuteur().getNom())
                .setHeader("Type d'auteur");

        grid.asSingleSelect()
                .addValueChangeListener(
                        e -> editor.editClasser(
                                e.getValue()));

        addNewBtn.addClickListener(
                e -> editor.editClasser(
                        new Classer()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listClassements();
        });

        listClassements();
    }

    void listClassements() {
        grid.setItems(
                classerService.getAllClassements());
    }
}