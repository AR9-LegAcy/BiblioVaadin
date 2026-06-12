package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.EmprunterId;
import com.usmb.but3.td4biblio.service.EmprunterService;
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
 * Form editor for Emprunter entity
 */
@Scope("prototype")
@SpringComponent
@UIScope
public class EmprunterEditor extends VerticalLayout implements KeyNotifier {

    private final EmprunterService emprunterService;
    private Emprunter emprunter;

    /* Fields to edit properties in Emprunter entity */
    TextField carteEmprunteur = new TextField("Carte Emprunteur");
    TextField idDocument = new TextField("ID Document");
    DatePicker dateEmprunt = new DatePicker("Date d'emprunt");
    DatePicker dateRetourPrevue = new DatePicker("Date de retour prévue");
    DatePicker dateRetourReelle = new DatePicker("Date de retour réelle");
    TextField prolongationEmprunt = new TextField("Prolongation d'emprunt");

    HorizontalLayout fields1 = new HorizontalLayout(carteEmprunteur, idDocument);
    HorizontalLayout fields2 = new HorizontalLayout(dateEmprunt, dateRetourPrevue, dateRetourReelle);
    HorizontalLayout fields3 = new HorizontalLayout(prolongationEmprunt);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Emprunter> binder = new Binder<>(Emprunter.class);
    private ChangeHandler changeHandler;

    public EmprunterEditor(EmprunterService service) {
        this.emprunterService = service;

        add(fields1, fields2, fields3, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);
        actions.setSpacing(true);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmprunter(emprunter));

        // Listen to changes made by the user
        addKeyPressListener(Key.ENTER, e -> save());
    }

    void delete() {
        EmprunterId id = new EmprunterId(emprunter.getIdDocument(), emprunter.getCarteEmprunteur());
        emprunterService.deleteEmpruntById(id);
        changeHandler.onChange();
    }

    void save() {
        emprunterService.saveEmprunt(emprunter);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void editEmprunter(Emprunter emprunter) {
        if (emprunter == null) {
            setVisible(false);
            return;
        }
        this.emprunter = emprunter;
        binder.readBean(emprunter);
        setVisible(true);
        carteEmprunteur.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }
}
