package com.todo.ui.view;

import com.todo.ui.config.SecurityContext;
import com.todo.ui.dto.CurrentUser;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.AttendeeDTO;
import com.todom.library.dto.CalendarEventDTO;
import com.todom.library.enums.AttendeeStatus;
import com.todom.library.utils.DateTimeUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Route(value = "events",layout = MainLayout.class)
@PageTitle("Events")
public class EventsView extends VerticalLayout {



    private ITodoService todoService;
    private Button btnSearch= new Button("Search");
    private DatePicker startDatePicker=new DatePicker();
    private DatePicker endDatePicker=new DatePicker();
    private Integer userId;
    private String userEmail=null;
    private String userTimeZone;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm a");
    private SecurityContext securityContext;
    private CurrentUser currentUser;
    private static final Logger log= LoggerFactory.getLogger(EventsView.class);
    public EventsView(ITodoService todoService, SecurityContext securityContext) {
        this.todoService = todoService;
        this.securityContext = securityContext;

        currentUser=securityContext.getCurrentUser();
        this.userId=currentUser.getUserId();
        userEmail=currentUser.getEmail();
       Object timeZone =  VaadinSession.getCurrent().getAttribute("userTimeZone");
       userTimeZone=String.valueOf(timeZone);
        initComp();
    }

    private void initComp() {

        HorizontalLayout datePickerLayout = new HorizontalLayout();
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setLocale(new Locale("tr","TR"));
        endDatePicker.setLocale(new Locale("tr","TR"));
        datePickerLayout.add(startDatePicker,endDatePicker,btnSearch);
        datePickerLayout.setSizeFull();
        add(datePickerLayout);

        loadAndAddEvent();

        btnSearch.addClickListener(e -> {
           loadAndAddEvent();
        });
    }

    private void loadAndAddEvent() {

        LocalDate startDate = startDatePicker.getValue();

        Instant startTime = DateTimeUtil.convertLocaldateTodInstant(startDate);
        Instant endTime = DateTimeUtil.convertLocaldateTodInstant(endDatePicker.getValue());


        List<CalendarEventDTO> calendarEventDTOList= this.todoService.getAllCalendarEventsByUserId(userId,startTime,endTime);
        log.info("The calendar event dto list : {}",calendarEventDTOList.size());
        FlexLayout eventsLayout = new FlexLayout();
        eventsLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        eventsLayout.getStyle().set("gap", "0.5rem");
        for(CalendarEventDTO calendarEventDTO:calendarEventDTOList){
            eventsLayout.add(createCalendarEventCard(calendarEventDTO));
        }

        this.add(eventsLayout);
    }

    private Card createCalendarEventCard(CalendarEventDTO calendarEventDTO) {
        Card card = new Card();
        card.setWidth("220px");
        Header header = new Header();
        HorizontalLayout meetingTitleLayout = new HorizontalLayout();
        meetingTitleLayout.getStyle().set("align-items","center");
        Image meetingImage = new Image();
        meetingImage.setSrc("images/meeting_title.png");

        NativeLabel lblTitle = new NativeLabel();
        lblTitle.setText(calendarEventDTO.getSummary());

        meetingTitleLayout.add(meetingImage);
        meetingTitleLayout.setPadding(false);
        String status="";

        Span eventStatusSpan = new Span();
        eventStatusSpan.getStyle().set("height","30px");
        String userStatusValue=null;
        if(calendarEventDTO.getAttendees()!=null && calendarEventDTO.getAttendees().size()>0){

            AttendeeDTO attendeeDTO= calendarEventDTO.getAttendees().stream().filter(e->
                    e.getEmail().equalsIgnoreCase(userEmail)).findAny().orElse(null);

            if(attendeeDTO !=null){
                userStatusValue=attendeeDTO.getResponseStatus();
            }
        }
       // log.info("User email and creator email : {}, {}", userEmail,calendarEventDTO.getCreator());
        if(userStatusValue != null){
            if(userStatusValue.equalsIgnoreCase("accepted")){
                status= AttendeeStatus.Accepted.label;
                eventStatusSpan = new Span(createIcon(VaadinIcon.CHECK), new Span(status));
                eventStatusSpan.getElement().getThemeList().add("badge success pill primary");
              //  eventStatusSpan.add(createIcon(VaadinIcon.CHECK));
            }
            else if(userStatusValue.equalsIgnoreCase("declined")){
                status=AttendeeStatus.Rjected.label;
                eventStatusSpan = new Span(createIcon(VaadinIcon.EXCLAMATION_CIRCLE_O), new Span(status));
                eventStatusSpan.getElement().getThemeList().add("badge error pill primary");
               // eventStatusSpan.add(createIcon(VaadinIcon.EXCLAMATION_CIRCLE_O));
            }
            else if(userStatusValue.equalsIgnoreCase("tentative")){
                status=AttendeeStatus.Not_Sure.label;
                eventStatusSpan = new Span(createIcon(VaadinIcon.WARNING), new Span(status));
                eventStatusSpan.getElement().getThemeList().add("badge warning pill primary");
               // eventStatusSpan.add(createIcon(VaadinIcon.WARNING));
            } else if (userStatusValue.equalsIgnoreCase("needsAction")) {
                status=AttendeeStatus.Needs_Action.label;
                log.info("Need action value is : {}",status);
                eventStatusSpan = new Span(createIcon(VaadinIcon.HAND), new Span(status));
                eventStatusSpan.getElement().getThemeList().add("badge contrast pill primary");
             //   eventStatusSpan.add(createIcon(VaadinIcon.HAND));
            }
        }

        Span startTimeSpan = new Span(createIcon(VaadinIcon.CLOCK), new Span(formatter.format(calendarEventDTO.getStartDate())));
        startTimeSpan.getElement().getThemeList().add("badge");

        if(status ==null || status.isBlank()){
            if(userEmail!=null && calendarEventDTO.getCreator() !=null &&
                    userEmail.equalsIgnoreCase(calendarEventDTO.getCreator())){
                log.info("User email and creator both are same: {}, {}", userEmail,calendarEventDTO.getCreator());
                status=AttendeeStatus.valueOf(AttendeeStatus.Creator.name()).toString();
                eventStatusSpan = new Span(createIcon(VaadinIcon.USER_CHECK), new Span(status));
                eventStatusSpan.getElement().getThemeList().add("badge contrast pill primary");
            }
        }

       // eventStatusSpan.setText(status);
        log.info("Status : {}, and status title : {}", status,calendarEventDTO.getSummary());
        eventStatusSpan.setVisible(false);

        if(status !=null && !status.isBlank()){
            eventStatusSpan.setVisible(true);
        }

        VerticalLayout timeAndStatusLayout = new VerticalLayout();
        timeAndStatusLayout.setPadding(false);
        meetingTitleLayout.add(eventStatusSpan);
        timeAndStatusLayout.add(startTimeSpan,lblTitle);

        header.add(meetingTitleLayout);
        card.setHeader(header);
        card.add(timeAndStatusLayout);
        return card;
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs)");
        return icon;
    }
}
