package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import javax.persistence.NoResultException;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

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
}
