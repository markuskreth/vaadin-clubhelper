package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.IdentifierLoadAccess;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class ClubEventDataTest extends AbstractDatabaseTest {

	@Test
	public void testSaveAndSelectEvent() {
		ClubEvent ev = creteEvent();

		transactional(() -> session.save(ev));
		Query<ClubEvent> query = session.createNamedQuery("ClubEvent.findAll", ClubEvent.class);
		List<ClubEvent> result = query.list();
		assertEquals(1, result.size());
	}

	@Test
	public void testEventWithPerson() {

		ClubEvent ev = creteEvent();

		Person person = testInsertPerson();
		Set<Person> persons = ev.getPersons();
		if (persons == null) {
			persons = new HashSet<>();
			ev.setPersons(persons);
		}
		persons.add(person);

		transactional(() -> session.save(ev));

		List<ClubeventHasPerson> loadEventPersons = loadEventPersons();
		assertFalse(loadEventPersons.isEmpty());
		ClubeventHasPerson link = loadEventPersons.get(0);
		assertEquals(person.getId(), link.getPersonId());
		assertEquals(ev.getId(), link.getClubEventId());

	}

	@Test
	public void changeEventsPersons() {
		ClubEvent ev = creteEvent();

		transactional(() -> session.save(ev));

		Person person = testInsertPerson();

		Person person2 = new Person();
		person2.setId(person.getId() + 1);
		person2.setPrename(person.getPrename() + "_2");
		person2.setSurname(person.getSurname() + "_2");
		person2.setBirth(LocalDate.now());

		transactional(() -> session.save(person2));

		IdentifierLoadAccess<ClubEvent> idLoadAccess = session.byId(ClubEvent.class);
		ClubEvent loaded = idLoadAccess.load(ev.getId());
		assertNotNull(loaded);
		if (loaded.getPersons() == null) {
			loaded.setPersons(new HashSet<>());
		}

		loaded.add(person);
		transactional(() -> session.update(ev));

		loaded.add(person2);
		transactional(() -> session.update(ev));

		List<ClubeventHasPerson> entries = loadEventPersons();
		assertEquals(2, entries.size());
	}

	@Test
	public void testChangeEventParticipantsRapidly() {

		final ClubEvent created = creteEvent();
		transactional(() -> {
			session.save(created);
		});

		final ClubEvent ev = session.byId(ClubEvent.class).load(created.getId());
		List<Person> persons = insertPersons(6);
		for (Person p : persons) {
			ev.add(p);
			transactional(() -> session.merge(ev));
		}
		ev.remove(persons.get(2));
		transactional(() -> session.merge(ev));

		ev.remove(persons.get(4));
		transactional(() -> session.merge(ev));

		ev.getPersons().add(persons.get(2));
		ev.getPersons().add(persons.get(4));
		transactional(() -> session.merge(ev));

		List<ClubeventHasPerson> result = loadEventPersons();
		assertEquals(6, result.size());
	}

}
