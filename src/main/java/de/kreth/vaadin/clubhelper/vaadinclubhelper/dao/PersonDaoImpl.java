package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import org.springframework.stereotype.Repository;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@Repository
public class PersonDaoImpl extends AbstractDaoImpl<Person>
		implements
			PersonDao {

	public PersonDaoImpl() {
		super(Person.class);
	}

}
