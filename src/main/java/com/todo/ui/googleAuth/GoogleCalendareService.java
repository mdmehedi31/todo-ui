package com.todo.ui.googleAuth;

import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserCalendarDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface GoogleCalendareService {
    public ApiResponse getGoogleCalendarConnectUrl(Integer userId);
    public ApiResponse saveUserGoogleCalendarConnectionCredential(String code, String userId);
    public Boolean isGoogleCalendarConnected(Integer userId);

}
