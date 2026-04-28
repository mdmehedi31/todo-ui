package com.todo.ui.registrationAndlogin;

import com.todo.ui.view.EventsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

@Route("/signup")
public class RegistrationView extends VerticalLayout {

    private Card formCard = new Card();
    private static Logger log = LoggerFactory.getLogger(RegistrationView.class);

    public RegistrationView() {

        this.setPadding(false);
        this.setSpacing(false);
        this.setClassName("sign-up-body");
        this.getStyle().set("align-items", "center").set("justify-content", "center").set("height", "100vh");

        formCard.addClassNames("card-width-height","sign-up-card");
        this.add(formCard);
        initComp();
    }

    private void initComp() {
        addSignupView();
    }

    private void addSignupView(){

        Consumer<Boolean> submitConsumer = b -> {
            log.info("Sign up successful submit consumer");
            removeComponent();
            openChangeCodeValidationView();
        };
        Consumer<Boolean> cancelConsumer = b -> {
            log.info("Sign up successful cancel consumer");
        };
        RegistrationFormView registrationFormView = new RegistrationFormView(cancelConsumer, submitConsumer);
        removeComponent();

        formCard.add(registrationFormView);

    }

    private void removeComponent(){
       // formCard.removeClassNames("card-width-height","sign-up-card");
        formCard.removeAll();
    }

    private void openChangeCodeValidationView() {

        Consumer<Boolean> submitConsumer = b -> {
            log.info("Code validation submit consumer");
            removeComponent();
           changePasswordView();
        };

        Consumer<Boolean> cancelConsumer = b -> {
            log.info("Code validation cancel consumer");
            removeComponent();
            addSignupView();
        };

        SignupCodeValidationView signupCodeValidationView = new SignupCodeValidationView(cancelConsumer, submitConsumer);
        removeComponent();
        formCard.add(signupCodeValidationView);
    }

    private void changePasswordView() {

        Consumer<Map<String, String>> changedPasswordConsumer = b -> {
          log.info("Changed password consumer");
            if(b!=null){
                UI.getCurrent().navigate(EventsView.class);
            }else {

            }
        };

        Consumer<Boolean> skipConsumer = isSKiped -> {
            log.info("Skip password validation");
            if(isSKiped!=null && isSKiped){
                UI.getCurrent().navigate(EventsView.class);
            }else{
                log.info("Skipped change password view else");
            }
        };

        SignupPasswordChangeView signupPasswordChangeView = new SignupPasswordChangeView(skipConsumer, changedPasswordConsumer);
        removeComponent();
        formCard.add(signupPasswordChangeView);
    }
}
