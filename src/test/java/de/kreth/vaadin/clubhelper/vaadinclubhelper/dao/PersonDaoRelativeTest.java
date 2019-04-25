package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;

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
	private TestDatabaseHelper testDatabaseHelper;

	@Autowired
	private SessionFactory sessionFactory;

	private Person person1;

	private Person person2;

	@BeforeEach
	public void setUp() throws Exception {
//		testDatabaseHelper.cleanDatabase();
//		List<Person> persons = TestPersonGenerator.generatePersonen(2);
//		person1 = persons.get(0);
//		person2 = persons.get(1);

		List<Person> resultList = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
		assertFalse(resultList.isEmpty());
	}

	@AfterEach
	public void clearDatabase() throws IOException {
//		testDatabaseHelper.cleanDatabase();
	}

	@Test
	void testLoadPersonById() {
		person1 = entityManager.find(Person.class, Integer.valueOf(1));
		assertNotNull(person1);
		assertEquals(1, person1.getId());
	}

	@Test
	void testStorePersonChildRelationship() {
		Session session = sessionFactory.openSession();
		session.doWork(conn -> {
			try (Statement stm = conn.createStatement()) {
				ResultSet rs = stm.executeQuery(
						"SELECT id, person1, person2, TO_PERSON1_RELATION, TO_PERSON2_RELATION, changed, created, deleted FROM relative where person1=1 OR person2=1 and deleted is null");
				while (rs.next()) {
					System.out.println("id=" + rs.getInt("id") + ", person1=" + rs.getInt("person1") + ", person2="
							+ rs.getInt("person2") + ", TO_PERSON1_RELATION=" + rs.getString("TO_PERSON1_RELATION"));
				}
			}
		});
	}
}
