package com.todo.ui.gAuth.view;

import com.todo.ui.googleAuth.GoogleCalendareService;
import com.todom.library.dto.ApiResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

public class GoogleCalendarConnectionDialog extends VerticalLayout {

    private Button btnConnect= new Button("Connect");
    private Button btnDisconnect= new Button("Disconnect");
    private Button btnCancel= new Button("Cancel");
    private boolean isConnectd;
    private Consumer<String> redirectUrlConsumer;
   private Consumer<Boolean> isCancelableConsumer;
    private GoogleCalendareService googleCalendareService;
    private Integer userId;
    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarConnectionDialog.class);
    public GoogleCalendarConnectionDialog(boolean isConnectd,Consumer<String> redirectUrlConsumer,
                                          Consumer<Boolean> isCancelableConsumer,
                                          GoogleCalendareService googleCalendareService,Integer userId) {
        this.isConnectd = isConnectd;
        this.setPadding(false);
        this.setSpacing(false);
        this.getStyle().set("margin", "-15px");
        this.redirectUrlConsumer = redirectUrlConsumer;
        this.isCancelableConsumer = isCancelableConsumer;
        this.googleCalendareService = googleCalendareService;
        this.userId = userId;
        initComponent();
        eventListener();
    }

    private void eventListener() {

        btnConnect.addClickListener(e->{
            log.info("When click connect button is called");
            getRedirectUrl();
        });
    }

    private void getRedirectUrl() {
        ApiResponse response = this.googleCalendareService.getGoogleCalendarConnectUrl(this.userId);
        if(response!=null && response.getOther() !=null){
            Map<String, Object> urlMap = response.getOther();
            String url =String.valueOf(urlMap.get("url"));
            this.redirectUrlConsumer.accept(url);
        }else{
            Notification.show("No Google Calendar Connect URL");
        }
    }
    private void initComponent() {

        String connectedValue ="Connect";
        if(this.isConnectd){
            connectedValue = "Disconnect";
            this.btnConnect.setEnabled(false);
            this.btnDisconnect.setEnabled(true);
        }else{
            this.btnConnect.setEnabled(true);
            this.btnDisconnect.setEnabled(false);
        }

        String titleValue =String.format("Do you want to %s google calendar?",connectedValue);
        H3 title = new H3(titleValue);
        Image calendarIcon = new Image();
        calendarIcon.setSrc("images/google_calendar.svg");
        calendarIcon.getStyle().set("align-self","center");

        HorizontalLayout iconLayout = new HorizontalLayout();
        iconLayout.add(calendarIcon);

        btnConnect.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnDisconnect.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_PRIMARY);
        btnCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        HorizontalLayout buttonLayout = new HorizontalLayout(btnConnect,btnDisconnect,btnCancel);
        buttonLayout.setPadding(false);
        buttonLayout.getStyle().set("align-self","center");
        this.add(title, calendarIcon,buttonLayout);
    }
}
