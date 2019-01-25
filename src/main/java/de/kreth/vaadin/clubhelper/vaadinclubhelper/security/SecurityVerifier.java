package de.kreth.vaadin.clubhelper.vaadinclubhelper.security;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public interface SecurityVerifier {

	void setLoggedinPerson(Person person);

	Person getLoggedinPerson();

	boolean isPermitted(SecurityGroups... groups);

	boolean isLoggedin();

}