package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunter;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EmprunterService;
import com.usmb.but3.td4biblio.service.EmprunteurService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@Scope("prototype")
@SpringComponent
@UIScope
public class EmprunterEditor extends VerticalLayout implements KeyNotifier {

    private final EmprunterService emprunterService;
    private final EmprunteurService emprunteurService;
    private final DocumentService documentService;
    private Emprunter emprunter;

    ComboBox<Emprunteur> emprunteurField = new ComboBox<>("Emprunteur");
    ComboBox<Document> documentField = new ComboBox<>("Document");
    DatePicker dateEmprunt = new DatePicker("Date d'emprunt");
    DatePicker dateRetourPrevue = new DatePicker("Date de retour prévue");
    DatePicker dateRetourReelle = new DatePicker("Date de retour réelle");
    TextField prolongationEmprunt = new TextField("Prolongation d'emprunt");

    HorizontalLayout fields1 = new HorizontalLayout(emprunteurField, documentField);
    HorizontalLayout fields2 = new HorizontalLayout(dateEmprunt, dateRetourPrevue, dateRetourReelle);
    HorizontalLayout fields3 = new HorizontalLayout(prolongationEmprunt);

    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Emprunter> binder = new Binder<>(Emprunter.class);
    private ChangeHandler changeHandler;

    public EmprunterEditor(EmprunterService emprunterService,
            EmprunteurService emprunteurService,
            DocumentService documentService) {
        this.emprunterService = emprunterService;
        this.emprunteurService = emprunteurService;
        this.documentService = documentService;

        // Initialise ComboBoxes après injection
        emprunteurField.setItems(this.emprunteurService.getAllEmprunteurs());
        emprunteurField.setItemLabelGenerator(Emprunteur::getNom);

        documentField.setItems(this.documentService.getAllDocuments());
        documentField.setItemLabelGenerator(Document::getCodeIsbn);

        add(fields1, fields2, fields3, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);
        actions.setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());

        addKeyPressListener(Key.ENTER, e -> save());
        setVisible(false);
    }

    void delete() {
        if (emprunter != null) {
            emprunterService.deleteEmpruntById(new com.usmb.but3.td4biblio.entity.EmprunterId(
                    emprunter.getIdDocument().getIdDocument(),
                    emprunter.getCarteEmprunteur().getCarteEmprunteur()));
            changeHandler.onChange();
        }
    }

    void save() {
        if (emprunter != null) {
            emprunter.setCarteEmprunteur(emprunteurField.getValue());
            emprunter.setIdDocument(documentField.getValue());
            emprunter.setDateEmprunt(dateEmprunt.getValue());
            emprunter.setDateRetourReelle(dateRetourReelle.getValue());
            try {
                emprunter.setProlongationEmprunt(
                        prolongationEmprunt.getValue() != null && !prolongationEmprunt.getValue().isEmpty()
                                ? Integer.parseInt(prolongationEmprunt.getValue())
                                : null);
            } catch (NumberFormatException ex) {
                emprunter.setProlongationEmprunt(null);
            }
            // Ajout de la prolongation
            if (dateRetourPrevue.getValue() != null
                    && emprunter.getProlongationEmprunt() != null) {

                emprunter.setDateRetourPrevue(
                        dateRetourPrevue.getValue()
                                .plusDays(emprunter.getProlongationEmprunt()));

            } else {
                emprunter.setDateRetourPrevue(dateRetourPrevue.getValue());
            }
            var emprunts = emprunterService.getEmpruntsByCarteEmprunteur(emprunteurField.getValue().getCarteEmprunteur());
            if (emprunts.size()>=10){
                Notification.show(
                    "Impossible : cet emprunteur a déjà 10 emprunts en cours.",
                    3000,
                    Notification.Position.MIDDLE
                );
                return;
            }
            emprunterService.saveEmprunt(emprunter);
            changeHandler.onChange();
        }
    }

    public final void editEmprunter(Emprunter e) {
        if (e == null) {
            setVisible(false);
            return;
        }
        emprunter = e;

        // Initialise les champs avec les valeurs existantes
        emprunteurField.setValue(emprunter.getCarteEmprunteur());
        documentField.setValue(emprunter.getIdDocument());
        dateEmprunt.setValue(emprunter.getDateEmprunt());
        dateRetourPrevue.setValue(emprunter.getDateRetourPrevue());
        dateRetourReelle.setValue(emprunter.getDateRetourReelle());
        prolongationEmprunt.setValue(
                emprunter.getProlongationEmprunt() != null ? emprunter.getProlongationEmprunt().toString() : "");

        setVisible(true);
        emprunteurField.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }

    public interface ChangeHandler {
        void onChange();
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