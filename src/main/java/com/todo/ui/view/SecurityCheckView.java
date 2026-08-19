package com.todo.ui.view;


import com.todo.ui.config.SecurityContext;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityCheckView implements VaadinServiceInitListener {

    @Autowired
    private SecurityContext securityContext;

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {

        serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(e->{

              boolean isAuthenticated=  securityContext.isAuthenticated();
              if (!isAuthenticated) {
                  System.out.println("You are not authenticated");
                  e.forwardTo(HomeView.class);
              }
            });
        });
    }
}
