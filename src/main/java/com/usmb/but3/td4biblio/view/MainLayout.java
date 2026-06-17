package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.security.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.IconSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import static com.vaadin.flow.theme.lumo.LumoUtility.*;

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createHeader(), new Scroller(createSideNav()));

    }

    // ── Drawer : logo cliquable ───────────────────────────────────────────

    private Div createHeader() {
        var appLogo = VaadinIcon.CUBES.create();
        appLogo.addClassNames(TextColor.PRIMARY, IconSize.LARGE);

        var appName = new Span("Application : BiBlio Vaadin");
        appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

        var header = new Div(appLogo, appName);
        header.addClassNames(Display.FLEX, Padding.MEDIUM, Gap.MEDIUM, AlignItems.CENTER);
        header.getStyle().set("cursor", "pointer");
        header.addClickListener(e -> UI.getCurrent().navigate(MainView.class));
        return header;
    }

    // ── Drawer : nav filtrée selon le rôle ───────────────────────────────

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(Margin.Horizontal.MEDIUM);

        boolean isBib = SessionManager.isBibliothecaire();
        nav.addItem(new SideNavItem("Accueil", "", new Icon(VaadinIcon.HOME)));

        // Uniquement bibliothécaire
        if (isBib) {
            MenuConfiguration.getMenuEntries().forEach(entry -> {
                String path = entry.path();
                // Exclure les pages déjà ajoutées et les pages réservées
                if (!path.equals("") && !path.equals("livre")
                        && !path.equals("evenement") && !path.equals("login")) {
                    nav.addItem(createSideNavItem(entry));
                }
            });
        } else {
            // Toujours visibles
            nav.addItem(new SideNavItem("Livres", "livre", new Icon(VaadinIcon.BOOK)));
            nav.addItem(new SideNavItem("Événements", "evenement", new Icon(VaadinIcon.CALENDAR)));
            nav.addItem(new SideNavItem("Bibliothèques", "bibliotheque", new Icon(VaadinIcon.BUILDING)));
            nav.addItem(new SideNavItem("Documents", "document", new Icon(VaadinIcon.ARCHIVE)));
        }

        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(),
                    new Icon(menuEntry.icon()));
        }
        return new SideNavItem(menuEntry.title(), menuEntry.path());
    }

}