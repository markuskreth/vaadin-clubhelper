package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Adress;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.EntityAccessor;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relation;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpass;

@Repository
public class PersonDaoImpl extends AbstractDaoImpl<Person> implements PersonDao {

	private static final long serialVersionUID = -5809935282146356282L;

	public PersonDaoImpl() {
		super(Person.class);
	}

	@Override
	@Transactional
	public void save(Person obj) {
		checkSubEntities(obj);
		super.save(obj);
	}

	@Transactional
	public void checkSubEntities(Person obj) {
		Startpass startPass = obj.getStartpass();
		if (startPass != null) {
			persistOrUpdate(startPass);
		}
		List<Contact> contacts = obj.getContacts();
		if (contacts != null) {
			for (Contact c : contacts) {
				persistOrUpdate(c);
			}
		}
		List<Adress> adresses = obj.getAdresses();
		if (adresses != null) {
			for (Adress a : adresses) {
				persistOrUpdate(a);
			}
		}
	}

	@Transactional
	public void persistOrUpdate(EntityAccessor c) {
		Date now = new Date();
		c.setChanged(now);
		if (entityManager.contains(c) || c.hasValidId()) {
			entityManager.merge(c);
		}
		else {
			c.setCreated(now);
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
		Person p = null;
		String relation = null;
		if (r[1].equals(ignoring)) {
			p = entityManager.find(Person.class, r[2]);
			relation = r[3].toString();
		}
		else if (r[2].equals(ignoring)) {
			p = entityManager.find(Person.class, r[1]);
			relation = r[4].toString();
		}
		if (p != null && p.getDeleted() == null) {
			return new Relation(p, relation);
		}
		return null;
	}

	@Override
	@Transactional
	public void delete(Contact c) {
		c.setDeleted(new Date());
		entityManager.merge(c);
		Person person = c.getPerson();
		person.getContacts().remove(c);
	}

	@Override
	@Transactional
	public void delete(Person p) {
		p.setDeleted(new Date());
		entityManager.merge(p);
	}

	@Override
	@Transactional
	public void delete(Adress a) {

		a.setDeleted(new Date());
		entityManager.merge(a);
		Person person = a.getPerson();
		person.getAdresses().remove(a);
	}
}
