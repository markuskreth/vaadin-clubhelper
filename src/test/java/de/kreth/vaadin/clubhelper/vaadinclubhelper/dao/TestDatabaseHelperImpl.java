package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Component
public class TestDatabaseHelperImpl implements TestDatabaseHelper {

	private static final AtomicInteger eventIdSequence = new AtomicInteger();

	@Autowired
	protected EntityManager entityManager;

	@Override
	public void cleanDatabase() {

		Session session = (Session) entityManager;
		session.doWork(conn -> {

			List<String> tableNames = loadTables(conn);
			try (Statement stm = conn.createStatement()) {

				stm.addBatch("SET FOREIGN_KEY_CHECKS=0");
				for (String table : tableNames) {
					stm.addBatch("TRUNCATE TABLE " + table);
				}
				stm.addBatch("SET FOREIGN_KEY_CHECKS=1");
				stm.executeBatch();
			}
			if (conn.getAutoCommit() == false) {
				conn.commit();
			}
		});
	}

	public List<String> loadTables(Connection conn) throws SQLException {
		List<String> tableNames = new ArrayList<>();

		DatabaseMetaData meta = conn.getMetaData();
		String[] types = { "TABLE" };
		try (ResultSet tables = meta.getTables(null, null, null, types)) {
			while (tables.next()) {
				tableNames.add(tables.getString("TABLE_NAME"));
			}
		}
		return tableNames;
	}

	/**
	 * executes the given runnable within a jpa Transaction.
	 * 
	 * @param r
	 */
	@Override
	public void transactional(Runnable r) {
		transactional(session -> r.run());
	}

	@Override
	public void transactional(Consumer<Session> r) {

		Session session = (Session) entityManager;
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

	@Override
	public Person testInsertPerson() {
		Person p = new Person();
		p.setPrename("prename");
		p.setSurname("surname");
		p.setBirth(LocalDate.now());

		transactional(() -> entityManager.persist(p));
		return p;
	}

	@Override
	public List<Person> insertPersons(int count) {

		final List<Person> inserted = createPersons(count);

		transactional(() -> {
			for (Person p : inserted) {
				entityManager.persist(p);
			}
		});
		for (Person p : inserted) {
			assertTrue(p.getId() > 0, "not saved: " + p);
		}
		return inserted;
	}

	@Override
	public List<Person> createPersons(int count) {

		final List<Person> inserted = new ArrayList<>();

		for (int i = 0; i < count; i++) {

			Person p = new Person();
			p.setPrename("prename_" + i);
			p.setSurname("surname_" + i);
			p.setBirth(LocalDate.now());
			inserted.add(p);
		}
		return inserted;
	}

	@Override
	public ClubEvent creteEvent() {
		ClubEvent ev = ClubEventBuilder.builder().withId("id_" + eventIdSequence.getAndIncrement()).withAllDay(true)
				.withCaption("caption").withDescription("description")
				.withStart(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0, ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0, ZoneId.systemDefault())).withiCalUID("iCalUID")
				.withOrganizerDisplayName("organizerDisplayName").build();
		return ev;
	}

	@Override
	public List<ClubeventHasPerson> loadEventPersons() {

		Session session = (Session) entityManager;
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

	@Override
	public List<ClubEvent> allClubEvent() {
		return entityManager.createNamedQuery("ClubEvent.findAll", ClubEvent.class).getResultList();
	}

}
