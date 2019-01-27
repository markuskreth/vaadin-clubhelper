package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.EntityAccessor;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relation;
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
			persistOrUpdate(startPass);
		}
		List<Contact> contacts = obj.getContacts();
		if (contacts != null) {
			for (Contact c : contacts) {
				persistOrUpdate(c);
			}
		}
	}

	@Transactional
	public void persistOrUpdate(EntityAccessor c) {
		if (c.hasValidId() == false) {
			entityManager.persist(c);
		} else {
			entityManager.merge(c);
		}
	}

	@Override
	public Person findLoginUser(String username, String password) {
		TypedQuery<Person> query = entityManager.createNamedQuery(Person.QUERY_FINDLOGIN, Person.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		return query.getSingleResult();
	}

	@Override
	public List<Relation> findRelationsFor(Person p) {
		Query query = entityManager.createNativeQuery(
				"SELECT id, person1, person2, TO_PERSON1_RELATION, TO_PERSON2_RELATION, changed, created, deleted FROM relative");

		@SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();
		List<Relation> relations = new ArrayList<>();
		for (Object[] r : result) {
			Relation rel = toRelative(r, p.getId());
			if (rel != null) {
				relations.add(rel);
			}
		}
		return relations;
	}

	private Relation toRelative(Object[] r, int ignoring) {

		if (r[1].equals(ignoring)) {
			return new Relation(entityManager.find(Person.class, r[2]), r[3].toString());
		} else if (r[2].equals(ignoring)) {
			return new Relation(entityManager.find(Person.class, r[1]), r[4].toString());
		}
		return null;
	}
}
