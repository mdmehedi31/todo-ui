package com.todo.ui.view;

import com.todo.ui.todoService.IAiGenerateTodoService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("todo-generator")
public class TodoGeneratorView extends VerticalLayout {

    private final VerticalLayout todoContainer;
    private final TextField promptField;
    private final Scroller scroller;
    private IAiGenerateTodoService aiGenerateTodoService;

    public TodoGeneratorView(IAiGenerateTodoService aiGenerateTodoService) {

        this.aiGenerateTodoService = aiGenerateTodoService;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("generative-prompt-view");

        // --- Main UI Wrapper ---
        VerticalLayout mainWrapper = new VerticalLayout();
        mainWrapper.setWidth("500px");
        mainWrapper.setMaxWidth("90vw"); // Responsive width
        mainWrapper.setPadding(false);
        mainWrapper.setSpacing(false);
        mainWrapper.getStyle().set("margin-top", "-5vh");

        // 1. The Scroller and Container
        todoContainer = new VerticalLayout();
        todoContainer.setPadding(false);
        todoContainer.setSpacing(true);
        // Ensure items "stack" from the bottom up to meet the prompt
        todoContainer.setJustifyContentMode(JustifyContentMode.END);

        scroller = new Scroller(todoContainer);
        scroller.setHeight("350px");
        scroller.setWidthFull();
        scroller.getStyle().set("margin-bottom", "10px"); // Space between list and input
        scroller.addClassName("smooth-scroller");

        // 2. The Input Prompt
        promptField = new TextField();
        promptField.setPlaceholder("What topic can I generate tasks for?");
        promptField.setWidthFull();
        promptField.setClearButtonVisible(true);

        // Changed MAGICPLANE to MAGIC or SPARK_LINE for compatibility
        Icon prefixIcon = VaadinIcon.MAGIC.create();
        prefixIcon.getStyle().set("color", "var(--lumo-primary-color)");
        promptField.setPrefixComponent(prefixIcon);

        promptField.addClassName("modern-pill-prompt");

        promptField.addKeyDownListener(Key.ENTER, event -> {
            String value = promptField.getValue();
            if (value != null && !value.isBlank()) {
                addModernTodoItem(value);
                promptField.clear();
            }
        });

        mainWrapper.add(scroller, promptField);
        add(mainWrapper);
    }

    private void addModernTodoItem(String topic) {
        HorizontalLayout card = new HorizontalLayout();
        card.setWidthFull();
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(Alignment.CENTER);
        card.addClassName("vibrant-todo-card");

        // Checkbox first for better "ToDo" feel
        Checkbox done = new Checkbox();

        Span taskText = new Span(topic);
        taskText.addClassName("todo-card-text");

        Button deleteBtn = new Button(VaadinIcon.TRASH.create());
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        deleteBtn.addClassName("todo-delete-btn");
        deleteBtn.addClickListener(e -> todoContainer.remove(card));

        card.add(done, taskText, deleteBtn);
        card.expand(taskText);

        // Add as first to make it pop up right above the input
        todoContainer.addComponentAsFirst(card);

        // Auto-scroll to ensure the new item is visible
        scrollToTop();
    }

    private void scrollToTop() {
        scroller.getElement().executeJs("this.scrollTop = 0;");
    }
}