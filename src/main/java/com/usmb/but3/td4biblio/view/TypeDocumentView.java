package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.service.TypeDocumentService;
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
@Route(value = "type-document")
@PageTitle("Types de documents")
@Menu(title = "Types de documents", order = 1, icon = "vaadin:tags")
public class TypeDocumentView extends VerticalLayout {
    private final TypeDocumentService typeDocumentService;
    final Grid<TypeDocument> grid;
    final TextField filter;
    private final Button addNewBtn;
    final TypeDocumentEditor editor;

    public TypeDocumentView(TypeDocumentService typeDocumentService, TypeDocumentEditor editor) {
        this.typeDocumentService = typeDocumentService;
        this.editor = editor;
        this.grid = new Grid<>(TypeDocument.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un type de document", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("idTypeDocument", "nomTypeDocument");
        grid.getColumnByKey("idTypeDocument").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filtrer par nom du type de document");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listTypeDocuments(e.getValue()));

        
        // Instantiate and edit new Auteur when the new button is clicked
        addNewBtn.addClickListener(e -> editor.editTypeDocument(new TypeDocument()));

        // Connect selected TypeDocument to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editTypeDocument(e.getValue());
        });

        // Initialize listing
        listTypeDocuments(null);
    }

    void listTypeDocuments(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(typeDocumentService.getAllTypeDocuments());
        } else {
            grid.setItems(typeDocumentService.getTypeDocumentsByNomTypeDocumentLike(filterText));
        }
    }

    public Button getAddNewBtn() {
        return addNewBtn;
    }
}
