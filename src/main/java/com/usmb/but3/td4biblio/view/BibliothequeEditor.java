package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.service.BibliothequeService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
public class BibliothequeEditor extends VerticalLayout implements KeyNotifier {

    private final BibliothequeService bibliothequeService;
    private Bibliotheque bibliotheque;

    TextField nom = new TextField("Nom");
    TextField adresseRue = new TextField("Adresse");
    TextField adresseVille = new TextField("Ville");
    TextField codePostal = new TextField("Code postal");
    TextField horaires = new TextField("Horaires");

    HorizontalLayout fields = new HorizontalLayout(nom, adresseRue, adresseVille, codePostal, horaires);

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Bibliotheque> binder = new Binder<>(Bibliotheque.class);
    private ChangeHandler changeHandler;

    public BibliothequeEditor(BibliothequeService bibliothequeService) {
        this.bibliothequeService = bibliothequeService;

        add(fields, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editBibliotheque(bibliotheque));
        setVisible(false);
    }

    void delete() {
        bibliothequeService.deleteBibliothequeById(bibliotheque.getId());
        changeHandler.onChange();
    }

    void save() {
        bibliothequeService.saveBibliotheque(bibliotheque);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editBibliotheque(Bibliotheque b) {
        if (b == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = b.getId() != null;
        if (persisted) {
            bibliotheque = bibliothequeService.getBibliothequeById(b.getId());
        } else {
            bibliotheque = b;
        }
        cancel.setVisible(persisted);

        binder.setBean(bibliotheque);

        setVisible(true);

        nom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}