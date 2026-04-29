package com.todo.ui.endpoint;


import com.todo.ui.restEndPointCaller.RestEndPointCaller;
import com.todom.library.dto.TodoDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class AiGeneratorEndpoint {

    private RestEndPointCaller restEndPointCaller;
    public AiGeneratorEndpoint(RestEndPointCaller restEndPointCaller) {
        this.restEndPointCaller = restEndPointCaller;
    }


    public TodoDTO generateAndGetTodoList(String message){
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("message", message);
        TodoDTO response = restEndPointCaller.getCallerWithRequestParam(AIGENERATORENDPOINTPATH.GENERATE_TODO_PATH,form,TodoDTO.class);
        return response;
    }
}
