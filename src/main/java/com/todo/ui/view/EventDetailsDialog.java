package com.todo.ui.view;



import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class EventDetailsDialog extends Dialog {

    public EventDetailsDialog() {
        this.setWidth("750px");
        this.setMaxWidth("90vw");
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);

        layout.add(
                createHeaderSection(),
                new Hr(),
                createDateTimeSection(),
                createMeetingLinkSection(),
                createDescriptionSection(),
                createOrganizerSection(),
                createAttendeesSection(),
                createMetadataSection(),
                new Hr(),
                createFooterActions()
        );

        add(layout);
    }

    private HorizontalLayout createHeaderSection() {
        H2 title = new H2("New Job Meeting");
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);

        Span statusBadge = new Span("Scheduled");
        statusBadge.getElement().getThemeList().add("badge success");

        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL), e -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout header = new HorizontalLayout(title, statusBadge, closeButton);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(title);

        return header;
    }

    private VerticalLayout createDateTimeSection() {
        Span date = new Span(new Icon(VaadinIcon.CALENDAR), new Span(" Wednesday, Oct 22, 2025"));
        Span time = new Span(new Icon(VaadinIcon.CLOCK), new Span(" 08:56 AM - 09:56 AM (1 hr duration)"));
        Span timezone = new Span(new Icon(VaadinIcon.GLOBE), new Span(" Asia/Dhaka"));

        VerticalLayout dateTimeLayout = new VerticalLayout(date, time, timezone);
        dateTimeLayout.setSpacing(false);
        dateTimeLayout.setPadding(false);
        dateTimeLayout.addClassNames(LumoUtility.TextColor.SECONDARY);
        return dateTimeLayout;
    }

    private VerticalLayout createMeetingLinkSection() {
        Div card = new Div();
        card.addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Padding.MEDIUM, LumoUtility.BorderRadius.MEDIUM);
        card.setWidthFull();

        Span meetLabel = new Span("🎥 Google Meet: ");
        Anchor link = new Anchor("https://meet.google.com/abc-defg-hij", "https://meet.google.com/abc-defg-hij");
        link.setTarget("_blank");

        Button copyButton = new Button("Copy Link", new Icon(VaadinIcon.COPY));
        copyButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout linkRow = new HorizontalLayout(meetLabel, link, copyButton);
        linkRow.setAlignItems(FlexComponent.Alignment.CENTER);

        Button joinButton = new Button("Join Meeting");
        joinButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        joinButton.setWidthFull();

        VerticalLayout layout = new VerticalLayout(linkRow, joinButton);
        layout.setPadding(false);
        card.add(layout);

        return new VerticalLayout(card);
    }

    private VerticalLayout createDescriptionSection() {
        H4 title = new H4("Description");
        title.addClassNames(LumoUtility.Margin.Bottom.XSMALL, LumoUtility.Margin.Top.MEDIUM);

        Div content = new Div();
        content.setText("This meeting will be held for taking job interview of the new client.");
        content.addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Padding.MEDIUM, LumoUtility.BorderRadius.MEDIUM);
        content.setWidthFull();

        VerticalLayout layout = new VerticalLayout(title, content);
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

    private VerticalLayout createOrganizerSection() {
        H4 title = new H4("Organizer");
        title.addClassNames(LumoUtility.Margin.Bottom.XSMALL);

        Span avatar = new Span("👤"); // Can be replaced with an actual image/Avatar component
        Span name = new Span("Md Mehedi Hasan");
        Anchor email = new Anchor("mailto:mdmehedihasan.mh31@gmail.com", "mdmehedihasan.mh31@gmail.com");
        email.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        VerticalLayout contactDetails = new VerticalLayout(name, email);
        contactDetails.setSpacing(false);
        contactDetails.setPadding(false);

        HorizontalLayout organizerRow = new HorizontalLayout(avatar, contactDetails);
        organizerRow.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout layout = new VerticalLayout(title, organizerRow);
        layout.setPadding(false);
        return layout;
    }

    private VerticalLayout createAttendeesSection() {
        H4 title = new H4("Attendees (3)");
        title.addClassNames(LumoUtility.Margin.Bottom.XSMALL);

        VerticalLayout attendeeList = new VerticalLayout();
        attendeeList.setPadding(false);
        attendeeList.setSpacing(false);

        // Example Attendee 1
        attendeeList.add(createAttendeeRow("hasanmehedi2271@gmail.com", "Pending", "badge"));
        // Example Attendee 2
        attendeeList.add(createAttendeeRow("mehedichowdhury31@gmail.com", "Accepted", "badge success"));

        VerticalLayout layout = new VerticalLayout(title, attendeeList);
        layout.setPadding(false);
        return layout;
    }

    private HorizontalLayout createAttendeeRow(String email, String status, String badgeTheme) {
        Span avatar = new Span("👤");
        Span emailSpan = new Span(email);
        Span statusBadge = new Span(status);
        statusBadge.getElement().getThemeList().add(badgeTheme);

        HorizontalLayout row = new HorizontalLayout(avatar, emailSpan, statusBadge);
        row.setWidthFull();
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.addClassNames(LumoUtility.Padding.Vertical.XSMALL);
        return row;
    }

    private Details createMetadataSection() {
        VerticalLayout metaData = new VerticalLayout();
        metaData.setPadding(false);
        metaData.setSpacing(false);
        metaData.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        metaData.add(new Span("Event ID: 3025-8412-392-I5521"));
        metaData.add(new Span("Calendar ID: primary"));
        metaData.add(new Span("Created: 2025-02-22 08:56 AM"));

        Details details = new Details("Event Information", metaData);
        return details;
    }

    private HorizontalLayout createFooterActions() {
        Button editBtn = new Button("Edit Event");
        editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteBtn = new Button("Delete Event");
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button closeBtn = new Button("Close", e -> this.close());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout footer = new HorizontalLayout(editBtn, deleteBtn, closeBtn);
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        return footer;
    }
}