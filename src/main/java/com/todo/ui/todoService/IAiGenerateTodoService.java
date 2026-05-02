package com.todo.ui.todoService;

import com.todom.library.dto.TodoDTO;

public interface IAiGenerateTodoService {

    public TodoDTO generateAiTodo(String message);
}
