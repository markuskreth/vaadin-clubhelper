package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.kreth.vaadin.clubhelper.HibernateHolder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AbstractDatabaseTest;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AbstractDatabaseTest.DB_TYPE;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDaoImpl;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@ExtendWith(SpringExtension.class)
class EventBusinessSpringTest {

	EventBusiness eventBusiness;

	private List<Person> persons;
	private ClubEvent event;

	@Autowired
	private EventBusiness business;

	@Autowired
	private EntityManager entityManager;

	private TypedQuery<ClubeventHasPerson> all;
//
//	@Autowired
//	private InnerConfig innerConfig;

	@Configuration
	public static class InnerConfig {

		private SessionFactory sessionFactory;

		public InnerConfig() {

			org.hibernate.cfg.Configuration configuration = HibernateHolder.configuration();

			sessionFactory = configuration.buildSessionFactory();
		}

		@Bean
		public EntityManager getEntityManager() {
			return sessionFactory.openSession();
		}

		@Bean
		public ClubEventDao getClubEventDao() {
			return new ClubEventDaoImpl();
		}

		@Bean
		public EventBusiness getEventBusiness() {
			return new EventBusiness();
		}

	}

	@BeforeEach
	void setUp() throws Exception {
		insertTestData();
		business.setSelected(event);

		all = entityManager.createQuery("from ClubeventHasPerson", ClubeventHasPerson.class);
	}

	@AfterEach
	void shutdown() {
		entityManager.clear();
		((Session) entityManager).doWork(conn -> AbstractDatabaseTest.cleanDatabase(conn, DB_TYPE.H2));
//		entityManager.flush();
	}

	private void insertTestData() {
		persons = new ArrayList<>();

		entityManager.getTransaction().begin();
		for (int i = 0; i < 3; i++) {

			Person p = new Person();
			p.setPrename("prename_" + i);
			p.setSurname("surname_" + i);
			p.setBirth(LocalDate.now());
			entityManager.persist(p);
			persons.add(p);
		}
		event = AbstractDatabaseTest.creteEvent();
		assertNull(event.getPersons());
		entityManager.persist(event);
		entityManager.getTransaction().commit();
	}

	@Test
	void testDataCorrectlyCreated() {

		assertEquals(0, all.getResultList().size());

		List<Person> stored = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
		assertEquals(3, stored.size());

		List<ClubEvent> events = business.loadEvents();
		assertEquals(1, events.size());
//		assertNotNull(events.get(0).getPersons());

	}

	@Test
	@Disabled
	void testAddPersonsToEvent() {
		assertEquals(0, all.getResultList().size());

		entityManager.getTransaction().begin();
		business.changePersons(new HashSet<>(persons.subList(0, 1)));
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		business.changePersons(new HashSet<>(persons.subList(0, 2)));
		entityManager.getTransaction().commit();

		List<ClubeventHasPerson> result = all.getResultList();
		assertEquals(2, result.size());
	}

	class DatabaseHelper extends AbstractDatabaseTest {
		public DatabaseHelper(EntityManager em) {
			this((Session) em);
		}

		public DatabaseHelper(Session session) {
			this.session = session;
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
