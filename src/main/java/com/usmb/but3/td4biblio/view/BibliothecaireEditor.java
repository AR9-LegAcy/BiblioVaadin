package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.service.BibliothecaireService;
import com.usmb.but3.td4biblio.service.BibliothequeService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
public class BibliothecaireEditor extends VerticalLayout implements KeyNotifier {

    private final BibliothecaireService bibliothecaireService;
    private final BibliothequeService bibliothequeService;
    private Bibliothecaire bibliothecaire;

    TextField pseudo = new TextField("Pseudo");
    TextField nom = new TextField("Nom");
    TextField prenom = new TextField("Prénom");
    ComboBox<Bibliotheque> bibliothequeField = new ComboBox<>("Bibliothèque");
    TextField adresseRue = new TextField("Rue");
    TextField adresseVille = new TextField("Ville");
    TextField adresseCP = new TextField("Code Postal");
    TextField email = new TextField("Email");
    DatePicker dateNaissance = new DatePicker("Date de naissance");
    TextField motDePasse = new TextField("Mot de passe");

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Bibliothecaire> binder = new Binder<>(Bibliothecaire.class);
    private ChangeHandler changeHandler;

    public BibliothecaireEditor(BibliothecaireService service, BibliothequeService bibliothequeService) {
        this.bibliothecaireService = service;
        this.bibliothequeService = bibliothequeService;

        // ✅ Remplir ComboBox Bibliothèque avec les items
        bibliothequeField.setItems(this.bibliothequeService.getAllBibliotheques());
        bibliothequeField.setItemLabelGenerator(Bibliotheque::getNom);
        HorizontalLayout fields1 = new HorizontalLayout(pseudo, prenom, nom, bibliothequeField);
        HorizontalLayout fields2 = new HorizontalLayout(adresseRue, adresseVille, adresseCP);
        HorizontalLayout fields3 = new HorizontalLayout(email);
        HorizontalLayout fields4 = new HorizontalLayout(dateNaissance);
        HorizontalLayout fields5 = new HorizontalLayout(motDePasse);
        add(fields1, fields2, fields3, fields4, fields5, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());

        addKeyPressListener(Key.ENTER, e -> save());

        setVisible(false);
    }

    void delete() {
        if (bibliothecaire != null) {
            bibliothecaireService.deleteBibliothecaireByPseudo(bibliothecaire.getPseudo());
            changeHandler.onChange();
        }
    }

    void save() {
        if (bibliothecaire != null) {
            // Synchroniser le ComboBox pour JPA avant sauvegarde
            bibliothecaire.setBibliotheque(bibliothequeField.getValue());
            bibliothecaireService.saveBibliothecaire(bibliothecaire);
            changeHandler.onChange();
        }
    }

    void cancel() {
        setVisible(false);
        if (cancelHandler != null) {
            cancelHandler.onCancel();
        }
    }

    public final void editBibliothecaire(Bibliothecaire b) {
        if (b == null) {
            setVisible(false);
            return;
        }

        bibliothecaire = b;

        // Assurer que le ComboBox est mis à jour avant le setBean
        bibliothequeField.setItems(this.bibliothequeService.getAllBibliotheques());

        // Pré-sélectionner la bibliothèque actuelle
        if (bibliothecaire.getBibliotheque() != null) {
            bibliothequeField.setValue(bibliothecaire.getBibliotheque());
        }

        binder.setBean(bibliothecaire);

        setVisible(true);
        prenom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }

    public interface ChangeHandler {
        void onChange();
    }

    public interface CancelHandler {
        void onCancel();
    }

    private CancelHandler cancelHandler;

    public void setCancelHandler(CancelHandler cancelHandler) {
        this.cancelHandler = cancelHandler;
    }
}