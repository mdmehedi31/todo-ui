package com.todo.ui.todoService;

import com.todo.ui.endpoint.AiGeneratorEndpoint;
import com.todom.library.dto.TodoDTO;
import org.springframework.stereotype.Service;

@Service
public class AiGenerateTodoService implements IAiGenerateTodoService{

    private AiGeneratorEndpoint aiGeneratorEndpoint;

    public AiGenerateTodoService(AiGeneratorEndpoint aiGeneratorEndpoint) {
        this.aiGeneratorEndpoint = aiGeneratorEndpoint;
    }

    @Override
    public TodoDTO generateAiTodo(String message) {
        return this.aiGeneratorEndpoint.generateAndGetTodoList(message);
    }
}
