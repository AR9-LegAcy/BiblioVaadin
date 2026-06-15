package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Classer;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.ClasserService;
import com.usmb.but3.td4biblio.service.TypeAuteurService;
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
public class ClasserEditor extends VerticalLayout implements KeyNotifier {

    private final ClasserService classerService;

    private Classer classer;

    ComboBox<Auteur> idAuteur = new ComboBox<>("Auteur");
    ComboBox<TypeAuteur> idTypeAuteur = new ComboBox<>("Type d'auteur");

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());

    HorizontalLayout actions =
            new HorizontalLayout(save, cancel, delete);

    Binder<Classer> binder =
            new Binder<>(Classer.class);

    private ChangeHandler changeHandler;

    public ClasserEditor(
            ClasserService classerService,
            AuteurService auteurService,
            TypeAuteurService typeAuteurService) {

        this.classerService = classerService;

        idAuteur.setItems(auteurService.getAllAuteurs());
        idAuteur.setItemLabelGenerator(
                a -> a.getPrenom() + " " + a.getNom());

        idTypeAuteur.setItems(
                typeAuteurService.getAllTypeAuteurs());
        idTypeAuteur.setItemLabelGenerator(
                TypeAuteur::getNom);

        add(idAuteur, idTypeAuteur, actions);

        binder.bindInstanceFields(this);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editClasser(classer));

        setVisible(false);
    }

    void save() {
        classerService.saveClassement(classer);
        changeHandler.onChange();
    }

    void delete() {
        classerService.deleteClassementById(
                new com.usmb.but3.td4biblio.entity.ClasserId(
                        classer.getIdAuteur().getId(),
                        classer.getIdTypeAuteur().getId()));
        changeHandler.onChange();
    }

    public final void editClasser(Classer c) {

        if (c == null) {
            setVisible(false);
            return;
        }

        classer = c;

        binder.setBean(classer);

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