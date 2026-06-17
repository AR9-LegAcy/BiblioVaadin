package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.service.DocumentService;
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

@Scope("prototype")
@SpringComponent
@UIScope
public class DocumentEditor extends VerticalLayout implements KeyNotifier {

    private final DocumentService documentService;
    private Document document;

    /* Fields to edit properties in Document entity */
    TextField codeIsbn = new TextField("Code ISBN");
    TextField descriptionDocument = new TextField("Description");
    TextField codeEmpruntable = new TextField("Empruntable (true/false)");
    TextField etatDocument = new TextField("Etat");
    DatePicker dateAcquisition = new DatePicker("Date d'acquisition");
    TextField formatTaille = new TextField("Format/Taille");
    TextField codeEmplacement = new TextField("Code d'emplacement");

    HorizontalLayout fields1 = new HorizontalLayout(codeIsbn, descriptionDocument);
    HorizontalLayout fields2 = new HorizontalLayout(codeEmpruntable, etatDocument);
    HorizontalLayout fields3 = new HorizontalLayout(dateAcquisition, formatTaille, codeEmplacement);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Document> binder = new Binder<>(Document.class);
    private ChangeHandler changeHandler;

    public DocumentEditor(DocumentService service) {
        this.documentService = service;

        add(fields1, fields2, fields3, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style buttons
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Add listeners
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());

        // Listen to changes made by the user
        addKeyPressListener(Key.ENTER, e -> save());
        setVisible(false);
    }

    void delete() {
        if (document != null && document.getIdDocument() != null) {
            documentService.deleteDocumentById(document.getIdDocument());
            changeHandler.onChange();
        }
    }

    void save() {
        if (document != null) {
            documentService.saveDocument(document);
            changeHandler.onChange();
        }
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editDocument(Document document) {
        if (document == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = document.getIdDocument() != null;
        if (persisted) {
            this.document = documentService.getDocumentById(document.getIdDocument());
        } else {
            this.document = document;
        }
        cancel.setVisible(true);

        binder.setBean(document);

        setVisible(true);

        codeIsbn.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
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
