package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public class SingleEventView extends CustomComponent {

	private static final long serialVersionUID = 4701035948083549772L;

	private final TextField textTitle;
	private final TextField textLocation;

	private DateField startDate;

	private DateField endDate;

	public SingleEventView() {
		setCaption("Gewählte Veranstaltung");
		addStyleName("bold-caption");
		setWidth(50.0f, Unit.PERCENTAGE);

		textTitle = new TextField();
		textTitle.setId("event.title");
		textTitle.setStyleName("title_label");
		textTitle.setCaption("Veranstaltung");
		textTitle.setEnabled(false);
		textTitle.setSizeFull();

		textLocation = new TextField();
		textLocation.setId("event.location");
		textLocation.setStyleName("title_label");
		textLocation.setCaption("Ort");
		textLocation.setEnabled(false);
		textLocation.setSizeFull();

		startDate = new DateField("Beginn");
		startDate.setEnabled(false);
		endDate = new DateField("Ende");
		endDate.setEnabled(false);

		textLocation.setHeight(endDate.getHeight(), endDate.getHeightUnits());

		GridLayout layout = new GridLayout(2, 2);
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponents(textTitle, startDate, textLocation, endDate);
		setCompositionRoot(layout);
	}

	void setTitle(String value) {
		if (value == null) {
			value = "";
		}
		textTitle.setValue(value);
	}

	void setLocation(String value) {
		if (value == null) {
			value = "";
		}
		textLocation.setValue(value);
	}

	public void setEvent(ClubEvent ev) {

		if (ev != null) {

			setTitle(ev.getCaption());
			setLocation(ev.getLocation());
			setStartDate(ev.getStart());
			setEndDate(ev);

		} else {
			setTitle("");
			setLocation("");
			endDate.setVisible(false);
		}
	}

	private void setEndDate(ClubEvent ev) {
		ZonedDateTime start = ev.getStart();
		ZonedDateTime end = ev.getEnd();
		if (start.until(end, ChronoUnit.DAYS) > 0) {
			endDate.setValue(end.toLocalDate());
			endDate.setVisible(true);
		} else {
			endDate.setValue(null);
			endDate.setVisible(false);
		}
	}

	private void setStartDate(ZonedDateTime start) {
		startDate.setValue(start.toLocalDate());
	}

}
