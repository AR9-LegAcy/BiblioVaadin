package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.service.StockerService;
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
@Route(value = "stocker")
@PageTitle("Le Stock")
@Menu(title = "Les Stocks", order = 1, icon = "vaadin:clipboard-check")
public class StockerView extends VerticalLayout {
    private final StockerService stockerService;
    final Grid<Stocker> grid;
    final TextField filter;
    private final Button addNewBtn;
    final StockerEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public StockerView(StockerService stockerService, StockerEditor editor) {
        this.stockerService = stockerService;
        this.editor = editor;
        this.grid = new Grid<>(Stocker.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un document dans une bibliothèque", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("idBibliotheque", "idDocument");
        grid.getColumnByKey("idBibliotheque").setWidth("150px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par document ou bibliotheque");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listStocks(e.getValue()));

        // Connect selected Auteur to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editStocker(e.getValue());
        });

        // Instantiate and edit new Auteur when the new button is clicked
        addNewBtn.addClickListener(e -> editor.editStocker(new Stocker()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listStocks(filter.getValue());
        });

        // Initialize listing
        listStocks(null);
    }

    void listStocks(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(stockerService.getEmpruntsByIdBibliotheque(Integer.parseInt(filterText)));
        } else {
            grid.setItems(stockerService.getAllStocks());
        }
    }
}
