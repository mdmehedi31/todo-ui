package com.todo.ui.view;

import com.todo.ui.event.AddTodoModelEvent;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.TodoRequestDTO;
import com.todom.library.dto.TodoResponseDTO;
import com.todom.library.enums.TodoType;
import com.todom.library.utils.DateTimeUtil;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.function.Consumer;

public class AddTodoPopup extends VerticalLayout {

    private TextField todoText= new TextField();
    private NativeLabel lblTitle= new NativeLabel("Add todo");
    private DatePicker startDate= new DatePicker("Goal start date");
    private DatePicker endDate= new DatePicker("Goal end date");
    private Button btnCancel= new Button("Cancel");
    private Button btnAddOrSave= new Button("Add");
    private Button btnStartDate= new Button();
    private Button btnEndDate= new Button();
    private ITodoService  todoService;
    private Consumer<Boolean> dialogCloseConsumer;
    private Consumer<TodoResponseDTO> updateOrSaveConsumer;
    private TodoResponseDTO todoResponse;
    private static final Logger log = LoggerFactory.getLogger(AddTodoPopup.class);

    public AddTodoPopup( ITodoService  todoService,  Consumer<Boolean> dialogCloseConsumer,TodoResponseDTO todoResponse,
                         Consumer<TodoResponseDTO> updateOrSaveConsumer) {
        this.todoService = todoService;
        this.dialogCloseConsumer = dialogCloseConsumer;
        this.todoResponse=todoResponse;
        this.updateOrSaveConsumer = updateOrSaveConsumer;

        this.setSpacing(false);
        this.getStyle().set("padding", "7px");
       // this.setPadding(false);
        //this.getStyle().set("margin-left", "-13px");
     //   this.getStyle().set("margin-top", "-20px");
        initTodoPopup();
        if(this.todoResponse !=null){
            setData();
            btnAddOrSave.setText("Save");
        }else{
            this.startDate.setValue(LocalDate.now());
            LocalDate endDate = LocalDate.now().plusDays(5);
            this.endDate.setValue(endDate);
        }
    }

    private void setData() {
        if(this.todoResponse.getTitle() !=null && !this.todoResponse.getTitle().isBlank()){
            this.todoText.setValue(this.todoResponse.getTitle());
        }

        if(this.todoResponse.getGoalStartDate() !=null){
            LocalDate ldStartDate = DateTimeUtil.convertUTCTimeToLocalDate(this.todoResponse.getGoalStartDate(),"Asia/Dhaka");
            this.startDate.setValue(ldStartDate);
        }

        if(this.todoResponse.getGoalEndDate() !=null){
            LocalDate lcEndDate = DateTimeUtil.convertUTCTimeToLocalDate(this.todoResponse.getGoalEndDate(),"Asia/Dhaka");
            this.endDate.setValue(lcEndDate);
        }
    }

    private void initTodoPopup() {
        todoText.setWidth("100%");
       // btnStartDate.setText("Goal start date");
        //Icon calenderStartDateIcon = VaadinIcon.CALENDAR.create();
       // Icon calenderEndDateIcon = VaadinIcon.CALENDAR.create();
       // btnStartDate.setIcon(calenderStartDateIcon);
       // btnEndDate.setText("Goal end date");
       // btnEndDate.setIcon(calenderEndDateIcon);

        Icon addIcon = VaadinIcon.PLUS.create();
        btnAddOrSave.setIcon(addIcon);
        btnCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        Icon closeIcon = VaadinIcon.CLOSE.create();
        btnStartDate.setIcon(closeIcon);
        btnAddOrSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_WARNING);

        btnAddOrSave.setEnabled(false);
        HorizontalLayout btnLayout= new HorizontalLayout(btnCancel, btnAddOrSave);
        btnLayout.getStyle().set("align-self", "center");
        HorizontalLayout datePickerLayout= new HorizontalLayout();
        datePickerLayout.setWidth("100%");
        startDate.setWidth("133px");
        endDate.setWidth("133px");
        datePickerLayout.add(startDate,endDate);


