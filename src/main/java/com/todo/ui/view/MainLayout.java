package com.todo.ui.view;


import com.todo.ui.config.SecurityContext;
import com.todo.ui.view.ai.TaskDashboardView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private Span viewTitle; // Class-level variable to update text

    private SecurityContext securityContext;

    public MainLayout(SecurityContext securityContext) {
        this.securityContext = securityContext;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addNavbarContent();
    }

    private void addNavbarContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new Span("TaskHub"); // Default title
        viewTitle.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.MEDIUM);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H2 appName = new H2("TaskHub");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", DashboardOverviewView.class, VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Todos", TaskDashboardView.class, VaadinIcon.LIST_UL.create()));
        nav.addItem(new SideNavItem("TODO Ai", TodoGeneratorView.class, VaadinIcon.AUTOMATION.create()));
        nav.addItem(new SideNavItem("Event", EventsView.class, VaadinIcon.CALENDAR.create()));
       // nav.addItem(new SideNavItem("Settings", "settings-path", VaadinIcon.COG.create()));

        Scroller scroller = new Scroller(nav);

        scroller.setClassName(LumoUtility.Padding.SMALL);

        Button btnLogout = new Button("Logout");
        btnLogout.setIcon(VaadinIcon.SIGN_OUT.create());
        btnLogout.addClickListener(e -> {

            log.info("Logout button clicked");
            if(securityContext!=null){
                securityContext.setCurrentUser(null);
                UI.getCurrent().navigate(HomeView.class);
            }
        });
        addToDrawer(appName, scroller, btnLogout);


    }

    // This method runs every time you switch views
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        viewTitle.setText(getCurrentPageTitle(event));
    }

    private String getCurrentPageTitle(BeforeEnterEvent event) {
        // Gets the @PageTitle annotation from the current view class
        PageTitle title = event.getNavigationTarget().getAnnotation(PageTitle.class);
        return title == null ? "TaskHub" : title.value();
    }
}

