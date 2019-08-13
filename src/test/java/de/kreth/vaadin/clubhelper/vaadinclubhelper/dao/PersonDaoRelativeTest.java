package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestPersonGenerator;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@Tag("spring")
@Disabled
public class PersonDaoRelativeTest {

	@Autowired
	private PersonDao personDao;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SessionFactory sessionFactory;

	private List<Person> persons;

	@BeforeEach
	@Transactional
	@Rollback(false)
	public void setUp() throws Exception {

		persons = TestPersonGenerator.generatePersonen(2);
		for (Person p : persons) {
			p.setId(0);
		}

	}

	@Test
	@Transactional
	void testLoadPersonById() {
		for (Person p : persons) {
			personDao.save(p);
		}
		TestDatabaseHelper.afterCommit(() -> {
			Person person1 = entityManager.find(Person.class, 1);
			assertNotNull(person1);
		});
	}

	@Test
	@Transactional
	void testStorePersonChildRelationship() {
		for (Person p : persons) {
			personDao.save(p);
		}
		TestDatabaseHelper.afterCommit(() -> {
			Session session = sessionFactory.openSession();
			session.doWork(conn -> {
				try (Statement stm = conn.createStatement()) {
					ResultSet rs = stm.executeQuery(
							"SELECT id, person1, person2, TO_PERSON1_RELATION, TO_PERSON2_RELATION, changed, created, deleted FROM relative where person1=1 OR person2=1 and deleted is null");
					while (rs.next()) {
						System.out.println(
								"id=" + rs.getInt("id") + ", person1=" + rs.getInt("person1") + ", person2="
										+ rs.getInt("person2") + ", TO_PERSON1_RELATION="
										+ rs.getString("TO_PERSON1_RELATION"));
					}
				}
			});
		});
	}

}
