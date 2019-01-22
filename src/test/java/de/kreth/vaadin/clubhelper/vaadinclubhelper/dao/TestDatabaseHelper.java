package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.List;
import java.util.function.Consumer;

import org.hibernate.Session;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubeventHasPerson;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public interface TestDatabaseHelper {

	/**
	 * executes the given runnable within a jpa Transaction.
	 * 
	 * @param r
	 */
	void transactional(Runnable r);

	void transactional(Consumer<Session> r);

	List<Person> insertPersons(int count);

	List<Person> createPersons(int count);

	ClubEvent creteEvent();

	List<ClubeventHasPerson> loadEventPersons();

	List<ClubEvent> allClubEvent();

	Person testInsertPerson();

	void cleanDatabase();
}