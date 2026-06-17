package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.service.TypeEvenementService;
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
public class TypeEvenementEditor extends VerticalLayout implements KeyNotifier {

    private final TypeEvenementService typeEvenementService;
    private TypeEvenement typeEvenement;

    TextField nom = new TextField("Nom du type d'événement");

    HorizontalLayout fields = new HorizontalLayout(nom);

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<TypeEvenement> binder = new Binder<>(TypeEvenement.class);
    private ChangeHandler changeHandler;

    public TypeEvenementEditor(TypeEvenementService service) {
        this.typeEvenementService = service;

        add(fields, actions);

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
        typeEvenementService.deleteTypeEvenementById(typeEvenement.getId());
        changeHandler.onChange();
    }

    void save() {
        typeEvenementService.saveTypeEvenement(typeEvenement);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editTypeEvenement(TypeEvenement te) {
        if (te == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = te.getId() != null;
        if (persisted) {
            typeEvenement = typeEvenementService.getTypeEvenementById(te.getId());
        } else {
            typeEvenement = te;
        }
        cancel.setVisible(true);

        binder.setBean(typeEvenement);

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