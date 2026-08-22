package com.todo.ui.endpoint;


import com.todo.ui.restEndPointCaller.RestEndPointCaller;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserCalendarDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleCalendarEndpoint {

    private RestEndPointCaller restEndPointCaller;
    public GoogleCalendarEndpoint(RestEndPointCaller restEndPointCaller) {
        this.restEndPointCaller = restEndPointCaller;
    }

    public ApiResponse getRedirectUrl(Integer userId){
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userId", String.valueOf(userId));
        ApiResponse response = this.restEndPointCaller.getCallerWithPathVariable(GoogleCalendarEndpointPath.GOOGEL_AUTH_URL+"/"+userId, ApiResponse.class);
        return response;
    }

    public ApiResponse saveUserGoogleCalendarConnectionCredential(MultiValueMap<String, String> authMap){
        ApiResponse response = this.restEndPointCaller.postCallerWithRequestParams(GoogleCalendarEndpointPath.ADD_GOOGLE_CALENDAR_USER_CREDENTIAL,authMap,ApiResponse.class);
        return response;
    }


    public Boolean isGoogleCalendarConnected(Integer userId){
        Boolean isConnected = this.restEndPointCaller.getCallerWithPathVariable(GoogleCalendarEndpointPath.GOOGLE_CALENDAR_CONNECTED+"/"+userId, Boolean.class);
        return isConnected;
    }

}
