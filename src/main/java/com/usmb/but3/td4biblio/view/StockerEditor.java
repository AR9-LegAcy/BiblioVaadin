package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Stocker;
import com.usmb.but3.td4biblio.entity.StockerId;
import com.usmb.but3.td4biblio.service.StockerService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@Scope("prototype")
@SpringComponent
@UIScope
public class StockerEditor extends VerticalLayout implements KeyNotifier {
    private final StockerService stockerService;
    private Stocker stocker;

    TextField idBibliothque = new TextField("ID Bibliothèque");
    TextField idDocument = new TextField("ID Document");

    HorizontalLayout fields1 = new HorizontalLayout(idBibliothque, idDocument);

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Stocker> binder = new Binder<>(Stocker.class);
    private ChangeHandler changeHandler;

    public StockerEditor(StockerService service) {
        this.stockerService = service;

        add(fields1, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);
        actions.setSpacing(true);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);


        addKeyPressListener(Key.ENTER, e -> save());
    }

    void delete() {
        StockerId id = new StockerId(stocker.getIdBibliotheque(), stocker.getIdDocument());
        stockerService.deleteStockById(id);
        changeHandler.onChange();
    }

    void save() {
        stockerService.saveStocker(stocker);
        changeHandler.onChange();
    }

    public interface ChangeHandler {    
        void onChange();
    }

    public void editStocker(Stocker stocker) {
        if (stocker == null) {
            setVisible(false);
            return;
        }

        this.stocker = stocker;
        binder.readBean(stocker);
        setVisible(true);
        idBibliothque.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }
}
