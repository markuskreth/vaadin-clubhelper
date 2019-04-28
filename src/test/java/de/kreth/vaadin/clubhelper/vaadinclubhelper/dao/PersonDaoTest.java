package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.TestDatabaseHelper.afterCommit;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

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

	private Person person;

	@BeforeEach
	public void setUp() throws Exception {
		person = new Person();
		person.setPrename("prename");
		person.setSurname("surname");
		person.setBirth(LocalDate.of(2018, 11, 8));
		person.setPassword("password");
		person.setGender(Gender.MALE);
		assertTrue(entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList().isEmpty());
	}

	@Test
	@Transactional
	public void testSave() {
		personDao.save(person);
		afterCommit(() -> {
			List<Person> stored = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
			assertEquals(1, stored.size());
			assertEquals(person, stored.get(0));
			assertEquals(person, personDao.get(person.getId()));
		});

	}

	@Test
	@Transactional
	public void testUpdate() {

		personDao.save(person);
		afterCommit(() -> {

			person.setSurname("surname2");
			person.setPrename("prename2");

			personDao.save(person);

		});
		afterCommit(() -> {
			List<Person> stored = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class).getResultList();
			assertEquals(1, stored.size());
			assertEquals(person, stored.get(0));
		});
	}

	@Test
	@Transactional
	public void testListAll() {

		personDao.save(person);

		person = new Person();
		person.setSurname("surname2");
		person.setPrename("prename2");
		person.setBirth(LocalDate.of(2018, 11, 8));
		person.setPassword("password");
		personDao.save(person);

		afterCommit(() -> {

			List<Person> stored = personDao.listAll();
			assertEquals(2, stored.size());
		});

	}

}
