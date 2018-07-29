package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinRequest;

import de.kreth.clubhelperbackend.google.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public class EventBusiness {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final List<ClubEvent> cache = new ArrayList<>();

	public List<ClubEvent> loadEvents(VaadinRequest request) {
		return loadEvents(request, false);
	}

	public synchronized List<ClubEvent> loadEvents(VaadinRequest request,
			boolean forceRefresh) {
		if (cache.isEmpty() == false && forceRefresh == false) {
			log.trace("Returning cached events: {}", cache);
			return Collections.unmodifiableList(cache);
		}

		log.debug(
				"Loading events from Google Calendar, cache size was: {}, forceRefresh={}",
				cache.size(), forceRefresh);
		cache.clear();

		BufferedWriter out = null;
		try {
			if (forceRefresh) {
				File f = new File("google_events.json");
				f.delete();
				out = new BufferedWriter(new FileWriter(f));

			}
			String remoteHost = "localhost";
			CalendarAdapter adapter = new CalendarAdapter();
			List<com.google.api.services.calendar.model.Event> events = adapter
					.getAllEvents(remoteHost);

			for (com.google.api.services.calendar.model.Event ev : events) {
				if (out != null) {
					out.write(ev.toPrettyString());
					out.newLine();
				}

				if ("cancelled".equals(ev.getStatus()) == false) {
					cache.add(ClubEvent.parse(ev));
				} else {
					log.debug("Cancelled: {}", ev.getSummary());
				}
			}
		} catch (GeneralSecurityException | IOException
				| InterruptedException e) {
			log.error("Error loading events from google.", e);
		}

		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				log.error("Error writing File", e);
			}
		}
		return Collections.unmodifiableList(cache);
	}
}
