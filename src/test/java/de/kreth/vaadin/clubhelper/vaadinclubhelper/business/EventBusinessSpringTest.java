package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ClubhelperException;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.TestDatabaseHelper;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Tag("spring")
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

	@BeforeEach
	void setUp() throws Exception {
		testDatabaseHelper.cleanDatabase();
		insertTestData();
		eventBusiness.setSelected(event);

		all = entityManager.createQuery("from ClubeventHasPerson", ClubeventHasPerson.class);
	}

	@AfterEach
	void shutdown() {
		entityManager.clear();
		testDatabaseHelper.cleanDatabase();
	}

	private void insertTestData() {
		persons = new ArrayList<>();
		testDatabaseHelper.transactional(() -> {
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
		});
	}

	@Test
	void testDataCorrectlyCreated() {

		assertEquals(0, all.getResultList().size());

		List<Person> stored = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
		assertEquals(3, stored.size());

		List<ClubEvent> events = eventBusiness.loadEvents();
		assertEquals(1, events.size());

	}

	@Test
	@Disabled
	void testAddPersonsToEvent() {
		assertEquals(0, all.getResultList().size());
		try {
			transactional(() -> eventBusiness.changePersons(new HashSet<>(persons.subList(0, 1))));
			transactional(() -> eventBusiness.changePersons(new HashSet<>(persons.subList(0, 2))));
		}
		catch (Exception e) {

		}

		List<ClubeventHasPerson> result = all.getResultList();
		assertEquals(2, result.size());
	}

	private void transactional(ThrowingRunnable<ClubhelperException> object) {
		testDatabaseHelper.transactional(() -> {
			try {
				object.run();
			}
			catch (ClubhelperException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
