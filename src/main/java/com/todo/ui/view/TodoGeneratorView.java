package com.todo.ui.view;

import com.todo.ui.todoService.IAiGenerateTodoService;
import com.todo.ui.todoService.ITodoService;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.TodoDTO;
import com.todom.library.dto.TodoListDTO;
import com.todom.library.enums.SuccessOrFailedEnum;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Route("todo-generator")
public class TodoGeneratorView extends VerticalLayout {

    private final VerticalLayout todoContainer;
    private final TextField promptField;
    private final Scroller scroller;
    private final ProgressBar loadingBar;
    private final IAiGenerateTodoService iAiGenerateTodoService;
    private final ITodoService iTodoService;
    private static final Logger log = LoggerFactory.getLogger(TodoGeneratorView.class);

    public TodoGeneratorView(IAiGenerateTodoService iAiGenerateTodoService,ITodoService iTodoService) {
        this.iAiGenerateTodoService = iAiGenerateTodoService;
        this.iTodoService = iTodoService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        addClassName("generative-prompt-view");

        VerticalLayout mainWrapper = new VerticalLayout();
        mainWrapper.setWidth("70%");
       // mainWrapper.setMaxWidth("95vw");
        mainWrapper.setHeight("90%");
        mainWrapper.setPadding(true);

        todoContainer = new VerticalLayout();
        todoContainer.setPadding(false);
        todoContainer.setSpacing(true);

        scroller = new Scroller(todoContainer);
        scroller.setWidthFull();
        scroller.getStyle().set("flex-grow", "1"); // Fill available space
        scroller.addClassName("smooth-scroller");

        loadingBar = new ProgressBar();
        loadingBar.setIndeterminate(true);
        loadingBar.setValue(0);
        loadingBar.addClassNames("loading-spinner", "hidden");

        promptField = new TextField();
        promptField.setPlaceholder("Message TodoAI...");
        promptField.setWidthFull();
        promptField.setPrefixComponent(VaadinIcon.MAGIC.create());
        promptField.addClassName("modern-pill-prompt");

       /* promptField.addKeyDownListener(Key.ENTER, event -> {
            String val = promptField.getValue();
            if (val != null && !val.isBlank()) {
                addUserChatBubble(val);
              //  processAiGeneration(val);
                promptField.clear();
            }
        });*/

        mainWrapper.add(scroller, loadingBar, promptField);
        add(mainWrapper);
        setupPromptLogic();
    }

    private void addUserChatBubble(String text) {
        Span bubble = new Span(text);
        bubble.addClassName("chat-bubble-user");
        HorizontalLayout wrapper = new HorizontalLayout(bubble);
        wrapper.setWidthFull();
        wrapper.setJustifyContentMode(JustifyContentMode.END);
        todoContainer.add(wrapper);
    }

    private void setupPromptLogic() {
        promptField.addKeyDownListener(Key.ENTER, event -> {
            String val = promptField.getValue();
            if (val != null && !val.isBlank()) {

                addUserChatBubble(val);
                loadingBar.removeClassName("hidden");
                promptField.setEnabled(false);
                promptField.clear();
                scrollToBottom();

                UI ui = UI.getCurrent();
                CompletableFuture.supplyAsync(() -> {
                    return iAiGenerateTodoService.generateAiTodo(val);
                }).thenAccept(todo -> {
                    ui.access(() -> {
                        loadingBar.addClassName("hidden");
                        promptField.setEnabled(true);
                        if (todo != null) {
                            renderAiResponse(todo);
                        } else {
                            todoContainer.add(new Span("Service unavailable."));
                        }
                        scrollToBottom();
                    });
                }).exceptionally(ex -> {
                    ui.access(() -> {
                        loadingBar.addClassName("hidden");
                        promptField.setEnabled(true);
                    });
                    return null;
                });
            }
        });
    }


    private void addTodoToList(TodoDTO todoDTO){


        ApiResponse response = this.iTodoService.addAiGenTodoList(todoDTO, 1);

        if(response !=null && response.getMessage() != null && response.getMessage().equals(SuccessOrFailedEnum.SUCCESS)){
            Notification.show("TODO save successfully");
        }
        else{
            Notification.show("Save failed");
        }
    }

    private void renderAiResponse(TodoDTO todoDTO) {
        VerticalLayout responseWrapper = new VerticalLayout();

        Button btnCreateTodo = new Button("Add to TODO list");
        btnCreateTodo.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_WARNING);
        responseWrapper.add(btnCreateTodo);

        btnCreateTodo.addClickListener(event -> {
           log.info("When trying to add todo in my list");
            addTodoToList(todoDTO);
        });

        responseWrapper.addClassName("chat-bubble-ai");
        responseWrapper.setPadding(true);
        responseWrapper.setSpacing(false);

        if (todoDTO.getTitle() != null) {
            H2 title = new H2(todoDTO.getTitle());
            title.getStyle().set("margin-top", "0");
            responseWrapper.add(title);
        }

      /*  if (todoDTO.getSubTitle() != null) {
            Span sub = new Span(todoDTO.getSubTitle());
            sub.addClassName("subtitle-text");
            responseWrapper.add(sub);
        }*/

        if (todoDTO.getTodoList() != null) {
            /*for (String list : todoDTO.getTodoList()) {
                addCategoryToResponse(responseWrapper, list.getTitle(), list.getTodoList());
            }*/
            addCategoryToResponse(responseWrapper,null /*list.getTitle()*/, todoDTO.getTodoList());
        }

        todoContainer.add(responseWrapper);
    }

    private void addCategoryToResponse(VerticalLayout parent, String catTitle, List<String> subTasks) {
        VerticalLayout catWrapper = new VerticalLayout();
        catWrapper.addClassName("category-card");
        catWrapper.setPadding(false);

        if (catTitle != null) {
            Span title = new Span(catTitle);
            title.addClassName("category-title");
            catWrapper.add(title);
        }

        for (String task : subTasks) {
            HorizontalLayout row = new HorizontalLayout();
            row.addClassName("task-row");
            Checkbox cb = new Checkbox();
            Span ts = new Span(task);
            cb.addValueChangeListener(e -> {
                ts.getStyle().set("text-decoration", e.getValue() ? "line-through" : "none");
                ts.getStyle().set("opacity", e.getValue() ? "0.5" : "1.0");
            });
            row.add(cb, ts);
            catWrapper.add(row);
        }
        parent.add(catWrapper);
    }

    private void scrollToBottom() {
        scroller.getElement().executeJs("this.scrollTop = this.scrollHeight;");
    }
}