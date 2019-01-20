package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
public class PersonDaoTest {

	@Autowired
	private PersonDao personDao;
	@Autowired
	private EntityManager entityManager;

	private Person person;
	private TypedQuery<Person> q;

	private final String truncateString = createTruncateString();

	@BeforeEach
	public void setUp() throws Exception {
		person = new Person();
		person.setPrename("prename");
		person.setSurname("surname");
		person.setBirth(LocalDate.of(2018, 11, 8));
		person.setPassword("password");
		q = entityManager.createNamedQuery(Person.QUERY_FINDALL, Person.class);
		assertTrue(q.getResultList().isEmpty());
	}

	@AfterEach
	public void clearDatabase() throws IOException {
		Query truncateQuery = entityManager.createNativeQuery(truncateString);
		transactional(() -> truncateQuery.executeUpdate());
	}

	private String createTruncateString() {
		try {
			InputStream resource = getClass().getResourceAsStream("/truncateTables.sql");
			return IOUtils.toString(resource, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testSave() {
		transactional(() -> personDao.save(person));
		List<Person> stored = q.getResultList();
		assertEquals(1, stored.size());
		assertEquals(person, stored.get(0));
		assertEquals(person, personDao.get(person.getId()));

	}

	@Test
	public void testUpdate() {

		transactional(() -> personDao.save(person));
		person.setSurname("surname2");
		person.setPrename("prename2");

		transactional(() -> personDao.update(person));

		List<Person> stored = q.getResultList();
		assertEquals(1, stored.size());
		assertEquals(person, stored.get(0));
	}

	public void transactional(Runnable r) {
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		r.run();
		tx.commit();
	}

	@Disabled
	@Test
	public void testListAll() {
		transactional(() -> personDao.save(person));
		entityManager.detach(person);

		person = new Person();
		person.setId(0);
		person.setSurname("surname2");
		person.setPrename("prename2");
		person.setBirth(LocalDate.of(2018, 11, 8));
		person.setPassword("password");
		transactional(() -> personDao.save(person));
		List<Person> stored = personDao.listAll();
		assertEquals(2, stored.size());

	}

	@Test
	public void testPersonGroup() {
		assertNotNull(new GroupDaoImpl());
	}
}
