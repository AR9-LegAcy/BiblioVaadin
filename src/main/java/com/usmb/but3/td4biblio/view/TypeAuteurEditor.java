package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.service.TypeAuteurService;
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
public class TypeAuteurEditor extends VerticalLayout implements KeyNotifier {

    private final TypeAuteurService typeAuteurService;

    private TypeAuteur typeAuteur;

    TextField nom = new TextField("Nom");

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<TypeAuteur> binder = new Binder<>(TypeAuteur.class);

    private ChangeHandler changeHandler;

    public TypeAuteurEditor(TypeAuteurService service) {

        this.typeAuteurService = service;

        add(nom, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());

        setVisible(false);
    }

    void delete() {
        typeAuteurService.deleteTypeAuteurById(typeAuteur.getId());
        changeHandler.onChange();
    }

    void save() {
        typeAuteurService.saveTypeAuteur(typeAuteur);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editTypeAuteur(TypeAuteur t) {

        if (t == null) {
            setVisible(false);
            return;
        }

        final boolean persisted = t.getId() != null;

        if (persisted) {
            typeAuteur = typeAuteurService.getTypeAuteurById(t.getId());
        } else {
            typeAuteur = t;
        }

        cancel.setVisible(true);

        binder.setBean(typeAuteur);

        setVisible(true);

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