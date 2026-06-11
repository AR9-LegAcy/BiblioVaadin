package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;
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
 * Form editor for Auteur entity
 */
@Scope("prototype")
@SpringComponent
@UIScope
public class AuteurEditor extends VerticalLayout implements KeyNotifier {

    private final AuteurService auteurService;
    private Auteur auteur;

    /* Fields to edit properties in Auteur entity */
    TextField prenom = new TextField("Prénom");
    TextField nom = new TextField("Nom");
    TextField nationalite = new TextField("Nationalité");
    TextField paysAuteur = new TextField("Pays");
    TextField villeAuteur = new TextField("Ville");
    TextField wikipediaAuteur = new TextField("Wikipedia");
    DatePicker dateNaissance = new DatePicker("Date de naissance");
    DatePicker dateDeces = new DatePicker("Date de décès");

    HorizontalLayout fields1 = new HorizontalLayout(prenom, nom);
    HorizontalLayout fields2 = new HorizontalLayout(nationalite, paysAuteur, villeAuteur);
    HorizontalLayout fields3 = new HorizontalLayout(dateNaissance, dateDeces);
    HorizontalLayout fields4 = new HorizontalLayout(wikipediaAuteur);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Auteur> binder = new Binder<>(Auteur.class);
    private ChangeHandler changeHandler;

    public AuteurEditor(AuteurService service) {
        this.auteurService = service;

        add(fields1, fields2, fields3, fields4, actions);

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
        cancel.addClickListener(e -> editAuteur(auteur));
        setVisible(false);
    }

    void delete() {
        auteurService.deleteAuteurById(auteur.getId());
        changeHandler.onChange();
    }

    void save() {
        auteurService.saveAuteur(auteur);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editAuteur(Auteur a) {
        if (a == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = a.getId() != null;
        if (persisted) {
            auteur = auteurService.getAuteurById(a.getId());
        } else {
            auteur = a;
        }
        cancel.setVisible(persisted);

        // Bind auteur properties to similarly named fields
        binder.setBean(auteur);

        setVisible(true);

        // Focus first name initially
        prenom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}