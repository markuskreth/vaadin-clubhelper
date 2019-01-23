package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.EntityAccessor;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpass;

@Repository
public class PersonDaoImpl extends AbstractDaoImpl<Person> implements PersonDao {

	public PersonDaoImpl() {
		super(Person.class);
	}

	@Override
	public void save(Person obj) {
		checkSubEntities(obj);
		super.save(obj);
	}

	public void checkSubEntities(Person obj) {
		Startpass startPass = obj.getStartpass();
		if (startPass != null && startPass.hasValidId() == false) {
			persistIfNew(startPass);
		}
		List<Contact> contacts = obj.getContacts();
		if (contacts != null) {
			for (Contact c : contacts) {
				persistIfNew(c);
			}
		}
	}

	public void persistIfNew(EntityAccessor c) {
		if (c.hasValidId() == false) {
			entityManager.persist(c);
		}
	}

	@Override
	public Person findLoginUser(String username, String password) {
		TypedQuery<Person> query = entityManager.createNamedQuery(Person.QUERY_FINDLOGIN, Person.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		return query.getSingleResult();
	}

}
