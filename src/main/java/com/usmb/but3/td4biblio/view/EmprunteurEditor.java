package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.service.EmprunteurService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.Text;
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
 * Form editor for Emprunteur entity
 */
@Scope("prototype")
@SpringComponent
@UIScope
public class EmprunteurEditor extends VerticalLayout implements KeyNotifier {

    private final EmprunteurService emprunteurService;
    private Emprunteur emprunteur;

    /* Fields to edit properties in Emprunteur entity */
    TextField prenom = new TextField("Prénom");
    TextField nom = new TextField("Nom");
    TextField email = new TextField("Email");
    TextField adresseRue = new TextField("Adresse");
    TextField adresseVille = new TextField("Ville de l'emprunteur");
    TextField adresseCodePostal = new TextField("Code Postal");
    DatePicker dateNaissance = new DatePicker("Date de naissance");
    DatePicker debutAbonnement = new DatePicker("Date début d'abonnement");
    DatePicker expirationAbonnement = new DatePicker("Date fin d'abonnement");

    HorizontalLayout fields1 = new HorizontalLayout(prenom, nom, email, dateNaissance);
    HorizontalLayout fields2 = new HorizontalLayout(adresseRue, adresseVille, adresseCodePostal);
    HorizontalLayout fields3 = new HorizontalLayout(debutAbonnement, expirationAbonnement);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Emprunteur> binder = new Binder<>(Emprunteur.class);
    private ChangeHandler changeHandler;

    public EmprunteurEditor(EmprunteurService service) {
        this.emprunteurService = service;

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
        cancel.addClickListener(e -> editEmprunteur(emprunteur));
        addKeyPressListener(Key.ENTER, e -> save());
        setVisible(false);
    }

    void delete() {
        emprunteurService.deleteEmprunteurById(emprunteur.getCarteEmprunteur());
        changeHandler.onChange();
    }
    
    void save() {
        emprunteurService.saveEmprunteur(emprunteur);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editEmprunteur(Emprunteur e) {
        if (e == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = e.getCarteEmprunteur() != null;
        if (persisted) {
            // Find fresh entity for editing
            emprunteur = emprunteurService.getEmprunteurById(e.getCarteEmprunteur());
        }
        else {
            emprunteur = e;
        }
        binder.setBean(emprunteur);
        setVisible(true);
        nom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }
}
