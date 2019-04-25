package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Gender;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@Tag("spring")
public class PersonDaoTest {

	@Autowired
	private PersonDao personDao;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private TestDatabaseHelper testDatabaseHelper;

	private Person person;

	@BeforeEach
	public void setUp() throws Exception {
		testDatabaseHelper.cleanDatabase();
		person = new Person();
		person.setPrename("prename");
		person.setSurname("surname");
		person.setBirth(LocalDate.of(2018, 11, 8));
		person.setPassword("password");
		person.setGender(Gender.MALE);
		assertTrue(entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList().isEmpty());
	}

	@AfterEach
	public void clearDatabase() throws IOException {
		testDatabaseHelper.cleanDatabase();
	}

	@Test
	public void testSave() {
		testDatabaseHelper.transactional(() -> personDao.save(person));

		List<Person> stored = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
		assertEquals(1, stored.size());
		assertEquals(person, stored.get(0));
		assertEquals(person, personDao.get(person.getId()));

	}

	@Test
	public void testUpdate() {

		testDatabaseHelper.transactional(() -> personDao.save(person));
		person.setSurname("surname2");
		person.setPrename("prename2");

		testDatabaseHelper.transactional(() -> personDao.save(person));

		List<Person> stored = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
		assertEquals(1, stored.size());
		assertEquals(person, stored.get(0));
	}

	@Test
	public void testListAll() {
		testDatabaseHelper.transactional(() -> personDao.save(person));

		person = new Person();
		person.setSurname("surname2");
		person.setPrename("prename2");
		person.setBirth(LocalDate.of(2018, 11, 8));
		person.setPassword("password");

		testDatabaseHelper.transactional(() -> personDao.save(person));
		List<Person> stored = personDao.listAll();
		assertEquals(2, stored.size());

	}

	@Test
	public void testPersonGroup() {
		assertNotNull(new GroupDaoImpl());
	}
}
