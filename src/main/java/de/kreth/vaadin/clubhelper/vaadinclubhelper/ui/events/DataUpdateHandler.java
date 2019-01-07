package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.events;

public interface DataUpdateHandler {

	void add(DataUpdatedEvent ev);

	boolean remove(DataUpdatedEvent ev);
}
