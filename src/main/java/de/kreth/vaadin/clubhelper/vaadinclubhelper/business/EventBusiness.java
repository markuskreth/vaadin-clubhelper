package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung.EventMeldung;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AltersgruppeDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Component
public class EventBusiness {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ClubEventDao clubEventDao;

	@Autowired
	AltersgruppeDao altersgruppeDao;

	private ClubEvent current;

	public synchronized List<ClubEvent> loadEvents() {
		List<ClubEvent> list = clubEventDao.listAll();
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
			try {
				clubEventDao.addPersons(current, selected);
				log.info("Updated {}, {} with participants: {}", current.getCaption(), current.getStart(), selected);
			} catch (Exception e) {
				log.error("Unable to update Event {}, {}, {} with participants: {}", current.getId(),
						current.getCaption(), current.getStart(), selected, e);
				throw e;
			}
		}
	}

	public Altersgruppe createAltersgruppe() {
		Altersgruppe e = new Altersgruppe();
		e.setStart(LocalDateTime.now().getYear() - 1);
		e.setEnd(LocalDateTime.now().getYear());
		e.setChanged(new Date());
		e.setCreated(e.getChanged());
		Set<Altersgruppe> altersgruppen = current.getAltersgruppen();
		if (altersgruppen.contains(e)) {
			for (Altersgruppe el : altersgruppen) {
				if (el.equals(e)) {
					return el;
				}
			}
		} else {
			altersgruppen.add(e);
			e.setClubEvent(current);
		}

		return e;
	}

	public void storeAltersgruppe(Altersgruppe edited) {
		altersgruppeDao.save(edited);
		clubEventDao.save(current);
	}

	public EventMeldung createMeldung() {
		return new EventMeldung(current);
	}

	public void storeEventType() {
		clubEventDao.updateEventType(current);
	}

	public void delete(ClubEvent bean) {
		clubEventDao.delete(bean);
		current = null;
	}
}
