package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Repository
public class PersonDaoImpl extends AbstractDaoImpl<Person> implements PersonDao {

	public PersonDaoImpl() {
		super(Person.class);
	}

	@Override
	public Person findLoginUser(String username, String password) {
		TypedQuery<Person> query = em.createNamedQuery(Person.QUERY_FINDLOGIN, Person.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		return query.getSingleResult();
	}

}
