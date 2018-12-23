package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.IdentifierLoadAccess;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class ClubEventDataTest extends AbstractDatabaseTest {

	private static final AtomicInteger counter = new AtomicInteger();

	public Person testInsertPerson() {
		Person p = new Person();
		p.setPrename("prename");
		p.setSurname("surname");
		p.setBirth(LocalDate.now());

		transactional(() -> session.save(p));
		return p;
	}

	public List<Person> insertPersons(int count) {

		final List<Person> inserted = new ArrayList<>();

		transactional(() -> {
			for (int i = 0; i < count; i++) {

				Person p = new Person();
				p.setPrename("prename_" + i);
				p.setSurname("surname_" + i);
				p.setBirth(LocalDate.now());
				session.save(p);
				inserted.add(p);
			}
		});
		for (Person p : inserted) {
			assertTrue(p.getId() > 0, "not saved: " + p);
		}
		return inserted;
	}

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

	private List<ClubeventHasPerson> loadEventPersons() {

		return session.doReturningWork(conn -> {

			List<ClubeventHasPerson> link = new ArrayList<>();

			try (Statement stm = conn.createStatement()) {
				ResultSet rs = stm.executeQuery("select * from clubevent_has_person");
				while (rs.next()) {
					ClubeventHasPerson ep = new ClubeventHasPerson();

					ep.setClubEventId(rs.getString("clubevent_id"));
					ep.setPersonId(rs.getInt("person_id"));
					link.add(ep);
				}
			}

			return link;
		});

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

		loaded.getPersons().add(person);
		transactional(() -> session.update(ev));

		loaded.getPersons().add(person2);
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

	private ClubEvent creteEvent() {
		ClubEvent ev = ClubEventBuilder.builder().withId("id_" + counter.getAndIncrement()).withAllDay(true)
				.withCaption("caption").withDescription("description")
				.withStart(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0, ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0, ZoneId.systemDefault())).withiCalUID("iCalUID")
				.withOrganizerDisplayName("organizerDisplayName").build();
		return ev;
	}
}
