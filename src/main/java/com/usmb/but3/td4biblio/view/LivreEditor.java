package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.List;

@Scope("prototype")
@SpringComponent
@UIScope
public class LivreEditor extends VerticalLayout implements KeyNotifier {

    private final LivreService livreService;
    private final EditeurService editeurService;

    private Livre livre;

    TextField titreLivre = new TextField("Titre");
    IntegerField nbPages = new IntegerField("Nombre de pages");
    DatePicker datePublication = new DatePicker("Date de publication");
    ComboBox<Editeur> editeurField = new ComboBox<>("Éditeur");

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private ChangeHandler changeHandler;

    public LivreEditor(LivreService livreService, EditeurService editeurService) {
        this.livreService = livreService;
        this.editeurService = editeurService;

        // Utilisation de editeurService pour remplir le ComboBox
        editeurField.setItems(editeurService.getAllEditeurs());
        editeurField.setItemLabelGenerator(Editeur::getNom);

        add(new HorizontalLayout(titreLivre, nbPages, datePublication, editeurField),
                new HorizontalLayout(save, cancel, delete));

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));

        setVisible(false);
    }

    void save() {
        livre.setTitreLivre(titreLivre.getValue());
        livre.setNbPages(nbPages.getValue());
        livre.setDatePublication(datePublication.getValue());
        livre.setIdEditeur(editeurField.getValue());

        Bibliothecaire bib = SessionManager.getBibliothecaire();

        livreService.saveLivre(livre, bib.getBibliotheque());

        if (changeHandler != null) {
            changeHandler.onChange();
        }

        setVisible(false);
    }

    void delete() {
        if (livre != null) {
            livreService.deleteLivreById(livre.getIdDocument());

            // Appel du ChangeHandler après suppression
            if (changeHandler != null) {
                changeHandler.onChange();
            }

            setVisible(false);
        }
    }

    public void editLivre(Livre l) {
        if (l == null) {
            setVisible(false);
            return;
        }
        this.livre = l;

        titreLivre.setValue(l.getTitreLivre() != null ? l.getTitreLivre() : "");
        nbPages.setValue(l.getNbPages());
        datePublication.setValue(l.getDatePublication());
        editeurField.setValue(l.getIdEditeur());

        setVisible(true);
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }

    public interface ChangeHandler {
        void onChange();
    }
}