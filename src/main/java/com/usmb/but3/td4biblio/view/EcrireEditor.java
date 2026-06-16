package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Ecrire;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.EcrireService;
import com.usmb.but3.td4biblio.service.LivreService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@Scope("prototype")
public class EcrireEditor extends VerticalLayout implements KeyNotifier {

    private final EcrireService ecrireService;

    private Ecrire ecrire;

    ComboBox<Auteur> idAuteur = new ComboBox<>("Auteur");
    ComboBox<Livre> idLivre = new ComboBox<>("Type d'auteur");

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Ecrire> binder = new Binder<>(Ecrire.class);

    private ChangeHandler changeHandler;

    public EcrireEditor(EcrireService ecrireService, AuteurService auteurService, LivreService livreService) {
        this.ecrireService = ecrireService;

        idAuteur.setItems(auteurService.getAllAuteurs());
        idAuteur.setItemLabelGenerator(a -> a.getPrenom() + " " + a.getNom());

        idLivre.setItems(livreService.getAllLivres());
        idLivre.setItemLabelGenerator(Livre::getTitreLivre);

        add(idAuteur, idLivre, actions);

        binder.bindInstanceFields(this);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEcrire(ecrire));

        setVisible(false);
    }

    void save() {
        ecrireService.saveEcrire(ecrire);
        changeHandler.onChange();
    }

    void delete() {
        ecrireService.deleteEcrireById(new com.usmb.but3.td4biblio.entity.EcrireId(ecrire.getIdAuteur().getId(), ecrire.getIdLivre().getIdDocument()));
        changeHandler.onChange();
    }

    public final void editEcrire(Ecrire c) {
        if (c == null) {
            setVisible(false);
            return;
        }

        ecrire = c;
        binder.setBean(ecrire);

        setVisible(true);
        idAuteur.focus();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}