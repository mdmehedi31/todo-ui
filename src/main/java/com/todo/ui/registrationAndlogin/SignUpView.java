package com.todo.ui.registrationAndlogin;

import com.todo.ui.config.SecurityContext;
import com.todo.ui.dto.CurrentUser;
import com.todo.ui.endpoint.UserEndpoint;
import com.todo.ui.view.DashboardOverviewView;
import com.todom.library.dto.UserAuthInfo;
import com.todom.library.dto.UserDTO;
import com.todom.library.dto.UserRegistrationDTO;
import com.todom.library.enums.SuccessOrFailedEnum;
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
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("signup")
@PageTitle("TaskHub - AI Powered Sign Up")
@AnonymousAllowed
public class SignUpView extends Div {

    private UserEndpoint userEndpoint;
    private SecurityContext securityContext;
    private static final Logger log = LoggerFactory.getLogger(SignUpView.class);

    public SignUpView(UserEndpoint userEndpoint, SecurityContext securityContext) {

        this.userEndpoint = userEndpoint;
        this.securityContext = securityContext;
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

        TextField username = new TextField();
        username.setPlaceholder("Username");
        EmailField email = new EmailField();
        email.setRequired(true);
        email.setRequiredIndicatorVisible(true);
        email.setErrorMessage("Please enter a valid email address");
        PasswordField password = new PasswordField();
        password.setRequired(true);
        password.setRequiredIndicatorVisible(true);
        password.setErrorMessage("Please enter a valid password");

        // Use custom field groups to stop the overlapping issue
        Div nameGroup = createFieldGroup("Full Name", username);
        Div emailGroup = createFieldGroup("Email Address",email);
        Div passGroup = createFieldGroup("Create Password", password);

        Button signUpBtn = new Button("Get Started");
        signUpBtn.addClassName("login-btn");
        signUpBtn.setWidthFull();

        email.setValueChangeMode(ValueChangeMode.LAZY);
        email.addValueChangeListener(event -> {

            email.setInvalid(email.getValue()==null || email.getValue().isBlank());
        });

        password.setValueChangeMode(ValueChangeMode.LAZY);
        password.addValueChangeListener(event -> {

            password.setInvalid(password.getValue()==null || password.getValue().isBlank());
        });

        signUpBtn.addClickListener(e -> {

            if(email.getValue()!=null && !email.getValue().isEmpty() && password.getValue()!=null && !password.getValue().isBlank()) {
            UserRegistrationDTO    userRegistrationDTO = new UserRegistrationDTO();
            userRegistrationDTO.setEmail(email.getValue());
            userRegistrationDTO.setPassword(password.getValue());
            userRegistrationDTO.setFullName(username.getValue());

            UserAuthInfo userAuthInfo = this.userEndpoint.createUser(userRegistrationDTO);
            if (userAuthInfo != null) {
                if(userAuthInfo.getMessage().equalsIgnoreCase(SuccessOrFailedEnum.SUCCESS.name())){
                    redirectToDashboard(userAuthInfo);
                }else{
                    Notification.show(userAuthInfo.getMessage(), 3000, Notification.Position.MIDDLE);
                }
            }
            }

        });
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