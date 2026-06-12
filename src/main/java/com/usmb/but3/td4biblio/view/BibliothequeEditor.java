package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.service.BibliothequeService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Form editor for Bibliotheque entity
 */
@Scope("prototype")
@SpringComponent
@UIScope
public class BibliothequeEditor extends VerticalLayout implements KeyNotifier {

    private final BibliothequeService bibliothequeService;
    private Bibliotheque bibliotheque;

    /* Fields to edit properties in Bibliotheque entity */
    TextField nom = new TextField("nom");
    TextField adresse_rue = new TextField("rue");
    TextField adresse_ville = new TextField("ville");
    TextField adresse_cp = new TextField("code postal");
    TextField horaires = new TextField("horaires");

    HorizontalLayout fields1 = new HorizontalLayout(nom);
    HorizontalLayout fields2 = new HorizontalLayout(adresse_rue, adresse_ville, adresse_cp);
    HorizontalLayout fields3 = new HorizontalLayout(horaires);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Bibliotheque> binder = new Binder<>(Bibliotheque.class);
    private ChangeHandler changeHandler;

    public BibliothequeEditor(BibliothequeService service) {
        this.bibliothequeService = service;

        add(fields1, fields2, fields3, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editBibliotheque(bibliotheque));
        setVisible(false);
    }

    void delete() {
        bibliothequeService.deleteBibliothequeById(bibliotheque.getId());
        changeHandler.onChange();
    }

    void save() {
        bibliothequeService.saveBibliotheque(bibliotheque);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editBibliotheque(Bibliotheque a) {
        if (a == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = a.getId() != null;
        if (persisted) {
            bibliotheque = bibliothequeService.getBibliothequeById(a.getId());
        } else {
            bibliotheque = a;
        }
        cancel.setVisible(persisted);

        // Bind bibliotheque properties to similarly named fields
        binder.setBean(bibliotheque);

        setVisible(true);

        // Focus first name initially
        nom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}