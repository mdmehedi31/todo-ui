package com.todo.ui.view.ai;


import com.todo.ui.googleAuth.GoogleCalendareService;
import com.todo.ui.todoService.ITodoService;
import com.todo.ui.view.AddTodoItemDialog;
import com.todo.ui.view.AddTodoPopup;
import com.todo.ui.view.TodoCardsView;
import com.todo.ui.view.TodoDetailsView;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

@Route("tast-dashboard")
@PageTitle("TaskHub - Workspace")
@PermitAll
public class TaskDashboardView extends Div {

    private ITodoService todoService;
    private GoogleCalendareService googleCalendareService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private Integer userId=1;
    private Div drawer;
    private Div overlay;
    private VerticalLayout drawerContent;
    private  Div grid = new Div();
    private SplitLayout splitLayout;
    private static final Logger log = LoggerFactory.getLogger(TaskDashboardView.class);

    public TaskDashboardView(ITodoService todoService,GoogleCalendareService googleCalendareService) {
        this.todoService = todoService;
        this.googleCalendareService = googleCalendareService;
        setSizeFull();
        addClassName("dashboard-root");

        Div stars = new Div();
        stars.addClassName("stars-layer");
        Div nebula = new Div(); nebula.addClassNames("nebula", "nebula-main");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        content.addClassName("dashboard-container");

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
        grid.addClassName("task-grid");
        loadTodoAndAddToGrid();
        overlay = new Div();
        overlay.addClassName("drawer-overlay");
        overlay.addClickListener(e -> closeDrawer());

        drawer = new Div();
        drawer.addClassName("side-drawer");

        drawerContent = new VerticalLayout();
        drawerContent.setPadding(true);
        drawer.add(drawerContent);

        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        splitLayout.setSplitterPosition(100);

        content.add(header, grid);
        Div detailsPlaceholder = new Div();

        splitLayout.addToPrimary(content);
        splitLayout.addToSecondary(detailsPlaceholder);

        add(stars, nebula, splitLayout);
    }

    private void loadTodoAndAddToGrid(){
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

                Consumer<Boolean> isDeletedCardConsumer =(isDeleted)->{
                    if(isDeleted){
                        taskCard.removeFromParent();
                    }else{
                        Notification.show("Board delete failed");
                    }
                };

                taskCard.getDeleteBtn().getElement().addEventListener("click", e->{
                    deleteTodoBoard(todoResponseDTO.getId(),isDeletedCardConsumer);
                }).stopPropagation();


                taskCard.addClickListener(e->{
                    openDrawer(todoResponseDTO);
                });

                Consumer<TodoResponseDTO> updateConsumer = (updatedTodoResponseDTO)->{
                    log.info("When update consumer accept");
                    getUI().ifPresent(ui->ui.access(()->{
                        if(updatedTodoResponseDTO !=null){
                            if(updatedTodoResponseDTO.getTitle() !=null && !updatedTodoResponseDTO.getTitle().isBlank()){
                                log.info("Title is not null"+updatedTodoResponseDTO.getTitle());
                                taskCard.setTaskTitle(updatedTodoResponseDTO.getTitle());
                            }
                            if(updatedTodoResponseDTO.getGoalStartDate() !=null){
                                LocalDateTime updatedStartDateValue = DateTimeUtil.convertUTCTimeToLocalDateTime(updatedTodoResponseDTO.getGoalStartDate(), "Asia/Dhaka");
                                log.info("updatedStartDateValue is "+updatedStartDateValue);
                                taskCard.setStartDate(formatter.format(updatedStartDateValue));
                            }
                            if(updatedTodoResponseDTO.getGoalEndDate() !=null) {
                                LocalDateTime updatedEndDateValue = DateTimeUtil.convertUTCTimeToLocalDateTime(updatedTodoResponseDTO.getGoalEndDate(), "Asia/Dhaka");
                                log.info("updatedEndDateValue is "+updatedEndDateValue);
                                taskCard.setEndDate(formatter.format(updatedEndDateValue));
                            }
                        }
                    }));
                };


                taskCard.getEditBtn().getElement().addEventListener("click", e->{

                    openEditDialog(todoResponseDTO,updateConsumer);
                }).stopPropagation();


            }
        }
    }

    private void closeDrawer() {
        drawer.removeClassName("open");
        overlay.removeClassName("visible");
    }

    private void openDrawer(TodoResponseDTO todo) {
        drawerContent.removeAll();

        H3 title = new H3(todo.getTitle());
        title.getStyle().set("color", "white").set("margin-bottom", "20px");

        TodoDetailsView taskDetailsView = new TodoDetailsView(todoService,userId,todo,this.googleCalendareService);

        splitLayout.addToSecondary(taskDetailsView);
        splitLayout.setSplitterPosition(70);
        /*
        drawer.add(taskDetailsView);
        drawer.addClassName("open");
        overlay.addClassName("visible");*/
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