package com.usmb.but3.td4biblio.security;

import com.usmb.but3.td4biblio.entity.Bibliothecaire;
import com.usmb.but3.td4biblio.entity.Emprunteur;
import com.vaadin.flow.server.VaadinSession;

/**
 * Gestion de la session utilisateur via VaadinSession.
 */
public class SessionManager {

    private static final String KEY_EMPRUNTEUR     = "session_emprunteur";
    private static final String KEY_BIBLIOTHECAIRE = "session_bibliothecaire";
    private static final String KEY_ROLE           = "session_role";

    private SessionManager() {}

    public static void loginEmprunteur(Emprunteur e) {
        VaadinSession.getCurrent().setAttribute(KEY_EMPRUNTEUR, e);
        VaadinSession.getCurrent().setAttribute(KEY_BIBLIOTHECAIRE, null);
        VaadinSession.getCurrent().setAttribute(KEY_ROLE, "EMPRUNTEUR");
    }

    public static void loginBibliothecaire(Bibliothecaire b) {
        VaadinSession.getCurrent().setAttribute(KEY_BIBLIOTHECAIRE, b);
        VaadinSession.getCurrent().setAttribute(KEY_EMPRUNTEUR, null);
        VaadinSession.getCurrent().setAttribute(KEY_ROLE, "BIBLIOTHECAIRE");
    }

    public static Emprunteur getEmprunteur() {
        return (Emprunteur) VaadinSession.getCurrent().getAttribute(KEY_EMPRUNTEUR);
    }

    public static Bibliothecaire getBibliothecaire() {
        return (Bibliothecaire) VaadinSession.getCurrent().getAttribute(KEY_BIBLIOTHECAIRE);
    }

    public static String getRole() {
        Object r = VaadinSession.getCurrent().getAttribute(KEY_ROLE);
        return r != null ? r.toString() : null;
    }

    public static boolean isLoggedIn() {
        return getRole() != null;
    }

    public static boolean isEmprunteur() {
        return "EMPRUNTEUR".equals(getRole());
    }

    public static boolean isBibliothecaire() {
        return "BIBLIOTHECAIRE".equals(getRole());
    }

    public static String getDisplayName() {
        if (isEmprunteur() && getEmprunteur() != null) {
            return getEmprunteur().getPrenom() + " " + getEmprunteur().getNom();
        }
        if (isBibliothecaire() && getBibliothecaire() != null) {
            return getBibliothecaire().getPrenom() + " " + getBibliothecaire().getNom();
        }
        return "";
    }

    public static void logout() {
        VaadinSession.getCurrent().setAttribute(KEY_EMPRUNTEUR, null);
        VaadinSession.getCurrent().setAttribute(KEY_BIBLIOTHECAIRE, null);
        VaadinSession.getCurrent().setAttribute(KEY_ROLE, null);
    }
}