package com.todo.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.ParentLayout;
import jakarta.servlet.http.HttpServletResponse;

@ParentLayout(MainLayout.class)
public class NotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    private Span invalidPathDisplay;

    public NotFoundView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("not-found-view");

        // The Sliding Emoji Container
        Div slider = new Div();
        slider.addClassName("emoji-slider");
        Span emoji = new Span("🏃💨"); // Runner emoji
        slider.add(emoji);

        H1 errorType = new H1("404");
        errorType.addClassName("error-code-wacky");

        H2 oops = new H2("Yikes! You're off the map.");

        // This will show the actual bad URL
        invalidPathDisplay = new Span();
        invalidPathDisplay.addClassName("bad-path-text");

        Span description = new Span("We searched everywhere but couldn't find that place. Maybe a cat sat on your keyboard?");
        description.addClassName("small-muted-text-wacky");

        Button homeBtn = new Button("GET ME OUTTA HERE", e -> UI.getCurrent().navigate(DashboardOverviewView.class));
        homeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        homeBtn.getStyle().set("margin-top", "30px");

        add(slider, errorType, oops, invalidPathDisplay, description, homeBtn);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        // Capture the broken path from the URL
        String path = event.getLocation().getPath();
        invalidPathDisplay.setText("Invalid Path: /" + path);
        return HttpServletResponse.SC_NOT_FOUND;
    }
}