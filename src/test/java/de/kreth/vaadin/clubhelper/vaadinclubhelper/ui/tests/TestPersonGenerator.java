package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import java.util.ArrayList;
import java.util.List;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Startpass;

public class TestPersonGenerator {

	public static List<Person> generatePersonen(int count) {
		List<Person> personen = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			Person p = new Person();
			p.setId(i);
			p.setPrename("prename_" + i);
			p.setSurname("surname_" + i);
			p.setUsername("username_" + i);
			p.setPassword("password_" + i);

			Startpass sp = new Startpass();
			sp.setStartpassNr("startpassNr_" + i);
			sp.setId(i);
			sp.setPerson(p);
			p.setStartpass(sp);
			personen.add(p);
		}
		return personen;
	}
}
