package com.todo.ui.view;



import com.todom.library.dto.AttendeeDTO;
import com.todom.library.dto.CalendarEventDTO;
import com.vaadin.flow.component.Html;
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
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.util.StreamUtils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EventDetailsDialog extends Dialog {

    private CalendarEventDTO calendarEventDTO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.ENGLISH);
    public EventDetailsDialog(CalendarEventDTO calendarEventDTO) {

        this.calendarEventDTO = calendarEventDTO;

        this.setWidth("500px");
        this.setMaxWidth("90vw");
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);
       // layout.getStyle().setWidth("50%");
        layout.getStyle().setMarginLeft("-17px");
        layout.getStyle().setMarginTop("-20px");
        layout.getStyle().setMarginBottom("-20px");
        layout.getStyle().setMarginRight("-17px");

        layout.add(
                createHeaderSection(),
                new Hr(),
                createDateTimeSection(),
              //  createMeetingLinkSection(),
                createDescriptionSection(),
                createOrganizerSection(),
                createAttendeesSection(),
                //createMetadataSection(),
              //  new Hr(),
                createFooterActions()
        );

        add(layout);
    }

    private HorizontalLayout createHeaderSection() {
        H2 title = new H2("Untitled Event");
        title.getStyle().setPaddingRight("10px");
        if(calendarEventDTO!=null && calendarEventDTO.getSummary() != null)
            title.setText(calendarEventDTO.getSummary());

        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);

        Span statusBadge = new Span("Scheduled");
        statusBadge.getElement().getThemeList().add("badge success");

      /*  Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL), e -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);*/

        HorizontalLayout header = new HorizontalLayout(title, statusBadge/*, closeButton*/);
      //  header.setWidthFull();
        header.setSpacing(false);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(title);

        return header;
    }

    private VerticalLayout createDateTimeSection() {

        String fromDateValue =null;
        String toDateValue =null;

        if(calendarEventDTO!=null && calendarEventDTO.getStartDate() != null)
            fromDateValue = calendarEventDTO.getStartDate().format(formatter);

        if(calendarEventDTO!=null && calendarEventDTO.getEnd() != null)
            toDateValue = calendarEventDTO.getEnd().format(formatter);

        Span from = new Span();
        from.getStyle().setPaddingLeft("5px");
        from.getStyle().setPaddingRight("5px");
       // from.add(new Html(" From :  <div><b>"+fromDateValue+"</div>"));
        Span to = new Span();
        to.getStyle().setPaddingLeft("5px");
        //to.add(new Html(" To :  <div><b>"+toDateValue+"</div>"));
        from.add(new Html("From : <div><b>" + fromDateValue + "</b></div>"));
        to.add(new Html("To : <div><b>" + toDateValue + "</b></div>"));

        Span duration = new Span();
        duration.getStyle().setDisplay(Style.Display.FLEX);
        if(fromDateValue == null && toDateValue == null){
            duration.getStyle().setPaddingLeft("5px");
            duration.setText("Not Specified");
        }
        else {
            duration.add(from, new Span(" - "),to);
        }
        Span date = new Span(new Icon(VaadinIcon.CALENDAR), duration);
        date.getStyle().setDisplay(Style.Display.FLEX);
      //  Span time = new Span(new Icon(VaadinIcon.CLOCK), new Span(" 08:56 AM - 09:56 AM (1 hr duration)"));

        String timeZone =" Not Specified";
        if (calendarEventDTO !=null && calendarEventDTO.getTimeZone() != null)
            timeZone=" "+calendarEventDTO.getTimeZone();

        Span timezone = new Span(new Icon(VaadinIcon.GLOBE), new Span(timeZone));

        VerticalLayout dateTimeLayout = new VerticalLayout(date, timezone);
        dateTimeLayout.setSpacing(false);
        dateTimeLayout.setPadding(false);
        dateTimeLayout.addClassNames(LumoUtility.TextColor.SECONDARY);
        return dateTimeLayout;
    }

   /* private VerticalLayout createMeetingLinkSection() {
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
    }*/

    private VerticalLayout createDescriptionSection() {
        H4 title = new H4("Description");
        title.addClassNames(LumoUtility.Margin.Bottom.XSMALL, LumoUtility.Margin.Top.MEDIUM);

        Div content = new Div();
        String description = null;

        content.getStyle().setWidth("456px");
        content.getStyle().set("overflow-wrap","break-word");
        if(calendarEventDTO!=null && calendarEventDTO.getDescription() != null)
            description = calendarEventDTO.getDescription();

         content.add(new Html("<div>"+description+"</div>"));
        content.addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Padding.MEDIUM, LumoUtility.BorderRadius.MEDIUM);
        content.setWidthFull();

        VerticalLayout layout = new VerticalLayout(title, content);
        layout.setVisible(description!=null);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setWidthFull();
        return layout;
    }

    private VerticalLayout createOrganizerSection() {
        H4 title = new H4("Organizer");
        title.addClassNames(LumoUtility.Margin.Bottom.XSMALL);

        Icon icon= VaadinIcon.MALE.create();
        Span avatar = new Span();
        avatar.add(icon);

        Span name = new Span("");

        String organizer = null;
        if(calendarEventDTO!=null && calendarEventDTO.getOrganizer() != null) {
            organizer = calendarEventDTO.getOrganizer();
            if (organizer.contains("@")){
                String organizerName = organizer;
                name.setText(organizerName.split("@")[0]);
            }else {
                name.setText(organizer);
            }
        }

        Anchor email = new Anchor();
        if (organizer != null) {
            email.setHref("mailto:" + organizer);
            email.setText(organizer);
        }

        email.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        VerticalLayout contactDetails = new VerticalLayout(name, email);
        contactDetails.setSpacing(false);
        contactDetails.setPadding(false);

        HorizontalLayout organizerRow = new HorizontalLayout(avatar, contactDetails);
        organizerRow.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout layout = new VerticalLayout(title, organizerRow);
        layout.setSpacing(false);
        layout.getStyle().setPaddingTop("10px");
        layout.setPadding(false);
        return layout;
    }

    private VerticalLayout createAttendeesSection() {

        Integer attendeeSize = calendarEventDTO.getAttendees()!=null?calendarEventDTO.getAttendees().size():0;
        H4 title = new H4("Attendees ("+attendeeSize+")");
        title.addClassNames(LumoUtility.Margin.Bottom.XSMALL);

        VerticalLayout attendeeList = new VerticalLayout();
        attendeeList.setPadding(false);
        attendeeList.setSpacing(false);

        if(calendarEventDTO !=null && calendarEventDTO.getAttendees()!=null) {
            for (AttendeeDTO attendeeDTO : calendarEventDTO.getAttendees()){
                String badgeValue="badge success";
                if(!attendeeDTO.getResponseStatus().equalsIgnoreCase("accepted"))
                    badgeValue="badge";

                attendeeList.add(createAttendeeRow(attendeeDTO.getEmail(), attendeeDTO.getResponseStatus(),badgeValue));
            }
        }
        // Example Attendee 1
        /*attendeeList.add(createAttendeeRow("hasanmehedi2271@gmail.com", "Pending", "badge"));
        // Example Attendee 2
        attendeeList.add(createAttendeeRow("mehedichowdhury31@gmail.com", "Accepted", "badge success"));
*/
        VerticalLayout layout = new VerticalLayout(title, attendeeList);
        layout.setVisible(attendeeSize>0);
        layout.setPadding(false);
        return layout;
    }

    private HorizontalLayout createAttendeeRow(String email, String status, String badgeTheme) {

        Icon humanIcon = VaadinIcon.MALE.create();
        Span avatar = new Span();
        avatar.add(humanIcon);
        Span emailSpan = new Span(email);
        Span statusBadge = new Span(status);
        statusBadge.getElement().getThemeList().add(badgeTheme);

        HorizontalLayout row = new HorizontalLayout(avatar, emailSpan, statusBadge);
       // row.setWidthFull();
       // row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        //row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.addClassNames(LumoUtility.Padding.Vertical.XSMALL);
        return row;
    }

   /* private Details createMetadataSection() {
        VerticalLayout metaData = new VerticalLayout();
        metaData.setPadding(false);
        metaData.setSpacing(false);
        metaData.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        metaData.add(new Span("Event ID: 3025-8412-392-I5521"));
        metaData.add(new Span("Calendar ID: primary"));
        metaData.add(new Span("Created: 2025-02-22 08:56 AM"));

        Details details = new Details("Event Information", metaData);
        return details;
    }*/

    private Button createFooterActions() {
       /* Button editBtn = new Button("Edit Event");
        editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteBtn = new Button("Delete Event");
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);*/

        Button closeBtn = new Button("Close", e -> this.close());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_ERROR);
        closeBtn.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        HorizontalLayout footer = new HorizontalLayout(/*editBtn, deleteBtn, */closeBtn);
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        return closeBtn;
    }
}