package com.todo.ui.view;

import com.todo.ui.gAuth.view.GoogleCalendarConnectionDialog;
import com.todo.ui.googleAuth.GoogleCalendareService;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.TodoListRequestDTO;
import com.todom.library.enums.SuccessOrFailedEnum;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class TodoListItemRow extends FlexLayout {

    private Checkbox checkbox= new Checkbox();
    private NativeLabel rowTitleLabel = new NativeLabel();
    private TodoListRequestDTO todoListResponseDTO;
    private static final Logger log = LoggerFactory.getLogger(TodoListItemRow.class);
    private Button btnDelete = new Button();
    private Button btnEdit = new Button();
    private Button btnCalendarSetup= new Button();

    private Integer boardId;
    private ITodoService todoService;
    private Integer userId;
    private GoogleCalendareService googleCalendareService;
    public TodoListItemRow(TodoListRequestDTO todoListResponseDTO, ITodoService todoService, Integer boardId,
                           Integer userId, GoogleCalendareService googleCalendareService) {
        this.todoListResponseDTO = todoListResponseDTO;
        this.todoService = todoService;
        this.boardId = boardId;
        this.userId = userId;
        this.googleCalendareService = googleCalendareService;

        addClassName("todo-item-row");
        setWidthFull();
        setAlignItems(Alignment.CENTER);

        initView();

        eventListener();
    }

    private void eventListener() {

        checkbox.addValueChangeListener(e -> {

            this.todoListResponseDTO.setIsDone(e.getValue());
            this.todoService.createOrUpdateTodoListItem(this.todoListResponseDTO);
        });
    }

    private void initView() {
        rowTitleLabel.setText(todoListResponseDTO.getTitle());
        rowTitleLabel.addClassName("todo-item-title");
        setFlexGrow(1, rowTitleLabel);

        setupIconButton(btnEdit, VaadinIcon.EDIT, "Edit item");
        setupIconButton(btnDelete, VaadinIcon.TRASH, "Delete todo item");
        setupIconButton(btnCalendarSetup, VaadinIcon.CALENDAR, "Setup schedule");

        Icon deleteIcon = VaadinIcon.TRASH.create();
        Icon editIcon = VaadinIcon.EDIT.create();
        Icon calendarSetupIcon = VaadinIcon.CALENDAR.create();

        setFlexGrow(1, rowTitleLabel);

        checkbox.setValue(todoListResponseDTO.getIsDone());

        btnEdit.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING);
        btnDelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        btnDelete.setIcon(deleteIcon);
        btnDelete.setTooltipText("Delete todo item");
        btnEdit.setIcon(editIcon);
        btnEdit.setTooltipText("Edit item");
        btnCalendarSetup.setIcon(calendarSetupIcon);
        btnCalendarSetup.setTooltipText("Setup schedule or goal range for this item");

        Consumer<Boolean> deleteTodoItemConsumer= (isdeleted)->{
          if(isdeleted){
               log.info("After delete item consumer");
              this.removeFromParent();
          }else{
              log.info("After delete item consumer when delete is failed");
              Notification.show("Todo  item delete failed");
          }
        };
        btnDelete.addClickListener(e->{
           log.info("When delete icon : {}, {}", todoListResponseDTO.getTitle(), todoListResponseDTO.getId());
           this.deleteCardItemFromBoard(todoListResponseDTO.getId(), deleteTodoItemConsumer);
        });

        Consumer<TodoListRequestDTO> todoListUpdateResponseConsumer= (todoListResponseDTO)->{
          if(todoListResponseDTO !=null && todoListResponseDTO.getId().equals(todoListResponseDTO.getId()) &&
          todoListResponseDTO.getTitle() !=null){
              rowTitleLabel.setText(todoListResponseDTO.getTitle());
          }
        };
        btnEdit.addClickListener(e->{
            log.info("Btn edit click : {}, {}", todoListResponseDTO.getTitle(), todoListResponseDTO.getId());
            AddTodoItemDialog addTodoItemDialog= new AddTodoItemDialog(this.boardId, todoService,todoListResponseDTO,todoListUpdateResponseConsumer);
            addTodoItemDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
            addTodoItemDialog.setWidth("500px");
            addTodoItemDialog.setHeight("170px");
            addTodoItemDialog.open();
        });

        btnCalendarSetup.addClickListener(e->{
           ApiResponse apiResponse = this.todoService.checkIsValidToSetupGoogleCalendar(userId);
           if(apiResponse.getMessage().equals(SuccessOrFailedEnum.SUCCESS)){

               Dialog dialog= new Dialog();
               Consumer<Boolean> isAdde=(added)->{
                 if(added){
                     dialog.close();
                 }
               };
               ScheduleSetupDialog scheduleSetupDialog=new ScheduleSetupDialog(todoService,this.boardId,this.userId,this.todoListResponseDTO.getId(),isAdde);


               dialog.setWidth("670px");
               dialog.add(scheduleSetupDialog);
               dialog.open();
                log.info("Google calendar is connected..: {}", apiResponse.getMessage());
           }else{
               log.info("Google calendar not connected...: {}", apiResponse.getMessage());
               openGoogleCalenderConnectDialog();
           }
        });
        HorizontalLayout actionButtons = new HorizontalLayout(btnEdit, btnDelete, btnCalendarSetup);
        actionButtons.setSpacing(false); // Removes gaps between the buttons
        actionButtons.addClassName("action-group");

        this.add(checkbox,rowTitleLabel,actionButtons);
    }

    private void setupIconButton(Button btn, VaadinIcon vIcon, String tooltip) {
        Icon icon = vIcon.create();
        icon.setSize("19px");
        btn.setIcon(icon);
        btn.setTooltipText(tooltip);
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        btn.addClassName("icon-btn-adjust");
    }

    private void  openGoogleCalenderConnectDialog() {
        Dialog dialog= new Dialog();
        dialog.setWidth("470px");
        Consumer<String> redirectUrlConsumer = (redirectUrl) -> {
            log.info("redirectUrlConsumer : {}", redirectUrl);
            if(redirectUrl !=null && !redirectUrl.isBlank()){
                log.info("redirect url is-> parent view {}", redirectUrl);
                dialog.close();
                UI.getCurrent().getPage().setLocation(redirectUrl);
            }else{
                log.info("Redirect url is empty");
            }
        };

        Consumer<Boolean> isCancelableConsumer = (isCancelable) -> {
            if(isCancelable){
                dialog.close();
            }
        };
        GoogleCalendarConnectionDialog calendarConnectionDialog = new GoogleCalendarConnectionDialog(false,redirectUrlConsumer,isCancelableConsumer,
                googleCalendareService, this.userId);
        dialog.add(calendarConnectionDialog);
        dialog.open();
    }

    private void deleteCardItemFromBoard(Integer todoItemId, Consumer<Boolean> cardItemIsDeleted) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Delete card");
        confirmDialog.setText("Are you sure want to delete this todo item?");
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
            ApiResponse response= this.todoService.deleteTodoList(todoItemId);
            if(response !=null && response.getMessage().equals(SuccessOrFailedEnum.SUCCESS)){
                cardItemIsDeleted.accept(true);
            }else{
                cardItemIsDeleted.accept(false);
            }
        });
    }
}
