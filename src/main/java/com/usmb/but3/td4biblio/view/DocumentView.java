package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope("prototype")
@Route(value = "document")
@PageTitle("Documents")
@Menu(title = "Documents", order = 1, icon = "vaadin:archive")
public class DocumentView extends VerticalLayout {

    private final DocumentService documentService;
    private final Grid<Document> grid;
    private final TextField filter;
    private final Button addNewBtn;
    private final DocumentEditor editor;

    private final boolean isBib = SessionManager.isBibliothecaire();

    public DocumentView(DocumentService documentService, DocumentEditor editor) {
        this.documentService = documentService;
        this.editor = editor;

        this.grid = new Grid<>(Document.class, false);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un document", VaadinIcon.PLUS.create());

        addNewBtn.getStyle().set("visibility", "hidden");

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");

        // ---------------- Colonnes ----------------
        grid.addColumn(Document::getCodeIsbn).setHeader("ISBN");
        grid.addColumn(Document::getDescriptionDocument).setHeader("Description");
        grid.addColumn(Document::getCodeEmpruntable).setHeader("Empruntable");
        grid.addColumn(Document::getEtatDocument).setHeader("État");

        // 👉 NOUVEAU : type document
        grid.addColumn(doc ->
                doc.getTypeDocument() != null
                        ? doc.getTypeDocument().getNomTypeDocument()
                        : ""
        ).setHeader("Type de document");

        grid.addColumn(Document::getDateAcquisition).setHeader("Date acquisition");
        grid.addColumn(Document::getFormatTaille).setHeader("Format / Taille");
        grid.addColumn(Document::getCodeEmplacement).setHeader("Emplacement");

        // ---------------- Filter ----------------
        filter.setPlaceholder("Filtrer par ISBN ou description");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listDocuments(e.getValue()));

        // ---------------- Bibliothécaire ----------------
        if (isBib) {
            addNewBtn.getStyle().set("visibility", "visible");

            grid.asSingleSelect().addValueChangeListener(e ->
                    editor.editDocument(e.getValue())
            );

            addNewBtn.addClickListener(e ->
                    editor.editDocument(new Document())
            );

            editor.setChangeHandler(() -> {
                editor.setVisible(false);
                listDocuments(filter.getValue());
            });

            editor.setCancelHandler(() -> grid.deselectAll());
        }

        listDocuments(null);
    }

    void listDocuments(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(documentService.getDocumentsByDescription(filterText));
        } else {
            grid.setItems(documentService.getAllDocuments());
        }
    }

    public Button getAddNewBtn() {
        return addNewBtn;
    }
}