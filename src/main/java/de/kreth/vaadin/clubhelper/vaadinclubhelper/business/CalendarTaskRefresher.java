package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.kreth.clubhelperbackend.google.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

@Component
public class CalendarTaskRefresher {

	public static final String SKIP_EVENT_UPDATE = "skipEventUpdate";

	private static final long RATE = 1000L * 60 * 10;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final boolean skip = Boolean.parseBoolean(System.getProperty(SKIP_EVENT_UPDATE, "false"));

	@Autowired
	ClubEventDao dao;
	@Autowired
	CalendarAdapter calendarAdapter;

	@Scheduled(fixedDelay = RATE)
	public void synchronizeCalendarTasks() {
		if (skip) {
			return;
		}

		List<ClubEvent> list = loadEventsFromGoogle();

		for (ClubEvent e : list) {
			if (dao.get(e.getId()) == null) {
				try {
					log.trace("try inserting {}", e);
					dao.save(e);
				} catch (ConstraintViolationException | DataIntegrityViolationException ex) {
					log.warn("Insert failed, updating {}", e);
					dao.update(e);
				}
			} else {
				log.trace("try updating {}", e);
				dao.update(e);
			}
			log.debug("successfully stored {}", e);
		}
	}

	public List<ClubEvent> loadEventsFromGoogle() {

		log.debug("Loading events from Google Calendar");

		List<ClubEvent> list = new ArrayList<>();

		try (FileExporter ex = FileExporter.builder(log).setFileName("google_events.json").setAppend(false).disable()
				.build()) {

			String remoteHost = "localhost";

			List<com.google.api.services.calendar.model.Event> events = calendarAdapter.getAllEvents(remoteHost);

			for (com.google.api.services.calendar.model.Event ev : events) {
				ex.writeLine(ev.toPrettyString());

				if ("cancelled".equals(ev.getStatus())) {
					log.debug("Cancelled: {}", ev.getSummary());
				} else {
					list.add(ClubEvent.parse(ev));
				}
			}

		} catch (GeneralSecurityException | IOException | InterruptedException e) {
			log.error("Error loading events from google.", e);
		} catch (Exception e1) {
			log.warn("Error closing " + FileExporter.class.getSimpleName(), e1);
		}
		return list;
	}

	public void setDao(ClubEventDao dao) {
		this.dao = dao;
	}

	public void setCalendarAdapter(CalendarAdapter calendarAdapter) {
		this.calendarAdapter = calendarAdapter;
	}
}
