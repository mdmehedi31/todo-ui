package com.todo.ui.view;


import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.GoogleCalendarEventCreateDto;
import com.todom.library.dto.UserCalendarDto;
import com.todom.library.enums.MeetingLocation;
import com.todom.library.utils.DateTimeUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScheduleSetupDialog extends VerticalLayout {

    private ITodoService todoService;
    private Integer boardId;
    private Integer userId;
    private Integer boardItemId;
    List<String> emailIdList= new ArrayList<>();
    private static final Logger log= LoggerFactory.getLogger(ScheduleSetupDialog.class);
    private TextField txtSummery = new TextField("Event title");
    private TextArea txtDescription = new TextArea("Event description");
    private DateTimePicker eventStartDate = new DateTimePicker("Event start date");
    private DateTimePicker eventEndDate = new DateTimePicker("Event end date");
    private ComboBox<UserCalendarDto> calendareListCombobox = new ComboBox<>("Select calendar");
    private ComboBox<MeetingLocation> meetingLocation = new ComboBox<>("Select meeting location");
    private Button btnSetupSchedule = new Button("Set up schedule");
    private Button btnCancel = new Button("Cancel");
    private String userTimezone;
    private Consumer<Boolean> isAdde;

    public ScheduleSetupDialog(ITodoService todoService, Integer boardId, Integer userId, Integer boardItemId,Consumer<Boolean> isAdde) {
        this.todoService = todoService;
        this.boardId = boardId;
        this.userId = userId;
        this.boardItemId = boardItemId;
        this.isAdde = isAdde;
        log.info("When setup schedule");
        this.setWidth(null);
        this.setPadding(false);
        initComponent();
        this.getStyle().set("margin", "-10px");
        eventListener();

       Object timeZone= VaadinSession.getCurrent().getAttribute("userTimeZone");
       this.userTimezone=String.valueOf(timeZone);
    }

    private void eventListener() {

        btnSetupSchedule.addClickListener(e -> {

            setupEvent();
        });

        btnCancel.addClickListener(e -> {
            this.isAdde.accept(true);
        });
    }

    private void setupEvent() {
        GoogleCalendarEventCreateDto googleCalendarEventCreateDto = new GoogleCalendarEventCreateDto();

        if(txtSummery.getValue() != null && !txtSummery.getValue().isBlank()) {
            googleCalendarEventCreateDto.setEventSummary(txtSummery.getValue());
        }

        if(txtDescription.getValue() != null && !txtDescription.getValue().isBlank()) {
            googleCalendarEventCreateDto.setDescription(txtDescription.getValue());
        }

        if(emailIdList != null && !emailIdList.isEmpty()) {
            googleCalendarEventCreateDto.setInviteUserList(emailIdList);
        }

        if(calendareListCombobox.getValue() !=null){
            googleCalendarEventCreateDto.setCalendarId(calendareListCombobox.getValue().getCalendarId());
        }

        if(meetingLocation.getValue() !=null){
            googleCalendarEventCreateDto.setLocation(meetingLocation.getValue().name());
        }

        if(eventStartDate.getValue() !=null){
            LocalDateTime startDateValue= eventStartDate.getValue();
            Instant startDateTimeValue = DateTimeUtil.convertLocalDateTimeToUTCTime(startDateValue,this.userTimezone);
            googleCalendarEventCreateDto.setEventStartDate(startDateTimeValue);
        }

        if(eventEndDate.getValue() !=null){
            LocalDateTime endDateValue= eventEndDate.getValue();
            Instant endDateTimeValue = DateTimeUtil.convertLocalDateTimeToUTCTime(endDateValue,this.userTimezone);
            googleCalendarEventCreateDto.setEventEndDate(endDateTimeValue);
        }

        ApiResponse response= this.todoService.setupEventSchedule(this.boardId, this.boardItemId,this.userId,googleCalendarEventCreateDto);

        if(response !=null){
           if(response.getMessage().equals("SUCCESS")){
               Notification.show("Setup schedule successful");
                this.isAdde.accept(true);
           }
        }

    }

    private void initComponent() {
        log.info("From init component method...");
        HorizontalLayout calendarListAndLocationLayout = new HorizontalLayout();
        calendarListAndLocationLayout.setWidth("100%");
        calendarListAndLocationLayout.setSpacing(true);
        calendarListAndLocationLayout.setPadding(false);
        List<UserCalendarDto> userCalendarDtoList= getAllUserCalendar();
        calendareListCombobox.setItems(userCalendarDtoList);
        calendareListCombobox.setWidth("65%");
        calendareListCombobox.setItemLabelGenerator(UserCalendarDto::getCalendarName);

        meetingLocation.setItems(MeetingLocation.values());
        calendarListAndLocationLayout.add(calendareListCombobox,meetingLocation);
        HorizontalLayout startAndDateLayout = new HorizontalLayout();
        startAndDateLayout.setSpacing(true);
        startAndDateLayout.setPadding(false);

        eventStartDate.setWidth("230px");
        eventEndDate.setWidth("230px");
        eventStartDate.setStep(Duration.ofMinutes(15));
        eventEndDate.setStep(Duration.ofMinutes(15));
        startAndDateLayout.add(eventStartDate,eventEndDate);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(false);
        btnSetupSchedule.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_PRIMARY);
        btnCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        buttonLayout.add(btnSetupSchedule, btnCancel);
        txtSummery.setWidth("100%");
        txtSummery.getStyle().set("margin-top", "-10px");
        txtDescription.setWidth("100%");
        txtDescription.getStyle().set("margin-top","-10px");
        startAndDateLayout.getStyle().set("margin-top","-10px");
        this.add(calendarListAndLocationLayout,startAndDateLayout, txtSummery,txtDescription,generateEmailField(),buttonLayout);

    }

    private List<UserCalendarDto> getAllUserCalendar() {
        log.info("When load user calendar list");
        List<UserCalendarDto> calendarList =this.todoService.getAllUserCalendarsByUserId(this.userId);
        log.info("When load user calendar list : {}", calendarList.size());
        return calendarList;
    }

    private Component generateEmailField(){
        log.info("When generate email field..");
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        layout.setWidth("100%");
        layout.getStyle().set("border","1px gray dotted");
        EmailField emailField = new EmailField();
        emailField.setManualValidation(true);
        emailField.setWidth("95%");
        emailField.getStyle().set("padding","10px");
        FlexLayout emailListFlexLayout = new FlexLayout();
        emailListFlexLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        emailListFlexLayout.getStyle().set("overflow-y","auto");
        emailListFlexLayout.getStyle().set("padding-left","5px");
        emailListFlexLayout.getStyle().set("max-height","115px");

        emailField.setErrorMessage("Enter a valid email address");
        emailField.setClearButtonVisible(true);

        emailField.addKeyDownListener(Key.ENTER, e-> {

            String email = emailField.getValue();
            if (email != null && email.contains("@")) {
                if(!emailIdList.contains(email)){
                    Div emailDiv=  generateEmaliBox(email);
                    emailDiv.getStyle().set("background","burlywood");
                    emailDiv.getStyle().set("margin","10px");
                    emailDiv.getStyle().set("border-radius","7px");
                    emailDiv.getStyle().set("font-weight","600");

                    emailListFlexLayout.add(emailDiv);
                }else{
                    Notification.show("Email already added in the list");
                }
            }else{
                Notification.show("Email is not valid...");
            }
            emailField.clear();
        });
        layout.add(emailField, emailListFlexLayout);
        return layout;
    }

    private Div generateEmaliBox(String email) {
        this.emailIdList.add(email);
        Div div = new Div();
        div.getStyle().set("padding","5px");
      //  div.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        NativeLabel lblEmail= new NativeLabel();
        lblEmail.setText(email);
        div.add(lblEmail);
        Icon closeIcon = VaadinIcon.CLOSE.create();
        closeIcon.getStyle().set("height","15px");
        closeIcon.getStyle().set("color","#f7100c");
        closeIcon.setTooltipText("Remove email");
        closeIcon.addClickListener(e -> {
            div.removeFromParent();
            emailIdList.remove(email);
            log.info("When trying to remove email from list");
        });
        div.add(closeIcon);
        return div;
    }
}
