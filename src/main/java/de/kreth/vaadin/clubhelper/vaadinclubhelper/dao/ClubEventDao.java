package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.util.Collection;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public interface ClubEventDao extends IDao<ClubEvent> {

	/**
	 * Updates Event with participants, {@link ClubEvent} and {@link Person} must be
	 * persistent already.
	 * 
	 * @param event
	 * @param persons complete List of Persons, including Persons already persisted.
	 */
	void addPersons(ClubEvent event, Collection<Person> persons);

	void updateEventType(ClubEvent obj);
}
