package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
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
    final Grid<Document> grid;
    final TextField filter;
    private final Button addNewBtn;
    final DocumentEditor editor;

    public Button getAddNewBtn() {
        return addNewBtn;
    }

    public DocumentView(DocumentService documentService, DocumentEditor editor) {
        this.documentService = documentService;
        this.editor = editor;
        this.grid = new Grid<>(Document.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un document", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns(
            "idDocument","codeIsbn","descriptionDocument","codeEmpruntable","etatDocument","dateAcquisition","formatTaille","codeEmplacement"
        );
        
        grid.getColumnByKey("idDocument")
            .setWidth("50px")
            .setFlexGrow(0);

        filter.setPlaceholder("Filtrer par code ISBN ou description");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listDocuments(e.getValue()));

        // Connect selected Document to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editDocument(e.getValue());
        });

        // Initialize listing
        listDocuments(null);
    }

    void listDocuments(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(documentService.getDocumentsByDescription(filterText));
        } else {
            grid.setItems(documentService.getAllDocuments());
        }
    }
}
