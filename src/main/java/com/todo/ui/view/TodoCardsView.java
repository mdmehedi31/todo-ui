package com.todo.ui.view;

import com.todo.ui.event.AddTodoModelEvent;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.TodoListRequestDTO;
import com.todom.library.dto.TodoListResponseDTO;
import com.todom.library.dto.TodoResponseDTO;
import com.todom.library.enums.SuccessOrFailedEnum;
import com.todom.library.enums.TodoType;
import com.todom.library.utils.DateTimeUtil;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class TodoCardsView extends FlexLayout {

    private ITodoService todoService;
    private Integer userId;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final Logger log = LoggerFactory.getLogger(TodoCardsView.class);
    private Consumer<TodoResponseDTO> todoResponseDTOConsumer;
    public TodoCardsView(ITodoService todoService, Integer userId, Consumer<TodoResponseDTO> todoResponseDTOConsumer) {
        this.todoService = todoService;
        this.userId = userId;
        this.todoResponseDTOConsumer = todoResponseDTOConsumer;
        this.setFlexWrap(FlexWrap.WRAP);
        this.getStyle().set("gap", "1rem");
        loadTodoList();


        ComponentUtil.addListener(UI.getCurrent(), AddTodoModelEvent.class, evt->{
            if(evt !=null && evt.getTodoResponseDTO() != null) {
                log.info("When todo response dto is not null... ... ... ");
                buildTodoList(evt.getTodoResponseDTO());
            }else{
                log.info("When evt is null.... ... ....");
            }
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI.getCurrent();

    }

    private void loadTodoList() {
        List<TodoResponseDTO> todoResponseDTOList= this.todoService.getAllTodos(userId);
        if(todoResponseDTOList !=null && todoResponseDTOList.size()>0){
            for(TodoResponseDTO todoResponseDTO : todoResponseDTOList){
                buildTodoList(todoResponseDTO);
            }
        }
    }

    public void buildTodoList(TodoResponseDTO todoResponse) {

        Card card = new Card();

        card.getElement().addEventListener("click", e->{
           log.info("When click card in the card view and card id is : " + todoResponse.getId());
            todoResponseDTOConsumer.accept(todoResponse);
        });

        card.getElement().setAttribute("cardId",todoResponse.getId()+"");
        card.getStyle().set("background-color", "#d5dde8");
        card.setWidth("230px");
        card.setHeight("170px");
        card.getStyle().set("padding", "5px");
        card.addThemeVariants(CardVariant.LUMO_OUTLINED);
        card.addThemeVariants(CardVariant.LUMO_ELEVATED);

        Hr separator = new Hr();
        separator.getStyle().set("margin-left", "-4px");
        separator.getStyle().set("margin-right", "-5px");
        NativeLabel lblTitle = new NativeLabel(todoResponse.getTitle());
        lblTitle.addClassName("board_title_width");
        String todoType="";
        if(todoResponse.getTodoType() !=null){
            todoType=todoResponse.getTodoType().name();
        }else{
            todoType= TodoType.MANUAL.name();
        }
        NativeLabel lblTodoType = new NativeLabel(todoType);

        NativeLabel lblDoneRation= new NativeLabel("0% Done");
        NativeLabel lblTotalTodo = new NativeLabel("10 Todos");
        HorizontalLayout divTodoType = new HorizontalLayout(/*lblTodoType,*/ lblTotalTodo,lblDoneRation);
        divTodoType.setSpacing(true);
        divTodoType.setWidth("100%");
        divTodoType.addClassName("margin-top-card");
        //divTodoType.setPadding(false);
        Instant startDate = todoResponse.getGoalStartDate() != null ? todoResponse.getGoalStartDate() :Instant.now();
        Instant endDate = todoResponse.getGoalEndDate() != null ? todoResponse.getGoalEndDate() :Instant.now();
        LocalDateTime startDateValue = DateTimeUtil.convertUTCTimeToLocalDateTime(startDate, "Asia/Dhaka");
        LocalDateTime endDateValue = DateTimeUtil.convertUTCTimeToLocalDateTime(endDate, "Asia/Dhaka");

        String startDateFormatted = formatter.format(startDateValue);
        String endDateFormatted = formatter.format(endDateValue);
        Span startDateSpan = new Span();
        startDateSpan.getElement().getThemeList().add("badge warning");
        startDateSpan.add(startDateFormatted);
        Span endDateSpan = new Span();
        endDateSpan.getElement().getThemeList().add("badge success");
        endDateSpan.add(endDateFormatted);
        card.setHeader(lblTitle);
        HorizontalLayout dateRange = new HorizontalLayout();
        dateRange.getStyle().set("justify-content", "space-between");
        dateRange.setSpacing(true);
        dateRange.setWidth("100%");
        dateRange.add(startDateSpan,endDateSpan);
        dateRange.addClassName("margin-top-card");

        Button btnAddTodo = new Button();
        Icon addIcon = VaadinIcon.PLUS_CIRCLE_O.create();
        btnAddTodo.setIcon(addIcon);
        btnAddTodo.setTooltipText("Add todo item");
        Div popupDiv = new Div();
       // Component popComponent=openAddTodoPopup(todoResponse.getId());
      /*  btnAddTodo.addClickListener(e->{
        //    popupDiv.removeAll();
            popupDiv.add(popComponent);
        });*/

        btnAddTodo.getElement().addEventListener("click", e->{
            log.info("In event listener call...");
          //  log.info("Is pop component is null : {}, {}",popComponent.isVisible(), popComponent.isAttached());
          //  popupDiv.add(popComponent);
            openAddChecklistDialog(todoResponse.getId());
        }).stopPropagation();
        /*
        Popover popover = new Popover();
        popover.setTarget(btnAddTodo);
        popover.setWidth("300px");
        //popover.setHeight("300px");
        popover.setPosition(PopoverPosition.START_TOP);
        popover.add(popupDiv); */
        card.add(dateRange);
        card.add(separator);
        card.add(divTodoType);

        Button btnDelete = new Button();
        Button btnEdit = new Button();
        Icon deleteIcon = VaadinIcon.TRASH.create();
        Icon editIcon = VaadinIcon.EDIT.create();
        btnEdit.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING);
        btnDelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        btnDelete.setIcon(deleteIcon);
        btnEdit.setIcon(editIcon);
        Consumer<TodoResponseDTO> updateConsumer = (updatedTodoResponseDTO)->{
            log.info("When update consumer accept");
            getUI().ifPresent(ui->ui.access(()->{
                if(updatedTodoResponseDTO !=null){
                    if(updatedTodoResponseDTO.getTitle() !=null && !updatedTodoResponseDTO.getTitle().isBlank()){
                        log.info("Title is not null"+updatedTodoResponseDTO.getTitle());
                        lblTitle.setTitle(updatedTodoResponseDTO.getTitle());
                    }
                    if(updatedTodoResponseDTO.getGoalStartDate() !=null){
                        LocalDateTime updatedStartDateValue = DateTimeUtil.convertUTCTimeToLocalDateTime(updatedTodoResponseDTO.getGoalStartDate(), "Asia/Dhaka");
                        log.info("updatedStartDateValue is "+updatedStartDateValue);
                        startDateSpan.setText(formatter.format(updatedStartDateValue));
                    }
                    if(updatedTodoResponseDTO.getGoalEndDate() !=null) {
                        LocalDateTime updatedEndDateValue = DateTimeUtil.convertUTCTimeToLocalDateTime(updatedTodoResponseDTO.getGoalEndDate(), "Asia/Dhaka");
                        log.info("updatedEndDateValue is "+updatedEndDateValue);
                        endDateSpan.setText(formatter.format(updatedEndDateValue));
                    }
                }
            }));
        };

        Consumer<Boolean> isDeletedCardConsumer =(isDeleted)->{
          if(isDeleted){
              card.removeFromParent();
          }else{
              Notification.show("Board delete failed");
          }
        };

        btnDelete.getElement().addEventListener("click", e->{
            log.info("In delete listener event click");
            deleteTodoBoard(todoResponse.getId(), isDeletedCardConsumer);
        }).stopPropagation();

        btnEdit.getElement().addEventListener("click",e->{
            openEditDialog(todoResponse,updateConsumer);
        }).stopPropagation();


        card.addToFooter(btnAddTodo, btnEdit, btnDelete);
        this.add(card);
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

    private void openAddChecklistDialog(Integer todoBoardId){

        AddTodoItemDialog addTodoItemDialog = new AddTodoItemDialog(todoBoardId, todoService,null,null);
        addTodoItemDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        addTodoItemDialog.setWidth("500px");
        addTodoItemDialog.setHeight("170px");
        addTodoItemDialog.open();

     /*   VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getStyle().set("padding","10px");

        TextArea addTodoTextArea= new TextArea("Add todo item");
        addTodoTextArea.getStyle().set("padding-top","0px");
        Button btnCancel= new Button("Cancel");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        Icon closeIcon = VaadinIcon.CLOSE.create();
        btnCancel.setIcon(closeIcon);
        addTodoTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        addTodoTextArea.setWidth("100%");
        addTodoTextArea.setHeight("100px");
        HorizontalLayout buttonHrLayout = new HorizontalLayout();
        buttonHrLayout.setSpacing(true);
        buttonHrLayout.setWidth("100%");
        buttonHrLayout.setPadding(false);

        Button addTodoButton = new Button();
        addTodoButton.setText("Add");
        addTodoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING);
        buttonHrLayout.add(btnCancel,addTodoButton);
        buttonHrLayout.getStyle().set("align-self", "center");
        layout.add(addTodoTextArea,buttonHrLayout);
        Dialog dialog= new Dialog();
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        dialog.setWidth("500px");
        dialog.setHeight("170px");
        dialog.add(layout);
        dialog.open();
        btnCancel.addClickListener(e->{
           dialog.close();
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
                    Notification.show("Added successfully..");
                }else{
                    Notification.show("Todo item not added");
                }
            }else{
                log.info("When text area value is null...: {}", addTodoTextArea.getValue());

            }
        }, Key.ENTER);*/
    }

    private Component openAddTodoPopup(Integer todoBoardId){
        log.info("Open add todo popup method dialog component create");
        VerticalLayout popupLayout = new VerticalLayout();
        popupLayout.setPadding(false);
        popupLayout.setSpacing(false);
        TextArea addTodoTextArea= new TextArea("Add todo item");
        addTodoTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        addTodoTextArea.setWidth("100%");
        addTodoTextArea.setHeight("100px");
        addTodoTextArea.getStyle().set("margin-top","-13px");
        Button addTodoButton = new Button();
        addTodoButton.setText("Add");
        addTodoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.getStyle().set("align-self", "end");
        buttonLayout.add( addTodoButton);
        popupLayout.add(addTodoTextArea,buttonLayout);

        Shortcuts.addShortcutListener(addTodoTextArea,()->{

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
        }, Key.ENTER);
        return popupLayout;
      /*  Dialog dialog = new Dialog(popupLayout);
        dialog.setWidth("300px");
        dialog.setHeight("300px");
        dialog.open();*/
    }

    private void openEditDialog(TodoResponseDTO todoResponse, Consumer<TodoResponseDTO> updateConsumer){
        Dialog dialog = new Dialog();
        Consumer<Boolean> dialogCloseConsumer = (e) -> {
            log.info("Dialog closed consumer");
            dialog.close();
        };
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        dialog.setWidth("500px");
        dialog.setHeight("205px");
        AddTodoPopup addTodoPopup = new AddTodoPopup(todoService, dialogCloseConsumer,todoResponse,updateConsumer);
        dialog.add(addTodoPopup);
        dialog.open();


    }

    private boolean addTodoItemToTheTodoBoard(String todoItem, Integer todoBoardId) {

        String color = "#8cad95";
        TodoListRequestDTO todoListRequestDTO = new TodoListRequestDTO();
        todoListRequestDTO.setTodoId(todoBoardId);
        todoListRequestDTO.setTitle(todoItem);
        todoListRequestDTO.setCreatedBy(1);
        todoListRequestDTO.setTodoBgColor(color);

        TodoListResponseDTO responseDTO = this.todoService.createOrUpdateTodoListItem(todoListRequestDTO);

        if (responseDTO != null && responseDTO.getId() != null) {
            log.info("Response id is {}", responseDTO.getId());
            return true;
        }
        return false;
    }
}