        this.add(lblTitle,todoText, datePickerLayout, btnLayout);
        eventListener();
       /* Popover popStartDate = new Popover();
        popStartDate.setTarget(btnStartDate);
        popStartDate.setWidth("300px");
        popStartDate.addThemeVariants(PopoverVariant.ARROW,
                PopoverVariant.LUMO_NO_PADDING);
        popStartDate.setPosition(PopoverPosition.BOTTOM);

        Popover popEndDate = new Popover();
        popEndDate.setTarget(btnEndDate);
        popEndDate.setWidth("300px");
        popEndDate.setHeight("300px");
        popEndDate.addThemeVariants(PopoverVariant.ARROW,
                PopoverVariant.LUMO_NO_PADDING);
        popEndDate.setPosition(PopoverPosition.BOTTOM);*/

     /*   btnStartDate.addClickListener(e->{
           openDateSelector("Start Date");
        });

        btnEndDate.addClickListener(e->{
            openDateSelector("End Date");
        });*/
    }

    private void eventListener() {

        btnCancel.addClickListener(e -> {
            log.info("Close button click listener");
            this.dialogCloseConsumer.accept(true);
        });

        btnAddOrSave.addClickListener(e -> {
            log.info("Add todo button click listener");
            saveTodo();
        });

        todoText.setValueChangeMode(ValueChangeMode.EAGER);
        todoText.addValueChangeListener(e->{
           if(todoText.getValue()!=null && todoText.getValue().length()>6) {
               log.info("When text field is not empty and length is grater the 6");
               this.btnAddOrSave.setEnabled(true);
           }else{
               log.info("When text field is empty");
               this.btnAddOrSave.setEnabled(false);
           }
        });
    }

    private void saveTodo() {

        if(endDate.getValue().isAfter(startDate.getValue())) {
            TodoRequestDTO todoRequestDTO= new TodoRequestDTO();
            todoRequestDTO.setTitle(todoText.getValue());
            LocalDate lcStartDate= LocalDate.now();
            LocalDate lcEndDate= LocalDate.now();

            if(this.startDate.getValue() != null){
                lcStartDate=this.startDate.getValue();
            }

            if(this.endDate.getValue() != null){
                lcEndDate=this.endDate.getValue();
            }

            Instant goalStartDate =DateTimeUtil.convertLocaldateTodInstant(lcStartDate);
            Instant goalEndDate =DateTimeUtil.convertLocaldateTodInstant(lcEndDate);
            todoRequestDTO.setGoalStartDate(goalStartDate);
            todoRequestDTO.setGoalEndDate(goalEndDate);

            if(this.todoResponse !=null && this.todoResponse.getId() !=null){
                todoRequestDTO.setId(this.todoResponse.getId());
            }else{
                todoRequestDTO.setCreatedById(1);
                todoRequestDTO.setTodoType(TodoType.MANUAL);
            }

            TodoResponseDTO todoResponseDTO= this.todoService.createOrUpdateTodo(todoRequestDTO);
            if(todoResponseDTO != null && todoResponseDTO.getId() !=null){
                log.info("The todo response dto is not null, save successfully...");
                if(updateOrSaveConsumer != null){
                    this.updateOrSaveConsumer.accept(todoResponseDTO);
                }else{
                    AddTodoModelEvent event = new AddTodoModelEvent(UI.getCurrent(),false,todoResponseDTO);
                    ComponentUtil.fireEvent(UI.getCurrent(), event);
                }
                if(this.dialogCloseConsumer != null){
                    this.dialogCloseConsumer.accept(true);
                }
            }else{
                log.info("Todo not save successfully....");
            }
        }else{
            Notification.show("Invalid date range",1200, Notification.Position.TOP_START);
        }

    }

  /*  private Component openDateSelector(String viewName){
        VerticalLayout datePickerLayout= new VerticalLayout();
        DatePicker datePicker= new DatePicker();
        datePicker.setWidth("100%");
        datePicker.addAttachListener(event -> {
            datePicker.getElement().executeJs("this.shadowRoot.querySelector('vaadin-text-field').style.display = 'none';");
            datePicker.open();
        });
        NativeLabel lblViewName= new NativeLabel(viewName);
        datePickerLayout.add(lblViewName, datePicker);
        return datePickerLayout;
    }*/
}
