package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Repository
public class PersonDaoImpl implements PersonDao {

	@Autowired
	EntityManager em;

	@Override
	public void save(Person obj) {
		em.persist(obj);
	}

	@Override
	public List<Person> list() {
		TypedQuery<Person> query = em.createNamedQuery(Person.QUERY_FINDALL,
				Person.class);
		return query.getResultList();
	}

}
