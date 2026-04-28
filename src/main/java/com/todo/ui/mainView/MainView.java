package com.todo.ui.mainView;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

/*@Layout("")*/
public class MainView extends AppLayout {

    public MainView() {

        addHeaderContent();
        addDrawerContent();

    }

    private void addDrawerContent() {

        FlexLayout layout = new FlexLayout();
        H3 title = new H3("My Plan Dot AI");
        layout.add(title);
        Header header = new Header(layout);
        Scroller scroller = new Scroller( addSideNav());
        scroller.getStyle().set("padding","13px");
        header.getStyle().set("padding","13px");
        addToDrawer(header, scroller);

    }

    private void addHeaderContent() {
        DrawerToggle drawerToggle = new DrawerToggle();
        setPrimarySection(Section.DRAWER);
        String viewTitle = MenuConfiguration.getPageHeader().orElse("");
        H1 header = new H1(viewTitle);
        header.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, drawerToggle, header);

    }

    private SideNav addSideNav() {
        SideNav sideNav = new SideNav();

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();

        menuEntries.forEach(m -> {

            if(m.icon()!=null){
                SideNavItem sideNavItem = new SideNavItem(m.title(),m.path(), new SvgIcon(m.icon()));
                sideNav.addItem(sideNavItem);
            }else{
                SideNavItem sideNavItem = new SideNavItem(m.title(),m.path());
                sideNav.addItem(sideNavItem);
            }
        });

        return sideNav;
    }
}
