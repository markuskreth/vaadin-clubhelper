package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.TestDatabaseHelper.afterCommit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType.Type;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@Tag("spring")
@Disabled
public class ClubEventDataTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	TestDatabaseHelper testDatabaseHelper;

	@Test
	@Transactional
	public void testEventAddon() {

		ClubEvent ev = testDatabaseHelper.creteEvent();
		ev.setType(Type.EINZEL);

		entityManager.persist(ev);
		afterCommit(() -> {
			List<ClubEvent> allClubEvent = testDatabaseHelper.allClubEvent();
			assertEquals(1, allClubEvent.size());
			assertEquals(Type.EINZEL, allClubEvent.get(0).getType());
		});

		ev.setType(Type.DOPPELMINI);
		entityManager.merge(ev);

		afterCommit(() -> {
			List<ClubEvent> allClubEvent = testDatabaseHelper.allClubEvent();
			assertEquals(1, allClubEvent.size());
			assertEquals(Type.DOPPELMINI, allClubEvent.get(0).getType());
		});
	}

	@Test
	@Transactional
	public void testSaveAndSelectEvent() {
		ClubEvent ev = testDatabaseHelper.creteEvent();
		entityManager.persist(ev);
		afterCommit(() -> {
			List<ClubEvent> result = testDatabaseHelper.allClubEvent();
			assertEquals(1, result.size());
		});
	}

	@Test
	@Transactional
	public void testEventWithPerson() {

		ClubEvent ev = testDatabaseHelper.creteEvent();

		Person person = testDatabaseHelper.testInsertPerson();
		Set<Person> persons = ev.getPersons();
		if (persons == null) {
			persons = new HashSet<>();
			ev.setPersons(persons);
		}
		persons.add(person);
		entityManager.persist(ev);

		afterCommit(() -> {
			List<ClubeventHasPerson> loadEventPersons = testDatabaseHelper.loadEventPersons();
			assertFalse(loadEventPersons.isEmpty());
			ClubeventHasPerson link = loadEventPersons.get(0);
			assertEquals(person.getId(), link.getPersonId());
			assertEquals(ev.getId(), link.getClubEventId());

		});
	}

	@Test
	@Transactional
	public void changeEventsPersons() {
		ClubEvent ev = testDatabaseHelper.creteEvent();
		entityManager.persist(ev);

		Person person = testDatabaseHelper.testInsertPerson();

		Person person2 = new Person();
		person2.setPrename(person.getPrename() + "_2");
		person2.setSurname(person.getSurname() + "_2");
		person2.setBirth(LocalDate.now());

		entityManager.persist(person2);

		ClubEvent loaded = entityManager.find(ClubEvent.class, ev.getId());
		assertNotNull(loaded);
		if (loaded.getPersons() == null) {
			loaded.setPersons(new HashSet<>());
		}

		loaded.add(person);
		entityManager.merge(ev);

		loaded.add(person2);
		entityManager.merge(ev);

		afterCommit(() -> {
			List<ClubeventHasPerson> entries = testDatabaseHelper.loadEventPersons();
			assertEquals(2, entries.size());
		});
	}

	@Test
	@Transactional
	public void testChangeEventParticipantsRapidly() {

		final ClubEvent created = testDatabaseHelper.creteEvent();
		entityManager.persist(created);

		afterCommit(() -> {
			final ClubEvent ev = entityManager.find(ClubEvent.class, created.getId());
			List<Person> persons = testDatabaseHelper.insertPersons(6);
			for (Person p : persons) {
				ev.add(p);
				entityManager.merge(ev);
			}
			ev.remove(persons.get(2));
			entityManager.merge(ev);

			ev.remove(persons.get(4));
			entityManager.merge(ev);

			ev.getPersons().add(persons.get(2));
			ev.getPersons().add(persons.get(4));
			entityManager.merge(ev);

		});
		afterCommit(() -> {

			List<ClubeventHasPerson> result = testDatabaseHelper.loadEventPersons();
			assertEquals(6, result.size());
		});
	}

}
