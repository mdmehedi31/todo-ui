package com.todo.ui.view;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("")
@PageTitle("TaskHub - Sign In")
@AnonymousAllowed
public class HomeView extends Div {

    public HomeView() {
        setSizeFull();
        addClassName("login-root");


        // Stars background layer
        Div starsLayer = new Div();
        starsLayer.addClassName("stars-layer");

        // Animated nebula blobs
        Div nebula1 = new Div();
        nebula1.addClassNames("nebula"," nebula-1");
        Div nebula2 = new Div();
        nebula2.addClassNames("nebula","nebula-2");
        Div nebula3 = new Div();
        nebula3.addClassNames("nebula","nebula-3");

        // Main content wrapper
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.addClassName("main-layout");
        mainLayout.setSizeFull();

        // ── LEFT PANEL ──────────────────────────────────────────
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.addClassName("left-panel");
        leftPanel.setSpacing(false);
        leftPanel.setPadding(false);

        H1 brand = new H1("TaskHub");
        brand.addClassName("brand-title");

        Paragraph tagline = new Paragraph("Precision Task Management");
        tagline.addClassName("brand-tagline");

        Paragraph description = new Paragraph(
                "Streamline backend workflows with AI-driven task generation."
        );
        description.addClassName("brand-description");

        // Feature items
        VerticalLayout features = new VerticalLayout();
        features.addClassName("features-list");
        features.setSpacing(false);
        features.setPadding(false);

        features.add(
                createFeatureItem(VaadinIcon.GRID_BIG, "Enterprise Boards",
                        "Scalable backend architecture."),
                createFeatureItem(VaadinIcon.MAGIC, "AI Task Engine",
                        "Automated roadmap generation."),
                createFeatureItem(VaadinIcon.SHIELD, "Secure Protocol",
                        "Integrated OAuth 2.0 security.")
        );

        leftPanel.add(brand, tagline, description, features);

        // ── RIGHT PANEL — SIGN IN CARD ───────────────────────────
        Div card = new Div();
        card.addClassName("signin-card");

        H2 cardTitle = new H2("Sign In");
        cardTitle.addClassName("card-title");

        // Email field
        Div emailGroup = new Div();
        emailGroup.addClassName("field-group");

        NativeLabel emailLabel = new NativeLabel("Email or Username");
        emailLabel.addClassName("field-label");

        TextField emailField = new TextField();
        emailField.addClassName("custom-text-field");
        emailField.setPlaceholder("Email or Username");
        emailField.setValue("mdsakibislam.mh35@mail.com");
        emailField.setWidthFull();

        emailGroup.add(emailLabel, emailField);

        // Password field
        Div passwordGroup = new Div();
        passwordGroup.addClassName("field-group");

        NativeLabel passwordLabel = new NativeLabel("Password");
        passwordLabel.addClassName("field-label");

        PasswordField passwordField = new PasswordField();
        passwordField.addClassName("custom-password-field");
        passwordField.setPlaceholder("Password");
        passwordField.setValue("password123");
        passwordField.setWidthFull();

        passwordGroup.add(passwordLabel, passwordField);

        // Login button
        Button loginBtn = new Button("Log In");
        loginBtn.addClassName("login-btn");
        loginBtn.setWidthFull();

        // Forgot password
        Anchor forgotLink = new Anchor("#", "Forgot password?");
        forgotLink.addClassName("forgot-link");

        Div forgotWrapper = new Div(forgotLink);
        forgotWrapper.addClassName("forgot-wrapper");

        // Create account
        HorizontalLayout registerRow = new HorizontalLayout();
        registerRow.addClassName("register-row");
        registerRow.setSpacing(false);

        Span newHere = new Span("New here?  ");
        newHere.addClassName("new-here-text");

        Anchor createLink = new Anchor("#", "Create account");
        createLink.addClassName("create-link");

        registerRow.add(newHere, createLink);

        card.add(cardTitle, emailGroup, passwordGroup, loginBtn, forgotWrapper, registerRow);

        mainLayout.add(leftPanel, card);

        add(starsLayer, nebula1, nebula2, nebula3, mainLayout);
    }

    // ── Helper: feature row ───────────────────────────────────
    private HorizontalLayout createFeatureItem(VaadinIcon iconType,
                                               String title, String subtitle) {
        HorizontalLayout row = new HorizontalLayout();
        row.addClassName("feature-item");
        row.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);

        Div iconBox = new Div();
        iconBox.addClassName("feature-icon-box");
        Icon icon = iconType.create();
        icon.addClassName("feature-icon");
        iconBox.add(icon);

        VerticalLayout text = new VerticalLayout();
        text.addClassName("feature-text");
        text.setSpacing(false);
        text.setPadding(false);

        Span titleSpan = new Span(title);
        titleSpan.addClassName("feature-title");

        Span subtitleSpan = new Span(subtitle);
        subtitleSpan.addClassName("feature-subtitle");

        text.add(titleSpan, subtitleSpan);
        row.add(iconBox, text);
        return row;
    }
}