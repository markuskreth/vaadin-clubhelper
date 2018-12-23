package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Component
public class EventBusiness {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ClubEventDao dao;

	private ClubEvent current;

	public synchronized List<ClubEvent> loadEvents() {

		List<ClubEvent> list = dao.listAll();
		log.trace("Returning events from database: {}", list);
		return list;

	}

	public ClubEvent getCurrent() {
		return current;
	}

	public void setSelected(ClubEvent ev) {
		this.current = ev;
	}

	public void changePersons(Set<Person> selected) {
		if (current != null) {
			for (Person p : selected) {
				current.add(p);
			}
			try {
				dao.update(current);
				log.info("Updated {}, {} with participants: {}", current.getCaption(), current.getStart(), selected);
			} catch (Exception e) {
				log.error("Unable to update Event {}, {}, {} with participants: {}", current.getId(),
						current.getCaption(), current.getStart(), selected, e);
			}
		}
	}
}
