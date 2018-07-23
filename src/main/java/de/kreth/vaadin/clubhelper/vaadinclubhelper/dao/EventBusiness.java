package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.server.VaadinRequest;

import de.kreth.clubhelperbackend.google.calendar.CalendarAdapter;
import de.kreth.clubhelperbackend.google.calendar.ClubEvent;

public class EventBusiness {

	private final List<ClubEvent> cache = new ArrayList<>();

	public List<ClubEvent> loadEvents(VaadinRequest request) {
		if (cache.isEmpty() == false) {
			return Collections.unmodifiableList(cache);
		}

		cache.clear();

		try {

			// String remoteHost = request.getRemoteHost();
			String remoteHost = "localhost";
			CalendarAdapter adapter = new CalendarAdapter();
			List<com.google.api.services.calendar.model.Event> events = adapter
					.getAllEvents(remoteHost);

			for (com.google.api.services.calendar.model.Event ev : events) {
				if ("cancelled".equals(ev.getStatus()) == false) {
					cache.add(ClubEvent.parse(ev));
				} else {
					System.out.println("Cancelled: " + ev.getSummary());
				}
			}
		} catch (GeneralSecurityException | IOException
				| InterruptedException e) {
			e.printStackTrace();
		}
		return Collections.unmodifiableList(cache);
	}
}
