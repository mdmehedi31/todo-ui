package com.todo.ui.view;

import com.todo.ui.googleAuth.GoogleCalendareService;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.TodoListResponseDTO;
import com.todom.library.dto.TodoResponseDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TodoDetailsView extends VerticalLayout {

    private final ITodoService todoService;
    private final Integer userId;
    private final TodoResponseDTO todoResponseDTO;
    private final GoogleCalendareService googleCalendareService;
    private final VerticalLayout listContainer = new VerticalLayout();
    private static final Logger log = LoggerFactory.getLogger(TodoDetailsView.class);

    public TodoDetailsView(ITodoService todoService, Integer userId, TodoResponseDTO todoResponseDTO,
                           GoogleCalendareService googleCalendareService) {
        this.todoService = todoService;
        this.userId = userId;
        this.todoResponseDTO = todoResponseDTO;
        this.googleCalendareService = googleCalendareService;

        addClassName("todo-details-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        loadAndInit();
    }

    private void loadAndInit() {
        // 1. Header Section
        addClassName("glass-details-pane");
        setPadding(false);
        setSpacing(false);
        setWidthFull();

        VerticalLayout header = new VerticalLayout();
        header.addClassName("details-sticky-header");
        header.setPadding(true);
        header.setSpacing(false);

        H3 title = new H3(todoResponseDTO.getTitle() != null ? todoResponseDTO.getTitle() : "Untitled Board");
        title.addClassName("details-board-title");

        HorizontalLayout metaActions = new HorizontalLayout();
        metaActions.setWidthFull();
        metaActions.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        metaActions.setAlignItems(FlexComponent.Alignment.CENTER);

        Span countBadge = new Span("Loading...");
        countBadge.addClassName("task-count-badge");

        Button btnAdd = new Button("Add Task", VaadinIcon.PLUS.create());
        btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnAdd.addClassName("cyber-button");
        btnAdd.addClickListener(e -> openAddDialog());

        metaActions.add(countBadge, btnAdd);
        header.add(title, metaActions);

        // 2. List Section (Scrollable)
        listContainer.addClassName("details-scroll-area");
        listContainer.setWidthFull();
        listContainer.setPadding(true);

        // Backend Call
        List<TodoListResponseDTO> items = this.todoService.getAllTodoListByTodoId(todoResponseDTO.getId());

        log.info("Task list size: " + items.size());
        if (items != null && items.size()>0) {
            countBadge.setText(items.size() + " Tasks");
            for (TodoListResponseDTO item : items) {
                listContainer.add(new TodoListItemRow(item, todoService, todoResponseDTO.getId(), userId, googleCalendareService));
            }
        } else {
            countBadge.setText("0 Tasks");
            Span emptyMsg = new Span("No tasks yet. Start by adding one!");
            emptyMsg.addClassName("empty-msg");
            listContainer.add(emptyMsg);
        }

        this.add(header, listContainer);
    }

    private void openAddDialog() {
        AddTodoItemDialog dialog = new AddTodoItemDialog(todoResponseDTO.getId(), todoService, null, null);
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        dialog.setWidth("500px");
        dialog.setHeight("170px");
        dialog.open();
    }

    public void addTodoItem(TodoListResponseDTO dto, Integer boardId) {
        listContainer.add(new TodoListItemRow(dto, todoService, boardId, userId, googleCalendareService));
    }

    public Integer getBoardId(){
        return todoResponseDTO.getId();
    }
}
