package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.text.DateFormat;

import com.vaadin.ui.Grid;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonGrid extends Grid<Person> {

	private static final long serialVersionUID = -8148097982839343673L;
	private final DateFormat birthFormat = DateFormat
			.getDateInstance(DateFormat.MEDIUM);

	public PersonGrid() {
		addColumn(Person::getPrename);
		addColumn(Person::getSurname);
		addColumn(Person::getBirth, b -> birthFormat.format(b));
	}

}
