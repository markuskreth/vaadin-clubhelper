package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import static de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.TestDatabaseHelper.afterCommit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ClubhelperException;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.TestDatabaseHelper;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Tag("spring")
@Disabled
class EventBusinessSpringTest {

	private List<Person> persons;

	private ClubEvent event;

	@Autowired
	private EventBusiness eventBusiness;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private TestDatabaseHelper testDatabaseHelper;

	private TypedQuery<ClubeventHasPerson> all;

	void setUp() throws Exception {
		insertTestData();
		eventBusiness.setSelected(event);

		all = entityManager.createQuery("from ClubeventHasPerson", ClubeventHasPerson.class);
	}

	private void insertTestData() {
		persons = new ArrayList<>();
		for (int i = 0; i < 3; i++) {

			Person p = new Person();
			p.setPrename("prename_" + i);
			p.setSurname("surname_" + i);
			p.setBirth(LocalDate.now());
			entityManager.persist(p);
			persons.add(p);
		}
		event = testDatabaseHelper.creteEvent();
		assertNull(event.getPersons());
		entityManager.persist(event);
	}

	@Test
	@Transactional
	void testDataCorrectlyCreated() throws Exception {

		setUp();

		afterCommit(() -> {

			assertEquals(0, all.getResultList().size());

			List<Person> stored = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
			assertEquals(3, stored.size());

			List<ClubEvent> events = eventBusiness.loadEvents();
			assertEquals(1, events.size());
		});

	}

	@Test
	@Transactional
	void testAddPersonsToEvent() throws Exception {
		setUp();

		afterCommit(() -> {
			assertEquals(0, all.getResultList().size());
		});

		afterCommit(() -> {
			try {
				eventBusiness.changePersons(new HashSet<>(persons.subList(0, 1)));
			}
			catch (ClubhelperException e) {
				throw new RuntimeException(e);
			}
		});

		afterCommit(() -> {
			List<ClubeventHasPerson> result = all.getResultList();
			assertEquals(1, result.size());
		});

		afterCommit(() -> {
			try {
				eventBusiness.changePersons(new HashSet<>(persons.subList(0, 2)));
			}
			catch (ClubhelperException e) {
				throw new RuntimeException(e);
			}
		});

		afterCommit(() -> {
			List<ClubeventHasPerson> result = all.getResultList();
			assertEquals(2, result.size());
		});

	}

}
