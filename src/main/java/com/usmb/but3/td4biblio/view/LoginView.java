package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.service.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView(AuthService authService) {

        TextField username =
                new TextField("Nom d'utilisateur");

        PasswordField password =
                new PasswordField("Mot de passe");

        Button connexion =
                new Button("Connexion");

        connexion.addClickListener(event -> {

            String role =
                    authService.login(
                            username.getValue(),
                            password.getValue());

            if ("BIBLIOTHECAIRE".equals(role)) {

                UI.getCurrent().navigate("admin");

            } else if ("EMPRUNTEUR".equals(role)) {

                UI.getCurrent().navigate("emprunteur");

            } else {
                Notification.show(
                        "Identifiant ou mot de passe invalide",
                        3000,
                        Notification.Position.MIDDLE
                );
            }
        });

        add(username, password, connexion);
    }
}

