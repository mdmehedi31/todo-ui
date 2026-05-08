package com.todo.ui.view;

import com.todo.ui.event.AddTodoItemEvent;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.TodoListRequestDTO;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class AddTodoItemDialog extends Dialog {

    private static final Logger log = LoggerFactory.getLogger(AddTodoItemDialog.class);
    private Integer todoBoardId;
    private ITodoService todoService;
    private TodoListRequestDTO todoListResponseDTO;
    private Consumer<TodoListRequestDTO> todoListUpdateResponseConsumer;
    private TextArea addTodoTextArea;
    private  Button addTodoButton;
    public AddTodoItemDialog(Integer todoBoardId,ITodoService todoService, TodoListRequestDTO todoListResponseDTO,
                             Consumer<TodoListRequestDTO> todoListUpdateResponseConsumer) {
        this.todoBoardId=todoBoardId;
        this.todoService=todoService;
        this.todoListResponseDTO=todoListResponseDTO;
        this.todoListUpdateResponseConsumer=todoListUpdateResponseConsumer;
        openAddChecklistDialog(this.todoBoardId);
        if(this.todoListResponseDTO !=null && this.todoListResponseDTO.getTitle() != null && !this.todoListResponseDTO.getTitle().isBlank()) {
           log.info("When todo list response dto is not null ");
            if(this.addTodoTextArea !=null){
                log.info("When addTodoTextArea is not null ");
                this.addTodoTextArea.setValue(todoListResponseDTO.getTitle());
            }
            if(addTodoButton!=null){
                log.info("When addTodoButton is not null ");
                addTodoButton.setText("Save");
            }
        }
    }

    private void openAddChecklistDialog(Integer todoBoardId){
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getStyle().set("padding","10px");
        Icon icon = VaadinIcon.INFO_CIRCLE.create();
        icon.getStyle().set("width","15px");
        icon.setTooltipText("Press enter for adding the new todo items");

        NativeLabel lblAddTodoItem = new NativeLabel("Add todo item");
        lblAddTodoItem.getStyle().set("margin-right","2px");
        Div lblDiv = new Div();
        lblDiv.add(lblAddTodoItem, icon);
        addTodoTextArea= new TextArea();
        addTodoTextArea.getStyle().set("padding-top","0px");
        Button btnCancel= new Button("Cancel");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        Icon closeIcon = VaadinIcon.CLOSE.create();
        btnCancel.setIcon(closeIcon);
        addTodoTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        addTodoTextArea.setWidth("100%");
        addTodoTextArea.setHeight("74px");
        HorizontalLayout buttonHrLayout = new HorizontalLayout();
        buttonHrLayout.setSpacing(true);
        buttonHrLayout.setWidth("100%");
        buttonHrLayout.setPadding(false);

        addTodoButton = new Button();
        addTodoButton.setText("Add");
        addTodoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING);
        buttonHrLayout.add(btnCancel,addTodoButton);
        buttonHrLayout.getStyle().set("align-self", "center");
        layout.add(lblDiv,addTodoTextArea,buttonHrLayout);
        this.add(layout);
        btnCancel.addClickListener(e->{
            this.close();
        });

        addTodoButton.addClickListener(e->{
            String todoListItem =addTodoTextArea.getValue();
            if(todoListItem !=null && !todoListItem.isBlank()){
                log.info("When text area value is not null... {}", todoListItem);
                boolean isAdded = addTodoItemToTheTodoBoard(todoListItem,todoBoardId);
                if(isAdded){
                    log.info("When is added is true");
                    addTodoTextArea.clear();
                    Notification.show("Added successfully..");
                }else{
                    Notification.show("Todo item not added");
                }
            }else{
                log.info("When text area value is null...: {}", addTodoTextArea.getValue());

            }
        });

        Shortcuts.addShortcutListener(addTodoTextArea,()->{

            String todoListItem =addTodoTextArea.getValue();
            if(todoListItem !=null && !todoListItem.isBlank()){
                log.info("When text area value is not null... {}", todoListItem);
                boolean isAdded = addTodoItemToTheTodoBoard(todoListItem,todoBoardId);
                if(isAdded){
                    log.info("When is added is true");
                    addTodoTextArea.clear();
                    this.close();
                    Notification.show("Added successfully..",1000, Notification.Position.MIDDLE);
                }else{
                    Notification.show("Todo item not added");
                }
            }else{
                log.info("When text area value is null...: {}", addTodoTextArea.getValue());

            }
        }, Key.ENTER);
    }

    private boolean addTodoItemToTheTodoBoard(String todoItem, Integer todoBoardId) {
        TodoListRequestDTO todoListRequestDTO = new TodoListRequestDTO();;
        if(this.todoListResponseDTO !=null && this.todoListResponseDTO.getId() !=null){
            todoListRequestDTO.setId(this.todoListResponseDTO.getId());
            if(todoItem !=null && !todoItem.isBlank()){
                todoListRequestDTO.setTitle(todoItem);
            }
        }else {
            String color = "#8cad95";
            todoListRequestDTO.setTodoId(todoBoardId);
            todoListRequestDTO.setTitle(todoItem);
            todoListRequestDTO.setCreatedBy(1);
            todoListRequestDTO.setTodoBgColor(color);
        }
        TodoListRequestDTO responseDTO = this.todoService.createOrUpdateTodoListItem(todoListRequestDTO);

        if (responseDTO != null && responseDTO.getId() != null) {
            log.info("Response id is {}", responseDTO.getId());

            if(todoListUpdateResponseConsumer!=null){
                todoListUpdateResponseConsumer.accept(responseDTO);
            }else{
                AddTodoItemEvent event = new AddTodoItemEvent(UI.getCurrent(),false,responseDTO,todoBoardId);
                ComponentUtil.fireEvent(UI.getCurrent(), event);
            }


            return true;
        }
        return false;
    }
}
