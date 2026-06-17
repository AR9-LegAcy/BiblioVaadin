package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.service.BibliothequeService;
import com.usmb.but3.td4biblio.service.EvenementService;
import com.usmb.but3.td4biblio.service.TypeEvenementService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@Scope("prototype")
@SpringComponent
@UIScope
public class EvenementEditor extends VerticalLayout implements KeyNotifier {

    private final EvenementService evenementService;
    private final BibliothequeService bibliothequeService;
    private final TypeEvenementService typeEvenementService;

    private Evenement evenement;

    TextField titre = new TextField("Titre");
    TextArea description = new TextArea("Description");
    DatePicker dateDebut = new DatePicker("Date début");
    DatePicker dateFin = new DatePicker("Date fin");

    ComboBox<Bibliotheque> bibliothequeField = new ComboBox<>("Bibliothèque");
    ComboBox<TypeEvenement> typeEvenementField = new ComboBox<>("Type d'évènement");

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Evenement> binder = new Binder<>(Evenement.class);
    private ChangeHandler changeHandler;

    public EvenementEditor(EvenementService evenementService,
                           BibliothequeService bibliothequeService,
                           TypeEvenementService typeEvenementService) {

        this.evenementService = evenementService;
        this.bibliothequeService = bibliothequeService;
        this.typeEvenementService = typeEvenementService;

        // Initialise ComboBox
        bibliothequeField.setItems(this.bibliothequeService.getAllBibliotheques());
        bibliothequeField.setItemLabelGenerator(Bibliotheque::getNom);

        typeEvenementField.setItems(this.typeEvenementService.getAllTypeEvenements());
        typeEvenementField.setItemLabelGenerator(TypeEvenement::getNom);

        add(titre,
            description,
            new HorizontalLayout(dateDebut, dateFin),
            new HorizontalLayout(typeEvenementField, bibliothequeField),
            actions
        );

        binder.bindInstanceFields(this);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());

        addKeyPressListener(Key.ENTER, e -> save());

        setVisible(false);
    }

    void save() {
        if (evenement == null) return;

        // Synchronisation manuelle des ComboBox
        evenement.setBibliotheque(bibliothequeField.getValue());
        evenement.setTypeEvenement(typeEvenementField.getValue());

        evenement.setTitre(titre.getValue());
        evenement.setDescription(description.getValue());
        evenement.setDateDebut(dateDebut.getValue());
        evenement.setDateFin(dateFin.getValue());

        evenementService.saveEvenement(evenement);
        changeHandler.onChange();
    }

    void delete() {
        if (evenement == null) return;
        evenementService.deleteEvenementById(evenement.getId());
        changeHandler.onChange();
    }

    public final void editEvenement(Evenement e) {
        if (e == null) {
            setVisible(false);
            return;
        }

        boolean persisted = e.getId() != null;
        if (persisted) {
            evenement = evenementService.getEvenementById(e.getId());
        } else {
            evenement = e;
        }

        cancel.setVisible(true);

        // Bind bean
        binder.setBean(evenement);

        // Affiche correctement les valeurs sélectionnées dans les ComboBox
        bibliothequeField.setValue(evenement.getBibliotheque());
        typeEvenementField.setValue(evenement.getTypeEvenement());

        // Affiche les autres champs
        titre.setValue(evenement.getTitre() != null ? evenement.getTitre() : "");
        description.setValue(evenement.getDescription() != null ? evenement.getDescription() : "");
        dateDebut.setValue(evenement.getDateDebut());
        dateFin.setValue(evenement.getDateFin());

        setVisible(true);
        titre.focus();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
    void cancel() {
        setVisible(false);
        if (cancelHandler != null) {
            cancelHandler.onCancel();
        }
    }

    public interface CancelHandler {
        void onCancel();
    }

    private CancelHandler cancelHandler;

    public void setCancelHandler(CancelHandler cancelHandler) {
        this.cancelHandler = cancelHandler;
    }
}