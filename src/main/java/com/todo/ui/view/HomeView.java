package com.todo.ui.view;


import com.todo.ui.config.SecurityContext;
import com.todo.ui.dto.CurrentUser;
import com.todo.ui.endpoint.UserEndpoint;
import com.todom.library.dto.UserAuthInfo;
import com.todom.library.dto.UserDTO;
import com.todom.library.dto.UserLoginDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;

@Route("")
@PageTitle("TaskHub - Sign In")
@AnonymousAllowed
public class HomeView extends Div {

    private UserEndpoint userEndpoint;
    private SecurityContext securityContext;

    private static final Logger log = LoggerFactory.getLogger(HomeView.class);
    public HomeView(UserEndpoint userEndpoint,SecurityContext securityContext) {

        this.userEndpoint = userEndpoint;
        this.securityContext = securityContext;

        setSizeFull();
        addClassName("login-root");
        findUserTimeZone();

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

        EmailField emailField = new EmailField();
        emailField.addClassName("custom-text-field");
        emailField.setPlaceholder("Email or Username");
        emailField.setValue("mdsakibislam.mh35@mail.com");
        emailField.setWidthFull();
        emailField.setErrorMessage("Email Can't be empty");
        emailField.setRequiredIndicatorVisible(true);
        emailField.setRequired(true);

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
        passwordField.setErrorMessage("Password Can't be empty");
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.setRequired(true);


        passwordGroup.add(passwordLabel, passwordField);

        // Login button
        Button loginBtn = new Button("Log In");
        loginBtn.addClassName("login-btn");
        loginBtn.setWidthFull();

        loginBtn.addClickListener(e->{

            if(emailField.getValue()!=null && passwordField.getValue()!=null) {

                UserLoginDTO userLoginDTO= new UserLoginDTO();
                userLoginDTO.setEmail(emailField.getValue());
                userLoginDTO.setPassword(passwordField.getValue());
                log.info("Now, Trying to authenticate user");

                UserAuthInfo response = userEndpoint.login(userLoginDTO);
                if(response!=null) {
                    log.info("Response get");
                    if(response.getMessage() != null) {
                        log.info("Message is not null : "+response.getMessage());
                        Notification.show(response.getMessage());
                    }else{
                        log.info("User authenticated by users");
                        redirectToDashboard(response);
                    }
                }else{
                    log.info("Login Failed :: Parts :::");
                    Notification.show("Login Failed");
                }
            }else{
                emailField.setInvalid(true);
                passwordField.setInvalid(true);
            }

        });

        emailField.setValueChangeMode(ValueChangeMode.EAGER);
        emailField.addValueChangeListener(event -> {
           emailField.setInvalid(false);
        });

        passwordField.setValueChangeMode(ValueChangeMode.EAGER);
        passwordField.addValueChangeListener(event -> {
            passwordField.setInvalid(false);
        });
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

    private void redirectToDashboard(UserAuthInfo response) {

        if(response.getUser() !=null) {
            UserDTO userDTO = response.getUser();

            CurrentUser currentUser = new CurrentUser();
            currentUser.setEmail(userDTO.getEmail());
            currentUser.setUserId(userDTO.getId());
            if (userDTO.getFullName() != null) {

                currentUser.setUserName(userDTO.getFullName());
            }
            currentUser.setJwtToken(response.getJwtToken());
            if (userDTO.getPhoneNo() != null) {
                currentUser.setPhoneNo(userDTO.getPhoneNo());
            }

            securityContext.setCurrentUser(currentUser);

            if(securityContext.isAuthenticated()){
                log.info("When user is authenticated,  now redirecting to Dashboard");
                UI.getCurrent().navigate(DashboardOverviewView.class);
            }

        }else {
            Notification.show("Can not login, please contact with admin");
        }

    }

    public void findUserTimeZone() {
        UI.getCurrent().getPage().executeJs(
                "return Intl.DateTimeFormat().resolvedOptions().timeZone"
        ).then(String.class, zone -> {
            System.out.println("User Timezone is: " + zone);
            // Store this in your user session
            VaadinSession.getCurrent().setAttribute("user-tz", ZoneId.of(zone));
        });
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