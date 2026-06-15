package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import com.usmb.but3.td4biblio.entity.*;
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
    private final TypeDocumentService typeDocumentService;

    private Livre livre;

    // Fields
    TextField titreLivre = new TextField("Titre");
    TextField codeIsbn = new TextField("ISBN");

    ComboBox<Editeur> editeurField = new ComboBox<>("Éditeur");
    ComboBox<TypeDocument> typeDocumentField = new ComboBox<>("Type document");

    IntegerField nbPages = new IntegerField("Pages");
    DatePicker datePublication = new DatePicker("Publication");

    TextField formatTaille = new TextField("Format");
    TextField codeEmplacement = new TextField("Emplacement");

    TextField descriptionDocument = new TextField("Description");
    TextField etatDocument = new TextField("État");

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private ChangeHandler changeHandler;

    public LivreEditor(LivreService livreService,
                       EditeurService editeurService,
                       TypeDocumentService typeDocumentService) {

        this.livreService = livreService;
        this.editeurService = editeurService;
        this.typeDocumentService = typeDocumentService;

        // ComboBox data
        editeurField.setItems(editeurService.getAllEditeurs());
        editeurField.setItemLabelGenerator(Editeur::getNom);

        typeDocumentField.setItems(typeDocumentService.getAllTypeDocuments());
        typeDocumentField.setItemLabelGenerator(TypeDocument::getNomTypeDocument);

        // Layout
        add(
            new HorizontalLayout(titreLivre, codeIsbn),
            new HorizontalLayout(editeurField, typeDocumentField),
            new HorizontalLayout(nbPages, datePublication),
            new HorizontalLayout(formatTaille, codeEmplacement),
            new HorizontalLayout(descriptionDocument, etatDocument),
            actions
        );

        // Actions
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editLivre(livre));

        addKeyPressListener(Key.ENTER, e -> save());

        setVisible(false);
    }

    void save() {
        if (livre == null) return;

        livre.setTitreLivre(titreLivre.getValue());
        livre.setCodeIsbn(codeIsbn.getValue());
        livre.setNbPages(nbPages.getValue());
        livre.setDatePublication(datePublication.getValue());
        livre.setFormatTaille(formatTaille.getValue());
        livre.setCodeEmplacement(codeEmplacement.getValue());
        livre.setDescriptionDocument(descriptionDocument.getValue());
        livre.setEtatDocument(etatDocument.getValue());

        // IMPORTANT: relations corrigées
        livre.setIdEditeur(editeurField.getValue());
        livre.setIdTypeDocument(typeDocumentField.getValue());

        livreService.saveLivre(livre);

        if (changeHandler != null) changeHandler.onChange();
    }

    void delete() {
        if (livre != null && livre.getIdLivre() != null) {
            livreService.deleteLivreById(livre.getIdLivre());
            if (changeHandler != null) changeHandler.onChange();
        }
    }

    public void editLivre(Livre l) {
        if (l == null) {
            setVisible(false);
            return;
        }

        this.livre = l;

        titreLivre.setValue(l.getTitreLivre() != null ? l.getTitreLivre() : "");
        codeIsbn.setValue(l.getCodeIsbn() != null ? l.getCodeIsbn() : "");
        nbPages.setValue(l.getNbPages());
        datePublication.setValue(l.getDatePublication());

        formatTaille.setValue(l.getFormatTaille() != null ? l.getFormatTaille() : "");
        codeEmplacement.setValue(l.getCodeEmplacement() != null ? l.getCodeEmplacement() : "");
        descriptionDocument.setValue(l.getDescriptionDocument() != null ? l.getDescriptionDocument() : "");
        etatDocument.setValue(l.getEtatDocument() != null ? l.getEtatDocument() : "");

        // IMPORTANT COMBOBOX FIX
        editeurField.setValue(l.getIdEditeur());
        typeDocumentField.setValue(l.getIdTypeDocument());

        setVisible(true);
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }

    public interface ChangeHandler {
        void onChange();
    }
}