package com.todo.ui.view;

import com.todo.ui.event.AddTodoItemEvent;
import com.todo.ui.googleAuth.GoogleCalendareService;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.TodoResponseDTO;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class TodoCardParentView extends FlexLayout {

    private ITodoService todoService;
    private GoogleCalendareService googleCalendareService;
    private Integer userId;
    private TodoDetailsView todoDetailsView=null;
    private TodoCardsView cardsView;

    private static final Logger log = LoggerFactory.getLogger(TodoCardParentView.class);

    public TodoCardParentView(ITodoService iTodoService, Integer userId,GoogleCalendareService googleCalendareService) {
        this.todoService = iTodoService;
        this.userId = userId;
        this.googleCalendareService = googleCalendareService;

        initComponent();


        ComponentUtil.addListener(UI.getCurrent(), AddTodoItemEvent.class, evt->{
            if(this.todoDetailsView !=null) {
                if(evt !=null && evt.getBoardId()!=null && this.todoDetailsView.getBoardId().equals(evt.getBoardId())){
                    this.todoDetailsView.addTodoItem(evt.getTodoListResponseDTO(),evt.getBoardId());
                }
            }
        });
    }

    Consumer<TodoResponseDTO> todoResponseDTOConsumer = (todoResponseDTO) -> {
        log.info("Todo response consumer accepted");
        if (todoResponseDTO != null) {

            if (todoDetailsView != null && todoResponseDTO.getId().equals(todoDetailsView.getBoardId())) {
                log.info("Both todo id is same");
                todoDetailsView.removeFromParent();
                todoDetailsView = null;
            }else{
                log.info("Both todo id is not same");
                if(todoDetailsView !=null) {
                    todoDetailsView.removeFromParent();
                    todoDetailsView = null;
                }
                log.info("Todo details view value is : {}", todoDetailsView);
                todoDetailsView = new TodoDetailsView(todoService, userId, todoResponseDTO, this.googleCalendareService);
                this.add(todoDetailsView);
            }
        }/*else if(todoDetailsView !=null) {
            todoDetailsView.removeFromParent();
            todoDetailsView=null;
        }*/
    };

    private void initComponent() {
        cardsView= new TodoCardsView(todoService, 1,todoResponseDTOConsumer);
        this.add(cardsView);
    }


/*
    public TodoCardsView getCardsView() {
        return this.cardsView;
    }
*/

}
