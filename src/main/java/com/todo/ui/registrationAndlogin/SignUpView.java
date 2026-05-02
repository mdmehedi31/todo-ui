package com.todo.ui.registrationAndlogin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("signup")
@PageTitle("TaskHub - AI Powered Sign Up")
@AnonymousAllowed
public class SignUpView extends Div {

    public SignUpView() {
        setSizeFull();
        addClassName("login-root");

        // Background Nebula Layers
        Div starsLayer = new Div();
        starsLayer.addClassName("stars-layer");
        Div nebula1 = new Div(); nebula1.addClassNames("nebula", "nebula-1");
        Div nebula2 = new Div(); nebula2.addClassNames("nebula", "nebula-2");
        Div nebula3 = new Div(); nebula3.addClassNames("nebula", "nebula-3");

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.addClassName("main-layout");
        mainLayout.setSizeFull();

        // ── LEFT PANEL ──
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.addClassName("left-panel");

        H1 brand = new H1("TaskHub");
        brand.addClassName("brand-title");

        Paragraph tagline = new Paragraph("Precision Task Management");
        tagline.addClassName("brand-tagline");

        Paragraph description = new Paragraph("Streamline backend workflows with AI-driven task generation.");
        description.addClassName("brand-description");

        leftPanel.add(brand, tagline, description);

        // ── MIDDLE PANEL (AI & Productivity Icons) ──
        Div iconContainer = new Div();
        iconContainer.addClassName("moving-icons-container");

        // AI specific icons: Sparkles (Magic), Brain (Intelligence), Bolt (Automation)
        Icon i1 = VaadinIcon.MAGIC.create(); i1.addClassNames("moving-icon", "icon-1");
        Icon i2 = VaadinIcon.CLUSTER.create(); i2.addClassNames("moving-icon", "icon-2");
        Icon i3 = VaadinIcon.BOLT.create(); i3.addClassNames("moving-icon", "icon-3");

        iconContainer.add(i1, i2, i3);

        // ── RIGHT PANEL (The Signup Card) ──
        Div card = new Div();
        card.addClassName("signup-card-container");

        H2 cardTitle = new H2("Create Account");
        cardTitle.addClassName("card-title");

        // Use custom field groups to stop the overlapping issue
        Div nameGroup = createFieldGroup("Full Name", new TextField());
        Div emailGroup = createFieldGroup("Email Address", new EmailField());
        Div passGroup = createFieldGroup("Create Password", new PasswordField());

        Button signUpBtn = new Button("Get Started");
        signUpBtn.addClassName("login-btn");
        signUpBtn.setWidthFull();

        HorizontalLayout loginRow = new HorizontalLayout();
        loginRow.addClassName("register-row");
        Span existing = new Span("Already a member? ");
        existing.addClassName("new-here-text");
        Anchor loginLink = new Anchor("", "Sign in");
        loginLink.addClassName("create-link");
        loginRow.add(existing, loginLink);

        card.add(cardTitle, nameGroup, emailGroup, passGroup, signUpBtn, loginRow);

        mainLayout.add(leftPanel, iconContainer, card);
        mainLayout.setFlexGrow(1, iconContainer);

        add(starsLayer, nebula1, nebula2, nebula3, mainLayout);
    }

    private Div createFieldGroup(String labelText, com.vaadin.flow.component.Component field) {
        Div group = new Div();
        group.addClassName("signup-field-group");

        NativeLabel label = new NativeLabel(labelText);
        label.addClassName("signup-field-label");

        field.getElement().setAttribute("class", "signup-input");
        field.getElement().getStyle().set("width", "100%");

        group.add(label, field);
        return group;
    }
}