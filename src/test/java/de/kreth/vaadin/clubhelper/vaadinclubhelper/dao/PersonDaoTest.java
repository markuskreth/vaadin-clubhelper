package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonDaoTest extends AbstractDatabaseTest {

	private PersonDaoImpl personDao;
	private Person person;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		personDao = new PersonDaoImpl();
		personDao.em = session;
		person = new Person();
		person.setPrename("prename");
		person.setSurname("surname");
//		Date time = new GregorianCalendar(2018, Calendar.NOVEMBER, 8).getTime();
		person.setBirth(LocalDate.of(2018, 11, 8));
		person.setPassword("password");
	}

	@Test
	public void testSave() {
		storePerson();
		List<Person> stored = session.createNamedQuery(Person.QUERY_FINDALL, Person.class).list();
		assertEquals(1, stored.size());
		assertEquals(person, stored.get(0));
		assertEquals(person, personDao.get(person.getId()));

	}

	public void storePerson() {
		Transaction tx = session.beginTransaction();
		personDao.save(person);
		tx.commit();
	}

	@Test
	public void testUpdate() {
		storePerson();
		person.setSurname("surname2");
		person.setPrename("prename2");

		Transaction tx = session.beginTransaction();
		personDao.update(person);
		tx.commit();
		List<Person> stored = session.createNamedQuery(Person.QUERY_FINDALL, Person.class).list();
		assertEquals(1, stored.size());
		assertEquals(person, stored.get(0));
	}

	@Test
	public void testListAll() {
		storePerson();
		session.detach(person);
		person = new Person();
		person.setSurname("surname2");
		person.setPrename("prename2");
		person.setBirth(LocalDate.of(2018, 11, 8));
		person.setPassword("password");
		storePerson();
		List<Person> stored = personDao.listAll();
		assertEquals(2, stored.size());

	}

	@Test
	public void testPersonGroup() {
		assertNotNull(new GroupDaoImpl());
	}
}
