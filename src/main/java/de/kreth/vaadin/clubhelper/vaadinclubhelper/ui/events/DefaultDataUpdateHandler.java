package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.events;

import java.util.ArrayList;
import java.util.List;

public class DefaultDataUpdateHandler implements DataUpdateHandler {

	private final List<DataUpdatedEvent> listeners = new ArrayList<>();

	@Override
	public void add(DataUpdatedEvent ev) {
		listeners.add(ev);
	}

	@Override
	public boolean remove(DataUpdatedEvent o) {
		return listeners.remove(o);
	}

	public void fireUpdateEvent() {
		synchronized (listeners) {
			for (DataUpdatedEvent ev : listeners) {
				ev.updateFinisched();
			}
		}
	}
}
