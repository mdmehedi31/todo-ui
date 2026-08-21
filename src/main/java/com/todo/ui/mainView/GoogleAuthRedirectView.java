package com.todo.ui.mainView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.ui.googleAuth.GoogleCalendareService;
import com.todo.ui.view.DashboardOverviewView;
import com.todom.library.dto.ApiResponse;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Route("oauth2/callback/google")
public class GoogleAuthRedirectView extends VerticalLayout implements BeforeEnterObserver {

    private GoogleCalendareService googleCalendareService;
    private static final Logger log = LoggerFactory.getLogger(GoogleAuthRedirectView.class);
    public GoogleAuthRedirectView(GoogleCalendareService googleCalendareService) {
        this.googleCalendareService = googleCalendareService;
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        Map<String, List<String>> parameters = queryParameters.getParameters();
        String code =null;
        List<String> codeList = parameters.get("code");
        List<String> stateList = parameters.get("state");
        String email="";
        String userId = "";
        if(codeList !=null && !codeList.isEmpty()){
        code = codeList.get(0);
        }

        if(stateList !=null && !stateList.isEmpty()){
            String stateValue = stateList.get(0);
            byte[] stateDecoder =Base64.getUrlDecoder().decode(stateValue);

            String decodedStateStaring = new String(stateDecoder, StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            try{
                JsonNode jsonNode = objectMapper.readTree(decodedStateStaring);
                userId= jsonNode.get("userId").asText();
                email = jsonNode.get("email").asText();
            } catch (Exception e) {
                log.info("Throw an exception when parsing the string to object, ",e);
            }

            ApiResponse response= this.googleCalendareService.saveUserGoogleCalendarConnectionCredential(code,userId);
            if (response != null) {
                Map<String, Object> others = response.getOther();
                if (others != null) {
                    log.info("Save connection credential message is : {}, {}, {}, {}", response.getMessage(), others.get("accessToken"), others.get("refreshToken"), others.get("expiredAt"));
                    UI.getCurrent().navigate(DashboardOverviewView.class);
                } else {
                    log.info("Others is null...");
                }
            }
        }
    }
}
