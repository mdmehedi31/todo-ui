package com.todo.ui.registrationAndlogin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class SignupCodeValidationView extends VerticalLayout {


    private static Logger log = LoggerFactory.getLogger(SignupCodeValidationView.class);

    H3 lblTitle = new H3("Verify your registration code");
    Span lblCodeMessage = new Span("We have sent you a verification code to your provided email address");
    private TextField numValidCode= new TextField("Enter Valid Code");
    private Button submitButton = new Button("Submit");
    private Button cancelButton = new Button("Cancel");

    private Consumer<Boolean> cancelConsumer;
    private Consumer<Boolean> submitConsumer;
    public SignupCodeValidationView(Consumer<Boolean> cancelConsumer,Consumer<Boolean> submitConsumer) {

        this.cancelConsumer = cancelConsumer;
        this.submitConsumer = submitConsumer;
        this.setPadding(false);
        this.setSpacing(false);
        initComp();
        eventListener();
    }

    private void eventListener() {

        submitButton.addClickListener(e -> {
            submitConsumer.accept(true);
        });

        cancelButton.addClickListener(e -> {
            cancelConsumer.accept(true);
        });
    }

    private void initComp() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        numValidCode.setPlaceholder("Enter Valid Code");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        //buttonLayout.setSpacing(false);
        buttonLayout.setPadding(false);
        buttonLayout.setAlignItems(Alignment.CENTER);
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_WARNING);
        cancelButton.addClassNames("cancel-btn");
        buttonLayout.add(submitButton,cancelButton);
        buttonLayout.getStyle().set("align-self","center");
        numValidCode.setMaxLength(6);
        numValidCode.setAllowedCharPattern("[0-9]");
        submitButton.setEnabled(false);
        numValidCode.getStyle().set("align-self", "center");
        numValidCode.addValueChangeListener(e->{
           if (e.isFromClient() && !e.getSource().isInvalid()){
               submitButton.setEnabled(true);
           }else{
               submitButton.setEnabled(false);
           }
        });

        lblTitle.getStyle().set("align-self", "center");
        layout.add(lblTitle, lblCodeMessage, numValidCode,buttonLayout);
        this.add(layout);
    }
}
