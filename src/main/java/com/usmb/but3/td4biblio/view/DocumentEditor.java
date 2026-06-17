package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.IsbnGeneratorService;
import com.usmb.but3.td4biblio.service.TypeDocumentService;

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

import java.time.LocalDate;

@Scope("prototype")
@SpringComponent
@UIScope
public class DocumentEditor extends VerticalLayout implements KeyNotifier {

    private final DocumentService documentService;
    private final TypeDocumentService typeDocumentService;
    private final IsbnGeneratorService isbnGeneratorService;

    private Document document;

    /* Champs */
    TextField codeIsbn = new TextField("Code ISBN");
    TextField descriptionDocument = new TextField("Description");

    ComboBox<Boolean> codeEmpruntable = new ComboBox<>("Empruntable");
    ComboBox<String> etatDocument = new ComboBox<>("État");
    ComboBox<TypeDocument> typeDocument = new ComboBox<>("Type de document");

    DatePicker dateAcquisition = new DatePicker("Date d'acquisition");
    TextField formatTaille = new TextField("Format/Taille");
    TextField codeEmplacement = new TextField("Code d'emplacement");

    HorizontalLayout fields1 = new HorizontalLayout(codeIsbn, descriptionDocument);
    HorizontalLayout fields2 = new HorizontalLayout(codeEmpruntable, etatDocument, typeDocument);
    HorizontalLayout fields3 = new HorizontalLayout(dateAcquisition, formatTaille, codeEmplacement);

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Document> binder = new Binder<>(Document.class);

    private ChangeHandler changeHandler;
    private CancelHandler cancelHandler;

    public DocumentEditor(DocumentService documentService,
            TypeDocumentService typeDocumentService,
            IsbnGeneratorService isbnGeneratorService) {

        this.documentService = documentService;
        this.typeDocumentService = typeDocumentService;
        this.isbnGeneratorService = isbnGeneratorService;

        add(fields1, fields2, fields3, actions);

        /* -------- Empruntable -------- */
        codeEmpruntable.setItems(true, false);
        codeEmpruntable.setItemLabelGenerator(v -> Boolean.TRUE.equals(v) ? "Oui" : "Non");

        /* -------- Etat -------- */
        etatDocument.setItems(
                "Neuf",
                "Très bon état",
                "Bon état",
                "Abîmé",
                "Hors service");
        etatDocument.setAllowCustomValue(false);

        /* -------- Type document -------- */
        typeDocument.setItems(typeDocumentService.getAllTypeDocuments());
        typeDocument.setItemLabelGenerator(TypeDocument::getNomTypeDocument);

        /* -------- Binding -------- */
        binder.forField(codeIsbn)
                .bind(Document::getCodeIsbn, Document::setCodeIsbn);

        binder.forField(descriptionDocument)
                .bind(Document::getDescriptionDocument, Document::setDescriptionDocument);

        binder.forField(codeEmpruntable)
                .bind(Document::getCodeEmpruntable, Document::setCodeEmpruntable);

        binder.forField(etatDocument)
                .bind(Document::getEtatDocument, Document::setEtatDocument);

        binder.forField(typeDocument)
                .bind(Document::getTypeDocument, Document::setTypeDocument);

        binder.forField(dateAcquisition)
                .bind(Document::getDateAcquisition, Document::setDateAcquisition);

        binder.forField(formatTaille)
                .bind(Document::getFormatTaille, Document::setFormatTaille);

        binder.forField(codeEmplacement)
                .bind(Document::getCodeEmplacement, Document::setCodeEmplacement);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());

        addKeyPressListener(Key.ENTER, e -> save());

        setVisible(false);
    }

    void save() {
        if (document != null) {
            try {
                binder.writeBean(document);

                Bibliotheque bib = SessionManager.getBibliothecaire().getBibliotheque();

                documentService.saveDocument(document, bib);

                if (changeHandler != null)
                    changeHandler.onChange();

                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void delete() {
        if (document != null && document.getIdDocument() != null) {
            documentService.deleteDocumentById(document.getIdDocument());

            if (changeHandler != null)
                changeHandler.onChange();
        }
    }

    public final void editDocument(Document document) {

        boolean isNew = (document == null || document.getIdDocument() == null);

        if (isNew) {
            this.document = new Document();

            this.document.setCodeIsbn(isbnGeneratorService.generateNextIsbn());
            this.document.setDateAcquisition(LocalDate.now());
            this.document.setCodeEmpruntable(true);
            this.document.setEtatDocument("Neuf");

        } else {
            this.document = documentService.getDocumentById(document.getIdDocument());
        }

        binder.setBean(this.document);

        setVisible(true);

        codeIsbn.setReadOnly(true); // OK mais seulement après bind

        codeIsbn.focus();
    }

    void cancel() {
        setVisible(false);
        if (cancelHandler != null)
            cancelHandler.onCancel();
    }

    /* -------- handlers -------- */
    public interface ChangeHandler {
        void onChange();
    }

    public interface CancelHandler {
        void onCancel();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }

    public void setCancelHandler(CancelHandler h) {
        this.cancelHandler = h;
    }
}