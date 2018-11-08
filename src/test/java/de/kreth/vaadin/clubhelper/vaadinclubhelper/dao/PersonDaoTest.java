package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ibm.icu.util.Calendar;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

class PersonDaoTest extends AbstractDatabaseTest {

	private PersonDaoImpl personDao;
	private Person person;

	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
		personDao = new PersonDaoImpl();
		personDao.em = session;
		person = new Person();
		person.setPrename("prename");
		person.setSurname("surname");
		person.setBirth(new GregorianCalendar(2018, Calendar.NOVEMBER, 8).getTime());
		person.setPassword("password");
	}

	@Test
	void testSave() {
		storePerson();
		List<Person> stored = session.createNamedQuery(Person.QUERY_FINDALL, Person.class).list();
		assertEquals(1, stored.size());
		assertEquals(person, stored.get(0));
	}

	public void storePerson() {
		Transaction tx = session.beginTransaction();
		personDao.save(person);
		tx.commit();
	}

	@Test
	void testUpdate() {
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
	void testListAll() {
		storePerson();
		session.detach(person);
		person = new Person();
		person.setSurname("surname2");
		person.setPrename("prename2");
		person.setBirth(new GregorianCalendar(2018, Calendar.NOVEMBER, 8).getTime());
		person.setPassword("password");
		storePerson();
		List<Person> stored = session.createNamedQuery(Person.QUERY_FINDALL, Person.class).list();
		assertEquals(2, stored.size());
		
	}

}
