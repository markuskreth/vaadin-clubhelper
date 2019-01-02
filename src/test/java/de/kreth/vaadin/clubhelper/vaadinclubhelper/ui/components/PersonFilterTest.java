package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AbstractDatabaseTest;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

class PersonFilterTest {

	private PersonFilter filter;
	private List<Person> persons;
	private final List<GroupDef> groups;

	public PersonFilterTest() {
		GroupDef adminGroup = new GroupDef();
		adminGroup.setId(1);
		adminGroup.setName("ADMIN");

		GroupDef competitorGroup = new GroupDef();
		competitorGroup.setName("Wettkämpfer");
		competitorGroup.setId(2);

		GroupDef participantGroup = new GroupDef();
		participantGroup.setName("ACTIVE");
		participantGroup.setId(3);

		GroupDef judgesGroup = new GroupDef();
		judgesGroup.setName("Kampfrichter");
		judgesGroup.setId(4);

		groups = Collections
				.unmodifiableList(Arrays.asList(adminGroup, competitorGroup, participantGroup, judgesGroup));

	}

	@BeforeEach
	void setUp() throws Exception {
		filter = new PersonFilter();
		persons = AbstractDatabaseTest.createPersons(5);
		assertEquals(5, persons.size());

		for (int i = 0; i < groups.size(); i++) {
			persons.get(i).add(groups.get(i));
			persons.get(0).add(groups.get(i)); // Person1 has all Rights.
		}

		assertEquals(groups.size(), persons.get(0).getGroups().size());
	}

	@Test
	void testNoFilterSet() {
		allPersonsAccepted();

		assertTrue(filter.test(null));
	}

	@Test
	void testPersonSelectionFilter() {
		Set<Person> selected = new HashSet<>(persons.subList(2, 4));
		filter.setSelectedPersons(selected);
		assertFalse(filter.test(persons.get(0)));
		assertFalse(filter.test(persons.get(1)));
		assertFalse(filter.test(persons.get(4)));
		assertTrue(filter.test(persons.get(2)));
		assertTrue(filter.test(persons.get(3)));

		filter.setSelectedPersons(null);
		testNoFilterSet();

		filter.setSelectedPersons(Collections.emptySet());
		allPersonsFiltered();

		filter.setSelectedPersons(new HashSet<>(persons));
		allPersonsAccepted();
	}

	@Test
	void testGroupSelection() {
		Set<GroupDef> selected = new HashSet<>();
		selected.add(groups.get(2));
		filter.setSelectedGroups(selected);

		assertTrue(filter.test(persons.get(0)));
		assertTrue(filter.test(persons.get(2)));

		assertFalse(filter.test(persons.get(1)));
		assertFalse(filter.test(persons.get(3)));
		assertFalse(filter.test(persons.get(4)));

	}

	public void allPersonsAccepted() {
		for (Person p : persons) {
			assertTrue(filter.test(p));
		}
	}

	public void allPersonsFiltered() {
		for (Person p : persons) {
			assertFalse(filter.test(p));
		}
	}

}
