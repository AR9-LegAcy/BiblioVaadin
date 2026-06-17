package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.EvenementService;
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
@Route("evenement")
@PageTitle("Evénements")
@Menu(title = "Evénements", order = 4, icon = "vaadin:calendar")
public class EvenementView extends VerticalLayout {

        private final EvenementService evenementService;

        final Grid<Evenement> grid;

        final TextField filter;

        final Button addNewBtn;

        final EvenementEditor editor;

        boolean isBib = SessionManager.isBibliothecaire();

        public EvenementView(
                        EvenementService evenementService,
                        EvenementEditor editor) {

                this.evenementService = evenementService;
                this.editor = editor;

                grid = new Grid<>(Evenement.class);

                filter = new TextField();

                addNewBtn = new Button("Ajouter", VaadinIcon.PLUS.create());
                addNewBtn.getStyle().set("visibility", "hidden");

                HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

                add(actions, grid, editor);

                grid.setColumns(
                                "titre",
                                "description",
                                "dateDebut",
                                "dateFin");

                grid.addColumn(
                                e -> e.getTypeEvenement() != null
                                                ? e.getTypeEvenement().getNom()
                                                : "")
                                .setHeader("Type");

                grid.addColumn(
                                e -> e.getBibliotheque() != null
                                                ? e.getBibliotheque().getNom()
                                                : "")
                                .setHeader("Bibliothèque");

                filter.setPlaceholder(
                                "Filtrer par titre");

                filter.setValueChangeMode(
                                ValueChangeMode.LAZY);

                filter.addValueChangeListener(
                                e -> listEvenements(e.getValue()));
                if (isBib) {
                        addNewBtn.getStyle().set("visibility", "visible");
                        grid.asSingleSelect()
                                        .addValueChangeListener(
                                                        e -> editor.editEvenement(
                                                                        e.getValue()));

                        addNewBtn.addClickListener(
                                        e -> editor.editEvenement(
                                                        new Evenement()));

                        editor.setChangeHandler(() -> {
                                editor.setVisible(false);
                                listEvenements(filter.getValue());
                        });
                        editor.setCancelHandler(() -> {
                                grid.deselectAll();
                            });
                }
                listEvenements(null);
        }

        void listEvenements(String filterText) {

                if (StringUtils.hasText(filterText)) {

                        grid.setItems(
                                        evenementService
                                                        .getEvenementsByTitreLike(
                                                                        "%" + filterText + "%"));
                } else {

                        grid.setItems(
                                        evenementService
                                                        .getAllEvenements());
                }
        }
}