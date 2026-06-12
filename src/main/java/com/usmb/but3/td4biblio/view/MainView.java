package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.service.EvenementService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route
public final class MainView extends Main {

    public MainView (EvenementService evenementService) {
        addClassName(LumoUtility.Padding.MEDIUM);
        //add(new ViewToolbar("Main"));
        add(new Div("Choisissez une option dans le menu à gauche."));
        
        //les evenement
        add(new H2("Événements à venir"));

        VerticalLayout listeEvenements = new VerticalLayout();

        evenementService.getEvenementsFuturs().forEach(e -> {

            listeEvenements.add(
                new Div(
                    e.getDateDebut()
                    + " - "
                    + e.getTitre()
                )
            );

        });

        add(listeEvenements);
    }

    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}
