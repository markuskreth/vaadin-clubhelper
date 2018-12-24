package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.kreth.clubhelperbackend.google.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.HibernateHolder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AbstractDatabaseTest;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDaoImpl;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@ExtendWith(SpringExtension.class)
class EventBusinessTest {

	EventBusiness eventBusiness;

	private List<Person> persons;
	private ClubEvent event;
	private DatabaseHelper helper;

	@Autowired
	protected EntityManager entityManager;

	private EventBusiness business;

	@Configuration
	public static class MyConfig {

		@Bean
		public EntityManager getEntityManager() throws Exception {

			// setup the session factory
			org.hibernate.cfg.Configuration configuration = HibernateHolder.configuration();

			SessionFactory sessionFactory = configuration.buildSessionFactory();
			return sessionFactory.openSession();

		}

		@Bean
		public CalendarAdapter getCalendarAdapter() throws GeneralSecurityException, IOException {
			return new CalendarAdapter();
		}

	}

	@BeforeEach
	void setUp() throws Exception {
		helper = new DatabaseHelper(entityManager);
		helper.cleanH2Database();

		ClubEventDaoImpl dao = new ClubEventDaoImpl();
		dao.setEntityManager(entityManager);

		business = new EventBusiness();
		business.dao = dao;
		insertTestData();
		business.setSelected(event);
	}

	private void insertTestData() {
		persons = helper.insertPersons(3);
		event = helper.creteEvent();
		helper.transactional((session) -> session.save(event));
	}

	@Test
	void testDataCorrectlyCreated() {
		assertEquals(0, helper.loadEventPersons().size());

		List<Person> stored = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
		assertEquals(3, stored.size());

		List<ClubEvent> events = business.loadEvents();
		assertEquals(1, events.size());
//		assertNotNull(events.get(0).getPersons());

	}

	@Test
	void testAddPersonsToEvent() {
		helper.transactional(() -> business.changePersons(new HashSet<>(persons.subList(0, 1))));
		helper.transactional(() -> business.changePersons(new HashSet<>(persons.subList(0, 2))));

		assertEquals(2, helper.loadEventPersons().size());
	}

	class DatabaseHelper extends AbstractDatabaseTest {
		public DatabaseHelper(EntityManager em) {
			this((Session) em);
		}

		public DatabaseHelper(Session session) {
			AbstractDatabaseTest.session = session;
		}

		@Override
		public void transactional(Runnable r) {
			super.transactional(r);
		}

		@Override
		public void transactional(Consumer<Session> r) {
			super.transactional(r);
		}
	}
}
