package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.security.SessionManager;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.util.List;

@Component
@Scope("prototype")
@Route("document")
@PageTitle("Documents")
@Menu(title = "Documents", order = 1, icon = "vaadin:archive")
public class DocumentView extends VerticalLayout {

    private final DocumentService documentService;

    private final Grid<Document> grid = new Grid<>(Document.class, false);

    private final TextField searchValue = new TextField();
    private final ComboBox<String> critere = new ComboBox<>();
    private final ComboBox<String> mode = new ComboBox<>();
    

    private final Button addNewBtn = new Button("Ajouter", VaadinIcon.PLUS.create());

    private final DocumentEditor editor;

    private final boolean isBib = SessionManager.isBibliothecaire();

    public DocumentView(DocumentService documentService, DocumentEditor editor) {
        this.documentService = documentService;
        this.editor = editor;
        
        // ---------------- GRID ----------------
        grid.addColumn(Document::getCodeIsbn).setHeader("ISBN");
        grid.addColumn(Document::getDescriptionDocument).setHeader("Description");
        grid.addColumn(d -> Boolean.TRUE.equals(d.getCodeEmpruntable()) ? "Oui" : "Non")
                .setHeader("Empruntable");
        grid.addColumn(Document::getEtatDocument).setHeader("État");

        grid.addColumn(d -> d.getTypeDocument() != null
                        ? d.getTypeDocument().getNomTypeDocument()
                        : "")
                .setHeader("Type");

        grid.addColumn(Document::getDateAcquisition).setHeader("Acquisition");

        // bouton Livre
        grid.addComponentColumn(doc -> {

            if (doc.getTypeDocument() != null &&
                    "Livre".equalsIgnoreCase(doc.getTypeDocument().getNomTypeDocument())) {

                Button btn = new Button("Livre", VaadinIcon.BOOK.create());

                btn.addClickListener(e ->
                        getUI().ifPresent(ui ->
                                ui.navigate("livre/" + doc.getIdDocument())
                        )
                );

                return btn;
            }

            return new Button();
        }).setHeader("Info Livre");

        // ---------------- SEARCH UI ----------------
        critere.setItems("Titre", "Type", "Auteur", "Bibliothèque");
        critere.setValue("Titre");
        critere.setAllowCustomValue(false);
        
        mode.setItems("Égal à", "Contient", "Débute par");
        mode.setValue("Contient");
        mode.setAllowCustomValue(false);

        searchValue.setPlaceholder("Recherche...");
        searchValue.setValueChangeMode(ValueChangeMode.LAZY);

        searchValue.addValueChangeListener(e -> refresh());

        critere.addValueChangeListener(e -> refresh());
        mode.addValueChangeListener(e -> refresh());

        // ---------------- ACTIONS ----------------
        addNewBtn.getStyle().set("visibility", "hidden");

        HorizontalLayout topBar = new HorizontalLayout(
                critere,
                mode,
                searchValue,
                addNewBtn
        );

        add(topBar, grid, editor);

        // ---------------- BIB ----------------
        if (isBib) {

            addNewBtn.getStyle().set("visibility", "visible");

            grid.asSingleSelect().addValueChangeListener(
                    e -> editor.editDocument(e.getValue())
            );

            addNewBtn.addClickListener(
                    e -> editor.editDocument(new Document())
            );

            editor.setChangeHandler(() -> {
                editor.setVisible(false);
                refresh();
            });

            editor.setCancelHandler(() -> grid.deselectAll());
        }

        refresh();
    }

    private void refresh() {
        List<Document> docs = documentService.searchDocuments(
                critere.getValue(),
                mode.getValue(),
                searchValue.getValue()
        );

        grid.setItems(docs);
    }
}