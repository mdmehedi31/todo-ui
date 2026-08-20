package com.todo.ui.googleAuth;

import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserCalendarDto;

import java.util.List;
import java.util.Map;

public interface GoogleCalendareService {
    public ApiResponse getGoogleCalendarConnectUrl(Integer userId);
    public ApiResponse saveUserGoogleCalendarConnectionCredential(String code, String userId);

}
