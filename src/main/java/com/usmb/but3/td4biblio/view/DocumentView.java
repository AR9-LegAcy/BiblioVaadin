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
import java.util.Set;

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

    // TYPES AUTORISÉS POUR AFFICHER LE BOUTON INFO LIVRE
    private static final Set<String> TYPES_INFO_LIVRE = Set.of(
            "Roman",
            "Bande dessinée",
            "Revue scientifique",
            "Manuel scolaire"
    );

    public DocumentView(DocumentService documentService, DocumentEditor editor) {
        this.documentService = documentService;
        this.editor = editor;

        // ---------------- GRID ----------------
        grid.addColumn(Document::getCodeIsbn)
                .setHeader("ISBN")
                .setSortable(true);

        grid.addColumn(Document::getDescriptionDocument)
                .setHeader("Description")
                .setSortable(true);

        grid.addColumn(d -> Boolean.TRUE.equals(d.getCodeEmpruntable()) ? "Oui" : "Non")
                .setHeader("Empruntable")
                .setSortable(true);

        grid.addColumn(Document::getEtatDocument)
                .setHeader("État")
                .setSortable(true);

        grid.addColumn(d -> d.getTypeDocument() != null
                        ? d.getTypeDocument().getNomTypeDocument()
                        : "")
                .setHeader("Type")
                .setSortable(true);

        grid.addColumn(Document::getDateAcquisition)
                .setHeader("Acquisition")
                .setSortable(true);

        // ---------------- BOUTON INFO LIVRE (MODIFIÉ UNIQUEMENT ICI) ----------------
        grid.addComponentColumn(doc -> {

            String type = doc.getTypeDocument() != null
                    ? doc.getTypeDocument().getNomTypeDocument()
                    : null;

            if (type != null && TYPES_INFO_LIVRE.contains(type)) {

                Button btn = new Button("Info livre", VaadinIcon.INFO_CIRCLE.create());

                btn.addClickListener(e ->
                        getUI().ifPresent(ui ->
                                ui.navigate("livre/detail/" + doc.getIdDocument())
                        )
                );

                return btn;
            }

            return null;

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