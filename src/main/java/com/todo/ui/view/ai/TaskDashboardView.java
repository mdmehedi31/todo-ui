package com.todo.ui.view.ai;


import com.todo.ui.todoService.ITodoService;
import com.todo.ui.view.AddTodoItemDialog;
import com.todo.ui.view.AddTodoPopup;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.TodoResponseDTO;
import com.todom.library.enums.SuccessOrFailedEnum;
import com.todom.library.utils.DateTimeUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

@Route("tast-dashboard")
@PageTitle("TaskHub - Workspace")
@PermitAll
public class TaskDashboardView extends Div {

    private ITodoService todoService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private Integer userId=1;
    public TaskDashboardView(ITodoService todoService) {
        this.todoService = todoService;
        setSizeFull();
        addClassName("dashboard-root");

        // ── Reuse your Space Background ──
        Div stars = new Div();
        stars.addClassName("stars-layer");
        Div nebula = new Div(); nebula.addClassNames("nebula", "nebula-main");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        content.addClassName("dashboard-container");

        // ── TOP HEADER (Branding & Global Actions) ──
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("dashboard-header");
        header.setWidthFull();

        H2 logo = new H2("TaskHub");
        logo.addClassName("dashboard-logo");

        HorizontalLayout topActions = new HorizontalLayout();
        Button addBtn = new Button("Add Todo", VaadinIcon.PLUS.create());
        addBtn.addClassName("primary-action-btn");

        Button googleBtn = new Button("Sync Google", VaadinIcon.GOOGLE_PLUS.create());
        googleBtn.addClassName("secondary-action-btn");

        topActions.add(addBtn, googleBtn);
        header.add(logo, topActions);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN.BETWEEN);

        // ── TASK GRID ──
        Div grid = new Div();
        grid.addClassName("task-grid");

        // Mock data matching image_a5c990.jpg
        /*grid.add(new TaskCard("Environment Setup for project", "29.09.2025", "05.10.2025", "10 Todos", 0));
        grid.add(new TaskCard("Create Product CRUD API", "29.09.2025", "05.10.2025", "10 Todos", 45));
        grid.add(new TaskCard("DB setup should be first...", "29.09.2025", "05.10.2025", "10 Todos", 100));
        grid.add(new TaskCard("New project environment", "07.10.2025", "31.10.2025", "10 Todos", 20));
        grid.add(new TaskCard("New JPA Create", "29.09.2025", "05.10.2025", "10 Todos", 0));
        grid.add(new TaskCard("Add Product Listener", "29.09.2025", "05.10.2025", "10 Todos", 75));
        */
        List<TodoResponseDTO> todoResponseDTOList= this.todoService.getAllTodos(userId);
        if(todoResponseDTOList !=null && todoResponseDTOList.size()>0){
            for(TodoResponseDTO todoResponseDTO : todoResponseDTOList){

                String title ="";
                String startDate = "";
                String endDate = "";
                Integer totalTodoListItems =0;
                Integer progress=0;
                if(todoResponseDTO.getTitle()!=null && !todoResponseDTO.getTitle().isBlank()){
                    title = todoResponseDTO.getTitle();
                }

                if(todoResponseDTO.getGoalStartDate() !=null){
                    LocalDateTime startDateValue = DateTimeUtil.convertUTCTimeToLocalDateTime(todoResponseDTO.getGoalStartDate(), "Asia/Dhaka");

                    startDate=formatter.format(startDateValue);
                }

                if(todoResponseDTO.getGoalEndDate() !=null){
                    LocalDateTime endDateValue = DateTimeUtil.convertUTCTimeToLocalDateTime(todoResponseDTO.getGoalEndDate(), "Asia/Dhaka");
                    endDate=formatter.format(endDateValue);
                }

                if (todoResponseDTO.getTotalTodoListItem() !=null){
                    totalTodoListItems = todoResponseDTO.getTotalTodoListItem();
                }

                if(todoResponseDTO.getProgress() !=null){

                }
                TaskCard taskCard = new TaskCard(title, startDate, endDate, totalTodoListItems, progress);
                grid.add(taskCard);

                taskCard.getAddBtn().getElement().addEventListener("click", e->{
                    openAddChecklistDialog(todoResponseDTO.getId());
                }).stopPropagation();


                taskCard.getDeleteBtn().getElement().addEventListener("click", e->{
                    openAddChecklistDialog(todoResponseDTO.getId());
                }).stopPropagation();


                taskCard.getEditBtn().getElement().addEventListener("click", e->{
                    openAddChecklistDialog(todoResponseDTO.getId());
                }).stopPropagation();


            }
        }
        content.add(header, grid);
        add(stars, nebula, content);
    }

    private void openAddChecklistDialog(Integer todoBoardId) {

        AddTodoItemDialog addTodoItemDialog = new AddTodoItemDialog(todoBoardId, todoService, null, null);
        addTodoItemDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        addTodoItemDialog.setWidth("500px");
        addTodoItemDialog.setHeight("170px");
        addTodoItemDialog.open();
    }

    private void deleteTodoBoard(Integer todoBoardId, Consumer<Boolean> isDeletedConsumer){
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Delete card");
        confirmDialog.setText("Are you sure want to delete this board?");
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.open();
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelButtonTheme(ButtonVariant.LUMO_PRIMARY.name());
        confirmDialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.name());
        confirmDialog.addCancelListener(cancel->{
            confirmDialog.close();
        });
        confirmDialog.addConfirmListener(e->{
            ApiResponse response= this.todoService.deleteTodo(todoBoardId);
            if(response !=null && response.getMessage().equals(SuccessOrFailedEnum.SUCCESS)){
                isDeletedConsumer.accept(true);
            }else{
                isDeletedConsumer.accept(false);
            }
        });
    }

    private void openEditDialog(TodoResponseDTO todoResponse, Consumer<TodoResponseDTO> updateConsumer){
        Dialog dialog = new Dialog();
        Consumer<Boolean> dialogCloseConsumer = (e) -> {
            //log.info("Dialog closed consumer");
            dialog.close();
        };
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        dialog.setWidth("500px");
        dialog.setHeight("205px");
        AddTodoPopup addTodoPopup = new AddTodoPopup(todoService, dialogCloseConsumer,todoResponse,updateConsumer);
        dialog.add(addTodoPopup);
        dialog.open();


    }
}