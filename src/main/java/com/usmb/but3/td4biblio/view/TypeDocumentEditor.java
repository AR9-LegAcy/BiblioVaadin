package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.service.TypeDocumentService;
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
public class TypeDocumentEditor extends VerticalLayout implements KeyNotifier {
    private final TypeDocumentService typeDocumentService;
    private TypeDocument typeDocument;

    /* Fields to edit properties in TypeDocument entity */
    TextField nomTypeDocument = new TextField("Nom du type de document");

    HorizontalLayout fields = new HorizontalLayout(nomTypeDocument);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<TypeDocument> binder = new Binder<>(TypeDocument.class);
    private ChangeHandler changeHandler;

    public TypeDocumentEditor(TypeDocumentService service) {
        this.typeDocumentService = service;

        add(fields, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editTypeDocument(typeDocument));
    }

    void delete() {
        typeDocumentService.deleteTypeDocumentById(typeDocument.getIdTypeDocument());
        changeHandler.onChange();
    }

    void save() {
        typeDocumentService.saveTypeDocument(typeDocument);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void editTypeDocument(TypeDocument td) {
        if (td == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = td.getIdTypeDocument() != null;
        if (persisted) {
            // Find fresh entity for editing
            typeDocument = typeDocumentService.getTypeDocumentById(td.getIdTypeDocument());
        } else {
            typeDocument = td;
        }
        cancel.setVisible(persisted);

        // Bind the TypeDocument to the editor fields
        binder.setBean(typeDocument);

        setVisible(true);

        // Focus on nomTypeDocument field when editing an existing TypeDocument
        nomTypeDocument.focus();
    }    

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
