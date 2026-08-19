package com.todo.ui.endpoint;

import com.todo.ui.restEndPointCaller.RestEndPointCaller;
import com.todom.library.dto.UserAuthInfo;
import com.todom.library.dto.UserLoginDTO;
import com.todom.library.dto.UserRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public class UserEndpoint {

    private RestEndPointCaller restEndPointCaller;

    public UserEndpoint(RestEndPointCaller restEndPointCaller) {
        this.restEndPointCaller = restEndPointCaller;
    }


    public UserAuthInfo login(UserLoginDTO userLoginDTO) {
        UserAuthInfo userAuthInfo = restEndPointCaller.
                postCallerWithRequestBody(UserEndpointCaller.LOGIN_PATH,userLoginDTO,UserAuthInfo.class);
        return userAuthInfo;
    }

    public UserAuthInfo createUser(UserRegistrationDTO userRegistrationDTO) {
        UserAuthInfo userAuthInfo = restEndPointCaller.postCallerWithRequestBody(UserEndpointCaller.CREATE_USER_PATH,userRegistrationDTO,UserAuthInfo.class);
        return userAuthInfo;
    }


}
