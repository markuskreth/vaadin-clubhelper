package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.junit.Test;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class ClubEventDataTest extends AbstractDatabaseTest {

	public Person testInsertPerson() {
		Person p = new Person();
		p.setPrename("prename");
		p.setSurname("surname");
		p.setBirth(LocalDate.now());
		Transaction tx = session.beginTransaction();
		session.save(p);
		tx.commit();
		return p;
	}

	public List<Person> insertPersons(int count) {

		List<Person> inserted = new ArrayList<>();

		Transaction tx = session.beginTransaction();
		for (int i = 0; i < count; i++) {

			Person p = new Person();
			p.setPrename("prename_" + i);
			p.setSurname("surname_" + i);
			p.setBirth(LocalDate.now());
			session.save(p);
			inserted.add(p);
		}
		tx.commit();
		for (Person p : inserted) {
			assertTrue("not saved: " + p, p.getId() > 0);
		}
		return inserted;
	}

	@Test
	public void testInsertEvent() {
		ClubEvent ev = creteEvent();

		Transaction tx = session.beginTransaction();
		session.save(ev);
		tx.commit();
	}

	@Test
	public void testSelectEvents() {
		ClubEvent ev = creteEvent();

		Transaction tx = session.beginTransaction();
		session.save(ev);
		tx.commit();

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

		Transaction tx = session.beginTransaction();
		session.save(ev);
		tx.commit();

		List<ClubeventHasPerson> loadEventPersons = loadEventPersons();
		assertFalse(loadEventPersons.isEmpty());
		ClubeventHasPerson link = loadEventPersons.get(0);
		assertEquals(person.getId(), link.getPersonId());
		assertEquals(ev.getId(), link.getClubEventId());

	}

	private List<ClubeventHasPerson> loadEventPersons() {
		final AtomicBoolean finishedWork = new AtomicBoolean(false);
		List<ClubeventHasPerson> link = new ArrayList<>();

		session.doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				Statement stm = connection.createStatement();
				ResultSet rs = stm.executeQuery("select * from clubevent_has_person");
				while (rs.next()) {
					ClubeventHasPerson ep = new ClubeventHasPerson();

					ep.setClubEventId(rs.getString("clubevent_id"));
					ep.setPersonId(rs.getInt("person_id"));
					link.add(ep);
				}

				finishedWork.set(true);
			}
		});
		assertTrue(finishedWork.get());
		return link;
	}

	@Test
	public void changeEventsPersons() {
		ClubEvent ev = creteEvent();

		Transaction tx = session.beginTransaction();
		session.save(ev);
		tx.commit();

		Person person = testInsertPerson();

		Person person2 = new Person();
		person2.setId(person.getId() + 1);
		person2.setPrename(person.getPrename() + "_2");
		person2.setSurname(person.getSurname() + "_2");
		person2.setBirth(LocalDate.now());

		tx = session.beginTransaction();
		session.save(person2);
		tx.commit();

		IdentifierLoadAccess<ClubEvent> idLoadAccess = session.byId(ClubEvent.class);
		ClubEvent loaded = idLoadAccess.load(ev.getId());
		assertNotNull(loaded);
		if (loaded.getPersons() == null) {
			loaded.setPersons(new HashSet<>());
		}

		loaded.getPersons().add(person);
		tx = session.beginTransaction();
		session.update(ev);
		tx.commit();

		loaded.getPersons().add(person2);
		tx = session.beginTransaction();
		session.update(ev);
		tx.commit();
		List<ClubeventHasPerson> entries = loadEventPersons();
		assertEquals(2, entries.size());
	}

	@Test
	public void testChangeEventParticipantsRapidly() {

		ClubEvent ev = creteEvent();
		ev.setPersons(new HashSet<>());
		Transaction tx = session.beginTransaction();
		session.save(ev);
		tx.commit();

		ev = session.byId(ClubEvent.class).load(ev.getId());
		List<Person> persons = insertPersons(6);
		for (Person p : persons) {
			ev.getPersons().add(p);
			tx = session.beginTransaction();
			session.merge(ev);
			tx.commit();
		}
		ev.getPersons().remove(persons.get(2));
		tx = session.beginTransaction();
		session.merge(ev);
		tx.commit();
		ev.getPersons().remove(persons.get(4));
		tx = session.beginTransaction();
		session.merge(ev);
		tx.commit();

		ev.getPersons().add(persons.get(2));
		ev.getPersons().add(persons.get(4));
		tx = session.beginTransaction();
		session.merge(ev);
		tx.commit();
		List<ClubeventHasPerson> result = loadEventPersons();
		assertEquals(6, result.size());
	}

	private ClubEvent creteEvent() {
		ClubEvent ev = ClubEventBuilder.builder().withId("id").withAllDay(true).withCaption("caption")
				.withDescription("description")
				.withStart(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0, ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0, ZoneId.systemDefault())).withiCalUID("iCalUID")
				.withOrganizerDisplayName("organizerDisplayName").build();
		return ev;
	}
}
