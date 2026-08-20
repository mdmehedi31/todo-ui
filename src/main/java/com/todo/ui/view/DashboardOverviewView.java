package com.todo.ui.view;

import com.todo.ui.config.SecurityContext;
import com.todo.ui.dto.CurrentUser;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.DashBoardDTO;
import com.todom.library.dto.TodoResponseDTO;
import com.todom.library.utils.DateTimeUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Route(value="dashboard-overview",layout = MainLayout.class)
@PageTitle("Dashboard")
public class DashboardOverviewView extends VerticalLayout {

    Grid<TodoResponseDTO> grid = new Grid<>();
    Grid<TodoResponseDTO> overDueGrid = new Grid<>();
    private ITodoService todoService;
    private  DashBoardDTO dashBoardDTO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();
    private  ZoneId userZone;
    private Button btnSearch= new Button("Search");
    private   HorizontalLayout layout = new HorizontalLayout();
    private static final Logger log = LoggerFactory.getLogger(DashboardOverviewView.class);
    private SecurityContext securityContext;
    private CurrentUser currentUser;
    public DashboardOverviewView(ITodoService todoService,SecurityContext securityContext) {

        this.todoService = todoService;
        this.securityContext = securityContext;
        this.currentUser = securityContext.getCurrentUser();

        ZoneId userZone = (ZoneId) VaadinSession.getCurrent().getAttribute("user-tz");
        if (userZone == null)
            userZone = ZoneId.of("Asia/Dhaka");

        addClassName("dashboard-container");
        setSizeFull();
        setSpacing(false);
        setPadding(false);

        add(createHeader());
        add(createStatsSection());
        add(createBottomSection());
        loadDasahboardData();

        eventListener();
    }

    private void eventListener() {
        btnSearch.addClickListener(e -> {
            log.info("Trying to search between the date...");
            loadDasahboardData();
        });
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("dashboard-header");
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(Alignment.CENTER);

        VerticalLayout titleArea = new VerticalLayout(
                new H2("Dashboard Overview"),
                new Span("Home / Dashboard")
        );
        titleArea.setPadding(false);
        titleArea.setSpacing(false);
        titleArea.addClassName("header-title-wrapper");

        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(Alignment.BASELINE);
        actions.setSpacing(true);

        btnSearch = new Button("Search", VaadinIcon.SEARCH.create());
        Button addBtn = new Button("AI TODO Generator", VaadinIcon.PLUS.create());
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBtn.addClassName("add-task-button");

        addBtn.addClickListener(e->{
            UI.getCurrent().navigate(TodoGeneratorView.class);
        });
        this.startDatePicker.setValue(LocalDate.now().minusDays(21));
        this.endDatePicker.setValue(LocalDate.now());
        Locale finnishLocale = new Locale("fi", "FI");
        startDatePicker.setLocale(finnishLocale);
        endDatePicker.setLocale(finnishLocale);
        actions.add(this.startDatePicker, this.endDatePicker, btnSearch, addBtn);
        header.add(titleArea, actions);
        header.setFlexGrow(1.0, titleArea);

        return header;
    }

    private Component createHeaderRow(String title, VaadinIcon icon, String iconClass) {
        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(true);
        header.setAlignItems(Alignment.CENTER);
        header.setPadding(false);

        Span titleLabel = new Span(title);
        titleLabel.getStyle()
                .set("font-size", "0.75rem")
                .set("font-weight", "600")
                .set("color", "#64748b")
                .set("text-transform", "uppercase");

        Icon statusIcon = icon.create();
        statusIcon.addClassName(iconClass);
        statusIcon.getStyle().set("font-size", "1rem");

        header.add(titleLabel, statusIcon);
        return header;
    }

    private void loadDasahboardData(){
        LocalDate startDate = this.startDatePicker.getValue();
        LocalDate endDate =this.endDatePicker.getValue();

        log.info("Start date : {}. End date : {}", startDate, endDate);
        if(this.currentUser!= null && currentUser.getUserId()!=null)
        dashBoardDTO = this.todoService.getAllDashBoardDataWithDateRange(startDate, endDate,currentUser.getUserId());

        createStatsSection();
    }

    private Component createStatsSection() {
        layout.removeAll();
        layout.setWidthFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.addClassName("stats-row");


        Integer totalInprogress =0;
        Integer totalDone=0;
        Integer totalDonePercentage=0;

        if(dashBoardDTO!=null && dashBoardDTO.getTotalInprogress() != null ) {
            totalInprogress = dashBoardDTO.getTotalInprogress();
        }

        if(dashBoardDTO!=null && dashBoardDTO.getTotalDone() != null ) {
            totalDone = dashBoardDTO.getTotalDone();
        }

        if (dashBoardDTO!=null && dashBoardDTO.getTotalDoneItemPercentage() != null) {
            totalDonePercentage = dashBoardDTO.getTotalDoneItemPercentage();
        }

        log.info("Progress value : {}, done : {}, Done % : {}", totalInprogress, totalDone, totalDonePercentage);
        layout.add(createProgressCard("IN PROGRESS", totalInprogress.toString(), VaadinIcon.COG, "blue-text"));
        layout.add(createDoneCard("DONE", totalDone.toString(), totalDonePercentage.toString()));

        if(dashBoardDTO!=null && dashBoardDTO.getRecentTodos() != null) {
            this.grid.setItems(dashBoardDTO.getRecentTodos());
        }
        return layout;
    }


    private Component createProgressCard(String title, String val, VaadinIcon icon, String colorClass) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames("info-card", "stat-card-fixed");

        log.info("Progress card->  Title : {}, Value : {}", title, val);

