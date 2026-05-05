package com.todo.ui.view;

import com.todo.ui.gAuth.view.GoogleCalendarConnectionDialog;
import com.todo.ui.googleAuth.GoogleCalendareService;
import com.todo.ui.todoService.ITodoService;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.lineawesome.LineAwesomeIcon;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.function.Consumer;

@Route("/todo")
@Menu(order = 0, icon = LineAwesomeIconUrl.TASKS_SOLID, title="Todo Boards")
public class Todo extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(Todo.class);

    private FlexLayout cardLayout = new FlexLayout();
    private ITodoService  todoService;
    private  TodoCardParentView cardParentView;
    private GoogleCalendareService googleCalendareService;
    private Button connectGoogleCalender= new Button("Connect Google Calender", VaadinIcon.GOOGLE_PLUS.create());
    private Button btnAddTodo = new Button("Add Todo", VaadinIcon.PLUS.create());
    private Integer userId=1;
    public Todo(ITodoService  todoService,GoogleCalendareService googleCalendareService){
        this.todoService = todoService;
        this.googleCalendareService = googleCalendareService;
        addClassName("dashboard-root");
        initComp();
        buildCard();


        UI.getCurrent().getPage().executeJs(
                "const tz = Intl.DateTimeFormat().resolvedOptions().timeZone; " +
                        "$0.$server.setUserTimeZone(tz);",
                getElement()
        );

    }

    @ClientCallable
    public void setUserTimeZone(String timeZone) {
        log.info("User system time zone: " + timeZone);
        VaadinSession.getCurrent().setAttribute("userTimeZone", timeZone);
    }

    private void buildCard() {
      //  TodoCardsView cardsView= new TodoCardsView(todoService, 1);
        cardParentView = new TodoCardParentView(todoService,1,googleCalendareService);
        this.add(cardParentView);
    }

    private void initComp() {
        this.setSpacing(false);

        connectGoogleCalender.addClassName("secondary-action-btn");
      //  connectGoogleCalender.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        connectGoogleCalender.addClickListener(e->{
            openGoogleCalenderConnectDialog();
            log.info("Open google calender");
        });
        btnAddTodo.addClassName("primary-action-btn");
       // btnAddTodo.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING)
        HorizontalLayout btnHorizontalLayout = new HorizontalLayout();
        btnHorizontalLayout.add(btnAddTodo, connectGoogleCalender);


        H2 logo = new H2("TaskHub");
        logo.addClassName("dashboard-logo");
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("dashboard-header");
        header.setWidthFull();
        header.add(logo, btnHorizontalLayout);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN.BETWEEN);

        this.add(header);
        btnAddTodo.addClickListener(e -> {
            log.info("When click add todo button");
            openDialog();
        });
    }

    private void openDialog() {
        log.info("Open dialog method...");
        Dialog dialog = new Dialog();
        Consumer<Boolean> dialogCloseConsumer = (e) -> {
            log.info("Dialog closed consumer");
            dialog.close();
        };

    /*    Consumer<TodoResponseDTO> saveTodoConsumer = (todoResponse) -> {
            log.info("From save TODO consumer");
            log.info("The value is : {} , {}, {}, {}, {}", todoResponse.getId(), todoResponse.getGoalStartDate(), todoResponse.getGoalEndDate(),todoResponse.getTitle(),todoResponse.getTodoType());
            this.cardParentView.getCardsView().buildTodoList(todoResponse);
        };*/

        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        dialog.setWidth("500px");
        dialog.setHeight("205px");
        AddTodoPopup addTodoPopup = new AddTodoPopup(todoService, dialogCloseConsumer,null,null);
        dialog.add(addTodoPopup);
        dialog.open();
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
}
