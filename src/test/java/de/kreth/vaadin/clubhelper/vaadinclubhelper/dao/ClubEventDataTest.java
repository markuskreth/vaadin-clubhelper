package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType.Type;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
public class ClubEventDataTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	TestDatabaseHelper testDatabaseHelper;

	@AfterEach
	public void cleanDatabase() {
		testDatabaseHelper.cleanDatabase();
	}

	@Test
	public void testEventAddon() {

		ClubEvent ev = testDatabaseHelper.creteEvent();
		CompetitionType competitionType = new CompetitionType();
		competitionType.setType(Type.EINZEL);
		ev.setCompetitionType(competitionType);

		testDatabaseHelper.transactional(() -> entityManager.persist(ev));
		List<ClubEvent> allClubEvent = testDatabaseHelper.allClubEvent();
		assertEquals(1, allClubEvent.size());
		assertEquals(Type.EINZEL, allClubEvent.get(0).getCompetitionType());

		competitionType.setType(Type.DOPPELMINI);
		testDatabaseHelper.transactional(() -> entityManager.merge(ev));
		allClubEvent = testDatabaseHelper.allClubEvent();
		assertEquals(1, allClubEvent.size());
		assertEquals(Type.DOPPELMINI, allClubEvent.get(0).getCompetitionType());
	}

	@Test
	public void testSaveAndSelectEvent() {
		ClubEvent ev = testDatabaseHelper.creteEvent();

		testDatabaseHelper.transactional(() -> entityManager.persist(ev));
		List<ClubEvent> result = testDatabaseHelper.allClubEvent();
		assertEquals(1, result.size());
	}

	@Test
	public void testEventWithPerson() {

		ClubEvent ev = testDatabaseHelper.creteEvent();

		Person person = testDatabaseHelper.testInsertPerson();
		Set<Person> persons = ev.getPersons();
		if (persons == null) {
			persons = new HashSet<>();
			ev.setPersons(persons);
		}
		persons.add(person);

		testDatabaseHelper.transactional(() -> entityManager.persist(ev));

		List<ClubeventHasPerson> loadEventPersons = testDatabaseHelper.loadEventPersons();
		assertFalse(loadEventPersons.isEmpty());
		ClubeventHasPerson link = loadEventPersons.get(0);
		assertEquals(person.getId(), link.getPersonId());
		assertEquals(ev.getId(), link.getClubEventId());

	}

	@Test
	public void changeEventsPersons() {
		ClubEvent ev = testDatabaseHelper.creteEvent();

		testDatabaseHelper.transactional(() -> entityManager.persist(ev));

		Person person = testDatabaseHelper.testInsertPerson();

		Person person2 = new Person();
		person2.setPrename(person.getPrename() + "_2");
		person2.setSurname(person.getSurname() + "_2");
		person2.setBirth(LocalDate.now());

		testDatabaseHelper.transactional(() -> entityManager.persist(person2));

		ClubEvent loaded = entityManager.find(ClubEvent.class, ev.getId());
		assertNotNull(loaded);
		if (loaded.getPersons() == null) {
			loaded.setPersons(new HashSet<>());
		}

		loaded.add(person);
		testDatabaseHelper.transactional(() -> entityManager.merge(ev));

		loaded.add(person2);
		testDatabaseHelper.transactional(() -> entityManager.merge(ev));

		List<ClubeventHasPerson> entries = testDatabaseHelper.loadEventPersons();
		assertEquals(2, entries.size());
	}

	@Test
	public void testChangeEventParticipantsRapidly() {

		final ClubEvent created = testDatabaseHelper.creteEvent();
		testDatabaseHelper.transactional(() -> {
			entityManager.persist(created);
		});

		final ClubEvent ev = entityManager.find(ClubEvent.class, created.getId());
		List<Person> persons = testDatabaseHelper.insertPersons(6);
		for (Person p : persons) {
			ev.add(p);
			testDatabaseHelper.transactional(() -> entityManager.merge(ev));
		}
		ev.remove(persons.get(2));
		testDatabaseHelper.transactional(() -> entityManager.merge(ev));

		ev.remove(persons.get(4));
		testDatabaseHelper.transactional(() -> entityManager.merge(ev));

		ev.getPersons().add(persons.get(2));
		ev.getPersons().add(persons.get(4));
		testDatabaseHelper.transactional(() -> entityManager.merge(ev));

		List<ClubeventHasPerson> result = testDatabaseHelper.loadEventPersons();
		assertEquals(6, result.size());
	}

}
