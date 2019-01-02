package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AbstractDatabaseTest;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

class PersonFilterTest {

	private PersonFilter filter;
	private List<Person> persons;

	@BeforeEach
	void setUp() throws Exception {
		filter = new PersonFilter();
		persons = AbstractDatabaseTest.createPersons(5);
		assertEquals(5, persons.size());
	}

	@Test
	void testNoFilterSet() {
		allAccepted();

		assertTrue(filter.test(null));
	}

	@Test
	void testSelectionFilter() {
		Set<Person> selected = new HashSet<>(persons.subList(2, 4));
		filter.setSelected(selected);
		assertFalse(filter.test(persons.get(0)));
		assertFalse(filter.test(persons.get(1)));
		assertFalse(filter.test(persons.get(4)));
		assertTrue(filter.test(persons.get(2)));
		assertTrue(filter.test(persons.get(3)));

		filter.setSelected(null);
		testNoFilterSet();

		filter.setSelected(Collections.emptySet());
		allFiltered();

		filter.setSelected(new HashSet<>(persons));
		allAccepted();
	}

	public void allAccepted() {
		for (Person p : persons) {
			assertTrue(filter.test(p));
		}
	}

	public void allFiltered() {
		for (Person p : persons) {
			assertFalse(filter.test(p));
		}
	}

}
