package com.todo.ui.registrationAndlogin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SignupPasswordChangeView extends VerticalLayout {

    private static Logger log = LoggerFactory.getLogger(SignupPasswordChangeView.class);
    private TextField txtCurrentPassword = new TextField("Current password");
    private PasswordField pfNewPassword = new PasswordField("New password");
    private PasswordField pfConfirmpassword = new PasswordField("Confirm password");
    private Button btnSubmit = new Button("Change password");
    private Button btnSkip= new Button("Skip");
    private Consumer<Boolean> skipConsumer;
    private Consumer<Map<String, String>> confirmConsumer;
    public SignupPasswordChangeView(Consumer<Boolean> skipConsumerm,Consumer<Map<String, String>> confirmConsumer) {

        this.skipConsumer = skipConsumerm;
        this.confirmConsumer = confirmConsumer;
        this.setPadding(false);
        this.setSpacing(false);
        initComp();
        eventListener();
    }

    private void eventListener() {

        btnSubmit.addClickListener(e -> {
           confirmConsumer.accept(new HashMap<String, String>() {{}});
        });


        btnSkip.addClickListener(e -> {
            skipConsumer.accept(false);
        });
    }

    private void initComp() {
        H3 lblTitle = new H3("Change your auto genereated password");

        txtCurrentPassword.setWidthFull();
        txtCurrentPassword.setEnabled(false);
        txtCurrentPassword.setValue("abcdef");
        pfNewPassword.setWidthFull();
        pfConfirmpassword.setWidthFull();
        btnSubmit.setWidthFull();
        btnSubmit.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnSkip.setWidthFull();
        btnSkip.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        txtCurrentPassword.addClassNames("text-field-margin-top");
        pfNewPassword.addClassNames("text-field-margin-top");
        pfConfirmpassword.addClassNames("text-field-margin-top");
        pfConfirmpassword.setManualValidation(true);
        pfNewPassword.setManualValidation(true);
        btnSubmit.setEnabled(false);

        pfNewPassword.setValueChangeMode(ValueChangeMode.EAGER);
        pfNewPassword.addValueChangeListener(e->{

            if(!pfConfirmpassword.getValue().equals(pfNewPassword.getValue())) {
                pfNewPassword.setErrorMessage("Password not match with confirm password");
            }else{
                btnSubmit.setEnabled(true);
                pfNewPassword.setErrorMessage("");
            }
        });

        pfConfirmpassword.setValueChangeMode(ValueChangeMode.EAGER);
        pfConfirmpassword.addValueChangeListener(e->{
           if(!pfConfirmpassword.getValue().equals(pfNewPassword.getValue())) {
               pfConfirmpassword.setErrorMessage("Password not match with new password");
           }else{
               btnSubmit.setEnabled(true);
               pfConfirmpassword.setErrorMessage("");
           }
        });
        this.add(lblTitle, txtCurrentPassword, pfNewPassword, pfConfirmpassword, btnSubmit, btnSkip);
    }
}
