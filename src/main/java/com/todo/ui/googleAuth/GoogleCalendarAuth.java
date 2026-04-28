package com.todo.ui.googleAuth;


import com.todo.ui.endpoint.GoogleCalendarEndpoint;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserCalendarDto;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
public class GoogleCalendarAuth implements GoogleCalendareService {

    private GoogleCalendarEndpoint googleCalendarEndpoint;

    public GoogleCalendarAuth(GoogleCalendarEndpoint googleCalendarEndpoint) {
        this.googleCalendarEndpoint = googleCalendarEndpoint;
    }

    @Override
    public Map<String, String> getGoogleCalendarConnectUrl(Integer userId){
        return googleCalendarEndpoint.getRedirectUrl(userId);
    }

    @Override
    public ApiResponse saveUserGoogleCalendarConnectionCredential(String code, String userId) {
        MultiValueMap<String, String> credentialsValue = new LinkedMultiValueMap<>();
        credentialsValue.add("code", code);
        credentialsValue.add("userId", userId);
        credentialsValue.add("email","hello@email.com");
        return googleCalendarEndpoint.saveUserGoogleCalendarConnectionCredential(credentialsValue);
    }
}
