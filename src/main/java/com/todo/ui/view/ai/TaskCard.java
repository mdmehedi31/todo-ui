package com.todo.ui.view.ai;


import com.todo.ui.todoService.ITodoService;
import com.todo.ui.view.AddTodoItemDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.shared.Tooltip;

public class TaskCard extends VerticalLayout {

    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;

    public TaskCard(String title, String startDate, String endDate, Integer todoCount, Integer progress) {
        addClassName("task-card");
        setSpacing(false);
        setPadding(true);
        setWidth("300px");
        setHeight("240px");
        // Title Section
        H3 taskTitle = new H3(title);
        Tooltip.forComponent(taskTitle)
                .setText(title);
        taskTitle.addClassName("task-title");

        // Date Badge Row
        HorizontalLayout dates = new HorizontalLayout();
        dates.addClassName("date-row");
        dates.setWidthFull();

        Span start = new Span(startDate);
        start.addClassNames("date-badge", "start-date");

        Span end = new Span(endDate);
        end.addClassNames("date-badge", "end-date");

        dates.add(start, end);
        dates.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Progress Section
        Span progressLabel = new Span(todoCount + " Todos • " + progress + "% Done");
        progressLabel.addClassName("progress-label");

        ProgressBar bar = new ProgressBar();
        bar.setValue(progress / 100.0);
        bar.addClassName("custom-progress-bar");

        // Action Buttons
        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("action-row");

         addBtn = createIconButton(VaadinIcon.PLUS, "action-add");
         editBtn = createIconButton(VaadinIcon.EDIT, "action-edit");
         deleteBtn = createIconButton(VaadinIcon.TRASH, "action-delete");

        actions.add(addBtn, editBtn, deleteBtn);

        add(taskTitle, dates, progressLabel, bar, actions);
    }

    private Button createIconButton(VaadinIcon vIcon, String className) {
        Button btn = new Button(vIcon.create());
        btn.addClassNames("task-icon-btn", className);
        return btn;
    }

    public Button getAddBtn() {
        return addBtn;
    }
    public Button getEditBtn() {
        return editBtn;
    }
    public Button getDeleteBtn() {
        return deleteBtn;
    }
}