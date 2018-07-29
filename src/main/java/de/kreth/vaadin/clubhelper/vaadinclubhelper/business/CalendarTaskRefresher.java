package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

@Component
public class CalendarTaskRefresher {

	private static final long RATE = 1000 * 60 * 10;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ClubEventDao dao;

	EventBusiness business = new EventBusiness();

	@Scheduled(fixedDelay = RATE)
	public void synchronizeCalendarTasks() {
		List<ClubEvent> events = business.loadEvents(null, true);
		for (ClubEvent e : events) {
			log.trace("try storing {}", e);
			dao.save(e);
			log.debug("successfully stored {}", e);
		}
	}
}
