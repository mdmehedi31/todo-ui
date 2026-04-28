package com.todo.ui.registrationAndlogin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class RegistrationFormView extends VerticalLayout {


    private static Logger log = LoggerFactory.getLogger(RegistrationFormView.class);

    private EmailField email = new EmailField("Email");
    private TextField txtUsername = new TextField("Username");
 //   private NativeLabel lblEmail = new NativeLabel("Email");
    private Span orSpan = new Span("Or");
    private Button btnSignup = new Button("Signup");
    private Button btnLoinWithGoogle = new Button("Login with Google");
    private Button btnCancel = new Button("Cancel");

    private Consumer<Boolean> cancelConsumer;
    private Consumer<Boolean> submitConsumer;

    public RegistrationFormView(Consumer<Boolean> cancelConsumer, Consumer<Boolean> submitConsumer) {
        this.cancelConsumer = cancelConsumer;
        this.submitConsumer = submitConsumer;
        this.setPadding(false);
        this.setSpacing(false);
        initComponent();
        eventListener();
    }

    private void eventListener() {

        email.addValueChangeListener(event -> {
            email.isInvalid();
        });

        email.addValidationStatusChangeListener(e->{
          if(e.getNewStatus()){
                btnSignup.setEnabled(false);
          }else{
              btnSignup.setEnabled(true);
          }
        });

        btnSignup.addClickListener(e -> {
            log.info("Signup clicked event listener");
           submitConsumer.accept(true);
        });

        btnCancel.addClickListener(e -> {
            log.info("Cancel clicked event listener");
            cancelConsumer.accept(false);
        });
    }

    private void initComponent() {

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setPadding(false);
        layout.setSpacing(false);

        email.setWidthFull();
        email.setRequiredIndicatorVisible(true);
        email.setPlaceholder("Enter your valid email address");
      email.addValueChangeListener(e->{
            if(e.isFromClient() && !e.getSource().isInvalid()){
                btnSignup.setEnabled(true);
            }else{
                btnSignup.setEnabled(false);
            }
        });

        btnSignup.setWidthFull();
        btnLoinWithGoogle.setWidthFull();
        btnSignup.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      //  btnCancel.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnCancel.setWidthFull();
        btnCancel.addClassNames("cancel-btn");
        H2 title = new H2("Create your account");
        Span subTitle = new Span("Explore our system which provide AI support");
        VerticalLayout titleLayout = new VerticalLayout(title, subTitle);
        titleLayout.setSpacing(false);
        titleLayout.getStyle().set("align-items", "center");
        titleLayout.getStyle().set("padding-top", "0px");
        Hr preHr = new Hr();
        preHr.addClassNames("hr-width");
        Hr postHr = new Hr();
        postHr.addClassNames("hr-width");
        HorizontalLayout orLayout = new HorizontalLayout();
        orLayout.setWidthFull();
        btnSignup.setEnabled(false);

        NativeLabel lblAlreadyAccount = new NativeLabel("Account already exists?");
        Anchor link = new Anchor("/login");
        link.setText("Login");
        link.getStyle().set("margin-left", "5px");
        HorizontalLayout alreadyHaveAccountLayout = new HorizontalLayout();
        alreadyHaveAccountLayout.setPadding(false);
        alreadyHaveAccountLayout.setSpacing(false);
        alreadyHaveAccountLayout.getStyle().set("align-self", "center");
        alreadyHaveAccountLayout.add(lblAlreadyAccount, link);

        orSpan.getStyle().set("margin","-6px");
        orLayout.add(preHr, orSpan, postHr);
        txtUsername.getStyle().set("margin-top", "-14px");
        txtUsername.getStyle().set("padding-top", "0px");
        txtUsername.setWidthFull();
        email.getStyle().set("margin-top", "-0px");
        email.getStyle().set("padding-top", "0px");
        layout.add(titleLayout, txtUsername,email, btnSignup,btnCancel, orLayout,btnLoinWithGoogle,alreadyHaveAccountLayout);
        this.add(layout);
    }
}
