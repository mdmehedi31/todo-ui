package com.todo.ui.view;

import com.todo.ui.googleAuth.GoogleCalendareService;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.TodoListResponseDTO;
import com.todom.library.dto.TodoResponseDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TodoDetailsView extends VerticalLayout {

    private ITodoService todoService;
    private Integer userId;
    private TodoResponseDTO todoResponseDTO;
    private static final Logger log= LoggerFactory.getLogger(TodoDetailsView.class);
    VerticalLayout layout = new VerticalLayout();
    private Button btnAdd = new Button("Add todos");
    private GoogleCalendareService googleCalendareService;

    public TodoDetailsView(ITodoService todoService, Integer userId, TodoResponseDTO todoResponseDTO,
                           GoogleCalendareService googleCalendareService) {
        this.todoService = todoService;
        this.userId = userId;
        this.todoResponseDTO = todoResponseDTO;
        this.googleCalendareService = googleCalendareService;
        this.setPadding(false);
        this.setSpacing(false);
        loadAndInit();
    }

    private void loadAndInit() {
        log.info("In the loadAndInit method : "+todoResponseDTO.getId());


        List<TodoListResponseDTO> todoListResponseDTOList = this.todoService.getAllTodoListByTodoId(todoResponseDTO.getId());

        if(todoListResponseDTOList !=null && todoListResponseDTOList.size() > 0){
            log.info("When todo list size is grater then 0");
            for(TodoListResponseDTO todoListResponseDTO : todoListResponseDTOList) {
               // Checkbox todoCheckbox = new Checkbox(todoListResponseDTO.getTitle());
               // todoCheckbox.getElement().setAttribute("todoListId", todoListResponseDTO.getId()+"");
                TodoListItemRow todoListItemRow = new TodoListItemRow(todoListResponseDTO,todoService,todoResponseDTO.getId(), userId,googleCalendareService);
                layout.add(todoListItemRow);
            }
        }else{
            NativeLabel lblTitle = new NativeLabel();
            lblTitle.setText("No todo list for the board");
            layout.add(lblTitle);
            log.info("When todo list size is 0");
        }

        btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING);
        btnAdd.addClickListener(e->{
            if(todoResponseDTO !=null && todoResponseDTO.getId() != null){
                log.info("When add todo response dto is not null");
                AddTodoItemDialog addTodoItemDialog= new AddTodoItemDialog(todoResponseDTO.getId(), todoService,null,null);
                addTodoItemDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
                addTodoItemDialog.setWidth("500px");
                addTodoItemDialog.setHeight("170px");
                addTodoItemDialog.open();
            }else{
                log.info("When add todo response dto is null");
            }

        });

        HorizontalLayout divTitle = new HorizontalLayout();
        Image todoImage = new Image();
        todoImage.setSrc("images/check-list.png");
        divTitle.add(todoImage);
        Span detailsCardNameSpan = new Span();
        detailsCardNameSpan.getElement().getThemeList().add("badge success");
        detailsCardNameSpan.getStyle().set("height", "40px");
        detailsCardNameSpan.getStyle().set("margin-top", "4px");

        if(this.todoResponseDTO !=null && this.todoResponseDTO.getTitle() != null){
            detailsCardNameSpan.setText(todoResponseDTO.getTitle());
            divTitle.add(detailsCardNameSpan);
        }

        divTitle.add(btnAdd);

        this.add(divTitle,layout);
    }

    public void addTodoItem(TodoListResponseDTO todoListResponseDTO, Integer boardId) {
        TodoListItemRow todoListItemRow = new TodoListItemRow(todoListResponseDTO,todoService,boardId,userId,googleCalendareService);
        layout.add(todoListItemRow);
    }
    public Integer getBoardId(){
        return todoResponseDTO.getId();
    }
}
