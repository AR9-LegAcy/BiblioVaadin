package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.usmb.but3.td4biblio.entity.Ecrire;
import com.usmb.but3.td4biblio.service.EcrireService;
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
@Route("ecrire")
@PageTitle("Ecrire auteurs")
@Menu(
    title = "Ecrire auteurs",
    order = 7,
    icon = "vaadin:tags"
)
public class EcrireView extends VerticalLayout {

    private final EcrireService ecrireService;

    final Grid<Ecrire> grid;

    final Button addNewBtn;

    final EcrireEditor editor;

    public EcrireView(EcrireService ecrireService, EcrireEditor editor) {
        this.ecrireService = ecrireService;
        this.editor = editor;

        grid = new Grid<>();

        addNewBtn = new Button("Ajouter", VaadinIcon.PLUS.create());

        add(new HorizontalLayout(addNewBtn), grid, editor);

        grid.addColumn(c -> c.getIdAuteur().getPrenom() + " " + c.getIdAuteur().getNom()).setHeader("Auteur");

        grid.addColumn(c -> c.getIdDocument().getIdgetTitreLivre()).setHeader("livre");

        grid.asSingleSelect().addValueChangeListener(e -> editor.editEcrire(e.getValue()));

        addNewBtn.addClickListener(e -> editor.editEcrire(new Ecrire()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listEcrires();
        });

        listEcrires();
    }

    void listEcrires() {
        grid.setItems(ecrireService.getAllEcrires());
    }
}