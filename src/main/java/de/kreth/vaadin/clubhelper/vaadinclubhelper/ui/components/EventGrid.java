package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.vaadin.ui.Grid;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public class EventGrid extends Grid<ClubEvent> {

	private static final long serialVersionUID = -5435770187868470290L;
	private transient final DateTimeFormatter df = DateTimeFormatter
			.ofLocalizedDate(FormatStyle.MEDIUM);

	public EventGrid() {

		addColumn(ClubEvent::getStart, dt -> {
			return dt != null ? df.format(dt) : "";
		}).setCaption("Start");
		addColumn(ClubEvent::getCaption).setCaption("Bezeichnung");
		addColumn(ClubEvent::getLocation).setCaption("Ort");
	}

}
