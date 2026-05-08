package com.todo.ui.event;

import com.todom.library.dto.TodoListRequestDTO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

@DomEvent("add-todo-item-event")
public class AddTodoItemEvent extends ComponentEvent<Component> {

    private TodoListRequestDTO todoListResponseDTO;
    private Integer boardId;
    public AddTodoItemEvent(Component source, boolean fromClient,@EventData("todolist.event")TodoListRequestDTO todoListResponseDTO,
                            @EventData("boardId.event") Integer boardId) {
        super(source, fromClient);
        this.todoListResponseDTO = todoListResponseDTO;
        this.boardId = boardId;
    }

    public TodoListRequestDTO getTodoListResponseDTO() {
        return todoListResponseDTO;
    }

    public void setTodoListResponseDTO(TodoListRequestDTO todoListResponseDTO) {
        this.todoListResponseDTO = todoListResponseDTO;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }
}
