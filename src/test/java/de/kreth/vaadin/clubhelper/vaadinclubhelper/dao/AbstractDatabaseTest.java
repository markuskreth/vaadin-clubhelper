package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import de.kreth.vaadin.clubhelper.HibernateHolder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public abstract class AbstractDatabaseTest {

	private static final AtomicInteger eventIdSequence = new AtomicInteger();

	protected static SessionFactory sessionFactory;
	protected Session session;

	@BeforeAll
	public static void setUpDatabase() throws Exception {

		// setup the session factory
		Configuration configuration = HibernateHolder.configuration();

		sessionFactory = configuration.buildSessionFactory();

	}

	public enum DB_TYPE {
		MYSQL("SET @@foreign_key_checks = 0", "SET @@foreign_key_checks = 1"),
		H2("SET REFERENTIAL_INTEGRITY FALSE", "SET REFERENTIAL_INTEGRITY TRUE");

		private DB_TYPE(String disableReferentialIntegrety, String enableReferentialIntegrety) {
			this.disableReferentialIntegrety = disableReferentialIntegrety;
			this.enableReferentialIntegrety = enableReferentialIntegrety;
		}

		final String disableReferentialIntegrety;
		final String enableReferentialIntegrety;
	}

	@AfterEach
	public void cleanH2Database() {
		if (session.getTransaction().isActive()) {
			session.flush();
		}

		session.doWork(conn -> {
			AbstractDatabaseTest.cleanDatabase(conn, DB_TYPE.H2);
		});

	}

	@BeforeEach
	void createSession() {
		if (session != null) {
			session.close();
		}
		session = sessionFactory.openSession();
	}

	public static void cleanDatabase(Connection connection, DB_TYPE type) throws SQLException {

		String TABLE_NAME = "TABLE_NAME";
		String[] TABLE_TYPES = { "TABLE" };
		Set<String> tableNames = new HashSet<>();

		try (ResultSet tables = connection.getMetaData().getTables(null, null, null, TABLE_TYPES)) {
			while (tables.next()) {
				tableNames.add(tables.getString(TABLE_NAME));
			}
		}

		try (Statement stm = connection.createStatement()) {
			stm.execute(type.disableReferentialIntegrety);
			try {
				for (String tableName : tableNames) {
					try {
						stm.executeUpdate("TRUNCATE TABLE " + tableName);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			} finally {
				stm.execute(type.enableReferentialIntegrety);
			}
		}

		if (connection.getAutoCommit() == false) {
			connection.commit();
		}
	}

	/**
	 * executes the given runnable within a jpa Transaction.
	 * 
	 * @param r
	 */
	protected void transactional(Runnable r) {
		transactional(session -> r.run());
	}

	protected void transactional(Consumer<Session> r) {

		EntityTransaction tx = session.getTransaction();
		tx.begin();
		try {
			r.accept(session);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}

	public Person testInsertPerson() {
		Person p = new Person();
		p.setPrename("prename");
		p.setSurname("surname");
		p.setBirth(LocalDate.now());

		transactional(() -> session.save(p));
		return p;
	}

	public List<Person> insertPersons(int count) {

		final List<Person> inserted = createPersons(count);

		transactional(() -> {
			for (Person p : inserted) {
				session.save(p);
			}
		});
		for (Person p : inserted) {
			assertTrue(p.getId() > 0, "not saved: " + p);
		}
		return inserted;
	}

	public static List<Person> createPersons(int count) {

		final List<Person> inserted = new ArrayList<>();

		for (int i = 0; i < count; i++) {

			Person p = new Person();
			p.setId(i + 1);
			p.setPrename("prename_" + i);
			p.setSurname("surname_" + i);
			p.setBirth(LocalDate.now());
			inserted.add(p);
		}
		return inserted;
	}

	public static ClubEvent creteEvent() {
		ClubEvent ev = ClubEventBuilder.builder().withId("id_" + eventIdSequence.getAndIncrement()).withAllDay(true)
				.withCaption("caption").withDescription("description")
				.withStart(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0, ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0, ZoneId.systemDefault())).withiCalUID("iCalUID")
				.withOrganizerDisplayName("organizerDisplayName").build();
		return ev;
	}

	public List<ClubeventHasPerson> loadEventPersons() {

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

}
