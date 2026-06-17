package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.service.EditeurService;
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
 * Form editor for Editeur entity
 */
@Scope("prototype")
@SpringComponent
@UIScope
public class EditeurEditor extends VerticalLayout implements KeyNotifier {

    private final EditeurService editeurService;
    private Editeur editeur;

    /* Fields to edit properties in Editeur entity */
    TextField nom = new TextField("nom");
    TextField adresse = new TextField("adresse");
    TextField siteWeb = new TextField("site Web");
    TextField wikipedia = new TextField("wikipedia");

    HorizontalLayout fields1 = new HorizontalLayout(nom);
    HorizontalLayout fields2 = new HorizontalLayout(adresse);
    HorizontalLayout fields3 = new HorizontalLayout(siteWeb, wikipedia);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Editeur> binder = new Binder<>(Editeur.class);
    private ChangeHandler changeHandler;

    public EditeurEditor(EditeurService service) {
        this.editeurService = service;

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
        cancel.addClickListener(e -> cancel());
        setVisible(false);
    }

    void delete() {
        editeurService.deleteEditeurById(editeur.getId());
        changeHandler.onChange();
    }

    void save() {
        editeurService.saveEditeur(editeur);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editEditeur(Editeur a) {
        if (a == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = a.getId() != null;
        if (persisted) {
            editeur = editeurService.getEditeurById(a.getId());
        } else {
            editeur = a;
        }
        cancel.setVisible(true);

        // Bind editeur properties to similarly named fields
        binder.setBean(editeur);

        setVisible(true);

        // Focus first name initially
        nom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
    void cancel() {
        setVisible(false);
        if (cancelHandler != null) {
            cancelHandler.onCancel();
        }
    }

    public interface CancelHandler {
        void onCancel();
    }

    private CancelHandler cancelHandler;

    public void setCancelHandler(CancelHandler cancelHandler) {
        this.cancelHandler = cancelHandler;
    }
}