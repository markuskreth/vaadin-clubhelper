package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;

import javax.persistence.NoResultException;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Adress;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relation;

public interface PersonDao extends IDao<Person> {

	/**
	 * Returns Person from Storage by username and (encrypted) password.
	 * 
	 * @param username login username
	 * @param password encrypted password
	 * @return
	 * @throws NoResultException if no result is found.
	 */
	Person findLoginUser(String username, String password) throws NoResultException;

	List<Relation> findRelationsFor(Person p);

	void delete(Contact c);

	void delete(Person p);

	void delete(Adress a);

}
