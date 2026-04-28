package com.todo.ui.event;

import com.todom.library.dto.TodoResponseDTO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

@DomEvent("add-todo-model-event")
public class AddTodoModelEvent extends ComponentEvent<Component>{

    private static final long serialVersionUID = 1L;
    private TodoResponseDTO todoResponseDTO;
    public AddTodoModelEvent(Component source, boolean fromClient,@EventData("event.todoResponseDto") TodoResponseDTO todoResponseDTO) {
        super(source,fromClient);
        this.todoResponseDTO = todoResponseDTO;
    }

    public TodoResponseDTO getTodoResponseDTO() {
        return todoResponseDTO;
    }

    public void setTodoResponseDTO(TodoResponseDTO todoResponseDTO) {
        this.todoResponseDTO = todoResponseDTO;
    }
}
