package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import com.vaadin.ui.Grid;

import de.kreth.clubhelperbackend.google.calendar.ClubEvent;

public class EventGrid extends Grid<ClubEvent> {

	private static final long serialVersionUID = -5435770187868470290L;

	public EventGrid() {
		addColumn(ClubEvent::getStart).setCaption("Start");
		addColumn(ClubEvent::getCaption).setCaption("Bezeichnung");
		addColumn(ClubEvent::getLocation).setCaption("Ort");
	}

}