        HorizontalLayout top = new HorizontalLayout(new Span(title), icon.create());
        top.setWidthFull();
        top.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 value = new H1(val);
        value.addClassNames("big-number", colorClass);

        Div barContainer = new Div(new Div());
        barContainer.addClassName("custom-progress-bar");

        Span trend = new Span("Trend: +5 new tasks this week");
        trend.addClassName("small-muted-text");

        card.add(top, value, barContainer, trend);
        return card;
    }

    private Component createOverdueCard(String title, String val, List<String> tasks) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames("info-card", "stat-card-fixed");

        HorizontalLayout top = new HorizontalLayout(new Span(title), VaadinIcon.WARNING.create());
        top.setWidthFull();
        top.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 value = new H1(val);
        value.addClassNames("big-number", "red-text");

        UnorderedList list = new UnorderedList();
        tasks.forEach(t -> list.add(new ListItem(t)));
        list.addClassName("task-bullet-list");

        card.add(top, value, list);
        return card;
    }
    private Component createDoneCard(String title, String val, String percent) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassNames("info-card", "stat-card-fixed");
        card.setAlignItems(Alignment.START);
        card.setPadding(true);
        log.info("DONE card->  Title : {}, Value : {}", title, val);
        VerticalLayout leftSide = new VerticalLayout();
        leftSide.setPadding(false);
        leftSide.setSpacing(false);
        leftSide.setAlignItems(Alignment.START);

        leftSide.add(createHeaderRow(title, VaadinIcon.CHECK_CIRCLE, "green-icon"),
                new H1(val),
                new Span("Completion vs. Goal"));

        leftSide.getComponentAt(1).getElement().getClassList().add("big-number");
        leftSide.getComponentAt(2).getElement().getClassList().add("small-muted-text");


        Div circleContainer = new Div();
        circleContainer.addClassName("circle-progress-wrapper");

        String numericValue = percent.replace("%", "").trim();

        Div circle = new Div(new Span(percent));
        circle.addClassName("progress-circle-ui");
        circle.getStyle().set("--percent", numericValue + "%");

        circle.getElement().getStyle().set("--percent", numericValue + "%");

        circle.getElement().getStyle().set("--percent", numericValue + "%");

        circleContainer.add(circle);

        card.add(leftSide, circleContainer);
        card.setFlexGrow(1, leftSide);

        return card;
    }


    private Component createBottomSection() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setPadding(true);
        layout.setSpacing(true);

        VerticalLayout trendCard = new VerticalLayout(new H4("OVER DUE TODOS"));
        trendCard.add(overDueGrid);
        trendCard.addClassNames("info-card", "bottom-card-height");

        VerticalLayout activityCard = new VerticalLayout(new H4("RECENT TASKS & ACTIVITY"));
        activityCard.addClassNames("info-card", "bottom-card-height");


        grid.addColumn( TodoResponseDTO::getTitle).setHeader("Task");
        grid.addColumn(e->{
            if(e.getGoalStartDate() !=null){
                log.info("date value is " + e.getGoalStartDate()+", board id is " + e.getId());
                String timeZoneString = (userZone != null) ? userZone.getId() : "UTC";
                LocalDate localDate= DateTimeUtil.convertUTCTimeToLocalDate(e.getGoalStartDate(),timeZoneString);
                return formatter.format(localDate);
            }
            return "";
        }).setHeader("End date");
        grid.addComponentColumn(s -> createBadge(s.getTotalTodoItems())).setHeader("Total TODOs");
        grid.addComponentColumn(s -> createStatusBadge(s.getTotalDoneItems())).setHeader("Total DONE");

        grid.addThemeVariants();
        grid.addClassName("dashboard-grid");

        overDueGridInti();
        activityCard.add(grid);
        layout.add(trendCard, activityCard);
        layout.setFlexGrow(1.5, trendCard);
        return layout;
    }

    private void overDueGridInti() {

        overDueGrid.addColumn( TodoResponseDTO::getTitle).setHeader("Task");
        overDueGrid.addColumn(e->{

            if(e.getGoalStartDate() !=null){
                String timeZoneString = (userZone != null) ? userZone.getId() : "UTC";
                LocalDate localDate= DateTimeUtil.convertUTCTimeToLocalDate(e.getGoalStartDate(),timeZoneString);
                return formatter.format(localDate);
            }
            return "";
        }).setHeader("Start date");

        overDueGrid.addColumn(e->{

            if(e.getGoalEndDate() !=null){
                String timeZoneString = (userZone != null) ? userZone.getId() : "UTC";
                LocalDate localDate= DateTimeUtil.convertUTCTimeToLocalDate(e.getGoalEndDate(),timeZoneString);
                return formatter.format(localDate);
            }
            return "";
        }).setHeader("End date");
        overDueGrid.addComponentColumn(s -> createBadge(s.getTotalTodoItems())).setHeader("Total TODOs");

        overDueGrid.addComponentColumn(s -> createStatusBadge(s.getTotalDoneItems())).setHeader("Total DONE");

        if(this.dashBoardDTO !=null && this.dashBoardDTO.getOverDueTodos()!=null && this.dashBoardDTO.getOverDueTodos().size() > 0){
            this.overDueGrid.setItems(this.dashBoardDTO.getOverDueTodos());
        }
    }

    private Span createBadge(Integer text) {
        Span s = new Span(text+"");
        s.addClassNames("badge-ui", "" + text);
        return s;
    }

    private Span createStatusBadge(Integer text) {
        Span s = new Span(text+"");
        s.addClassNames("badge-ui", "" + text);
        return s;
    }
}