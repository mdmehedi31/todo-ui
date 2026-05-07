package com.todo.ui.view;


import com.todo.ui.view.ai.TaskDashboardView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        // Create the Side Navigation component
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", DashboardOverviewView.class, VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Todos", TaskDashboardView.class, VaadinIcon.TASKS.create())); // Use your existing View class
        nav.addItem(new SideNavItem("TODO Ai", TodoGeneratorView.class, VaadinIcon.WORKPLACE.create()));
        nav.addItem(new SideNavItem("Event", EventsView.class, VaadinIcon.CALENDAR.create()));
        nav.addItem(new SideNavItem("Settings", "settings-view", VaadinIcon.COG.create()));

        // Add a header for the sidebar
        H1 logo = new H1("TODO App");
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "var(--lumo-space-m)");

        // Wrap navigation in a scroller for small screens
        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        // Add everything to the Drawer (Sidebar)
        addToDrawer(logo, scroller);

        // Optional: Add a toggle button to the navbar to hide/show sidebar
        addToNavbar(new DrawerToggle(), new Span("Task Management System"));

        // Set the sidebar to stay open by default
        setPrimarySection(Section.DRAWER);
    }
}