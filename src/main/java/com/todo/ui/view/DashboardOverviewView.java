package com.todo.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value="dashboard-overview",layout = MainLayout.class)
@PageTitle("Dashboard")
public class DashboardOverviewView extends VerticalLayout {

    public DashboardOverviewView() {
        addClassName("dashboard-container");
        setSizeFull();
        setSpacing(false);
        setPadding(false);

        add(createHeader());
        add(createStatsSection());
        add(createBottomSection());
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("dashboard-header");
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(Alignment.CENTER);

        // Left Side: Title and Breadcrumb
        VerticalLayout titleArea = new VerticalLayout(
                new H2("Dashboard Overview"),
                new Span("Home / Dashboard")
        );
        titleArea.setPadding(false);
        titleArea.setSpacing(false);
        titleArea.addClassName("header-title-wrapper");

        // Right Side: Actions
        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(Alignment.BASELINE);
        actions.setSpacing(true);

        Select<String> range = new Select<>();
        range.setItems("May 1, 2026 - May 31, 2026");
        range.setValue("May 1, 2026 - May 31, 2026");
        range.setLabel("Filtered by: Current Month");
        range.setPrefixComponent(VaadinIcon.CALENDAR.create());
        range.addClassName("custom-date-select");

        Button addBtn = new Button("AI TODO Generator", VaadinIcon.PLUS.create());
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBtn.addClassName("add-task-button");

        addBtn.addClickListener(e->{
            UI.getCurrent().navigate(TodoGeneratorView.class);
        });

        actions.add(range, addBtn);

        header.add(titleArea, actions);

        // FIX: Use setFlexGrow to push actions to the right
        header.setFlexGrow(1.0, titleArea);

        return header;
    }

    private Component createHeaderRow(String title, VaadinIcon icon, String iconClass) {
        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(true); // Small gap between text and icon
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

    private Component createStatsSection() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.addClassName("stats-row");

        // 1. In Progress
        layout.add(createProgressCard("IN PROGRESS", "24", VaadinIcon.COG, "blue-text"));

        // 2. Overdue
        layout.add(createOverdueCard("OVERDUE", "13", List.of("Website Revamp", "Client Feedback Report", "Marketing Proposal")));

        // 3. Done
        layout.add(createDoneCard("DONE", "68", "92%"));

        return layout;
    }


    private Component createProgressCard(String title, String val, VaadinIcon icon, String colorClass) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames("info-card", "stat-card-fixed");

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
        // Change to START to align title with other cards
        card.setAlignItems(Alignment.START);
        card.setPadding(true);

        VerticalLayout leftSide = new VerticalLayout();
        leftSide.setPadding(false);
        leftSide.setSpacing(false);
        leftSide.setAlignItems(Alignment.START);

        // Group Title and Icon exactly like the others
        leftSide.add(createHeaderRow(title, VaadinIcon.CHECK_CIRCLE, "green-icon"),
                new H1(val),
                new Span("Completion vs. Goal"));

        // Ensure style classes match the other cards
        leftSide.getComponentAt(1).getElement().getClassList().add("big-number");
        leftSide.getComponentAt(2).getElement().getClassList().add("small-muted-text");

        // Circular progress container
        Div circleContainer = new Div();
        circleContainer.addClassName("circle-progress-wrapper");

        Div circle = new Div(new Span(percent));
        circle.addClassName("progress-circle-ui");
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

        VerticalLayout trendCard = new VerticalLayout(new H4("TASK PROGRESS TREND"));
        trendCard.addClassNames("info-card", "bottom-card-height");

        VerticalLayout activityCard = new VerticalLayout(new H4("RECENT TASKS & ACTIVITY"));
        activityCard.addClassNames("info-card", "bottom-card-height");

        Grid<String[]> grid = new Grid<>();
        grid.addColumn(s -> s[0]).setHeader("Task");
        grid.addComponentColumn(s -> createBadge(s[1])).setHeader("Priority");
        grid.addColumn(s -> s[2]).setHeader("Due");
        grid.addComponentColumn(s -> createStatusBadge(s[3])).setHeader("Status");

        grid.setItems(
                new String[]{"Finalize Budget", "High Priority", "May 29", "Done"},
                new String[]{"Update Inventory", "Medium", "June 3", "In Progress"},
                new String[]{"Onboard New Hire", "Low", "May 15", "Overdue"}
        );
        grid.addThemeVariants();
        grid.addClassName("dashboard-grid");

        activityCard.add(grid);
        layout.add(trendCard, activityCard);
        layout.setFlexGrow(1.5, trendCard);
        return layout;
    }

    private Span createBadge(String text) {
        Span s = new Span(text);
        s.addClassNames("badge-ui", "priority-" + text.toLowerCase().replace(" ", "-"));
        return s;
    }

    private Span createStatusBadge(String text) {
        Span s = new Span(text);
        s.addClassNames("badge-ui", "status-" + text.toLowerCase().replace(" ", "-"));
        return s;
    }
}