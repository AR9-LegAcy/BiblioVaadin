package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.LivreService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Form editor for Livre entity
 */
@Scope("prototype")
@SpringComponent
@UIScope
public class LivreEditor extends VerticalLayout implements KeyNotifier {

    private final LivreService livreService;
    private Livre livre;

    /* Fields to edit properties in Livre entity */
    TextField titreLivre = new TextField("Titre");
    TextField codeIsbn = new TextField("Code ISBN");
    IntegerField idEditeur = new IntegerField("ID Éditeur");
    IntegerField idTypeDocument = new IntegerField("ID Type Document");
    IntegerField nbPages = new IntegerField("Nombre de pages");
    DatePicker datePublication = new DatePicker("Date de publication");
    DatePicker dateAcquisition = new DatePicker("Date d'acquisition");
    TextField formatTaille = new TextField("Format/Taille");
    TextField codeEmplacement = new TextField("Code emplacement");
    TextField descriptionDocument = new TextField("Description");
    TextField gifDocument = new TextField("Image (GIF)");
    TextField etatDocument = new TextField("État du document");

    HorizontalLayout fields1 = new HorizontalLayout(titreLivre, codeIsbn);
    HorizontalLayout fields2 = new HorizontalLayout(idEditeur, idTypeDocument);
    HorizontalLayout fields3 = new HorizontalLayout(nbPages, datePublication, dateAcquisition);
    HorizontalLayout fields4 = new HorizontalLayout(formatTaille, codeEmplacement);
    HorizontalLayout fields5 = new HorizontalLayout(descriptionDocument, etatDocument);
    HorizontalLayout fields6 = new HorizontalLayout(gifDocument);

    /* Action buttons */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Livre> binder = new Binder<>(Livre.class);
    private ChangeHandler changeHandler;

    public LivreEditor(LivreService service) {
        this.livreService = service;

        add(fields1, fields2, fields3, fields4, fields5, fields6, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editLivre(livre));
        setVisible(false);
    }

    void delete() {
        livreService.deleteLivreById(livre.getIdDocument());
        changeHandler.onChange();
    }

    void save() {
        livreService.saveLivre(livre);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editLivre(Livre l) {
        if (l == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = l.getIdDocument() != null;
        if (persisted) {
            livre = livreService.getLivreById(l.getIdDocument());
        } else {
            livre = l;
        }
        cancel.setVisible(persisted);

        // Bind livre properties to similarly named fields
        binder.setBean(livre);

        setVisible(true);

        // Focus title initially
        titreLivre.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}