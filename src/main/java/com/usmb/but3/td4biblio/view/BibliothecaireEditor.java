package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.service.BibliothecaireService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Form editor for Bibliothecaire entity
 */
@Scope("prototype")
@SpringComponent
@UIScope
public class BibliothecaireEditor extends VerticalLayout implements KeyNotifier {

    private final BibliothecaireService bibliothecaireService;
    private Bibliothecaire bibliothecaire;

    /* Fields to edit properties in Bibliothecaire entity */
    TextField pseudo = new TextField("Pseudo");
    TextField nom = new TextField("Nom");
    TextField prenom = new TextField("Prénom");
    ComboBox<Bibliotheque> bibliothequeField = new ComboBox<>("Bibliothèque");
    {
        bibliothequeField.setItemLabelGenerator(Bibliotheque::getNom);
    }
    TextField adresseRue = new TextField("Rue");
    TextField adresseVille = new TextField("Ville");
    TextField adresseCP = new TextField("Code Postal");
    TextField email = new TextField("Email");
    DatePicker dateNaissance = new DatePicker("Date de naissance");
    TextField motDePasse = new TextField("Mot de passe");

    HorizontalLayout fields1 = new HorizontalLayout(pseudo, prenom, nom, bibliothequeField);
    HorizontalLayout fields2 = new HorizontalLayout(adresseRue, adresseVille, adresseCP);
    HorizontalLayout fields3 = new HorizontalLayout(email);
    HorizontalLayout fields4 = new HorizontalLayout(dateNaissance);
    HorizontalLayout fields5 = new HorizontalLayout(motDePasse);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Bibliothecaire> binder = new Binder<>(Bibliothecaire.class);
    private ChangeHandler changeHandler;

    public BibliothecaireEditor(BibliothecaireService service) {
        this.bibliothecaireService = service;

        add(fields1, fields2, fields3, fields4, fields5, actions);

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
        cancel.addClickListener(e -> editBibliothecaire(bibliothecaire));
        setVisible(false);
    }

    void delete() {
        bibliothecaireService.deleteBibliothecaireByPseudo(bibliothecaire.getPseudo());
        changeHandler.onChange();
    }

    void save() {
        bibliothecaireService.saveBibliothecaire(bibliothecaire);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editBibliothecaire(Bibliothecaire a) {
        if (a == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = a.getNom() != null;
        if (persisted) {
            bibliothecaire = bibliothecaireService.getBibliothecaireByPseudo(a.getPseudo());
        } else {
            bibliothecaire = a;
        }
        cancel.setVisible(persisted);

        // Bind bibliothecaire properties to similarly named fields
        binder.setBean(bibliothecaire);

        setVisible(true);

        // Focus first name initially
        prenom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}