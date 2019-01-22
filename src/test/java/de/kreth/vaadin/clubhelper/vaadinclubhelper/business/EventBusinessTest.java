package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.AltersgruppeDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestAltersgruppen;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestPersonGenerator;

class EventBusinessTest {

	EventBusiness eventBusiness;

	ZonedDateTime startDate = ZonedDateTime.of(LocalDateTime.of(2019, 1, 1, 0, 0), ZoneId.systemDefault());

	@Mock
	ClubEventDao dao;

	@Mock
	AltersgruppeDao altersgruppeDao;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		eventBusiness = new EventBusiness();
		eventBusiness.clubEventDao = dao;
		eventBusiness.altersgruppeDao = altersgruppeDao;
	}

	@Test
	void testCreateMeldung() {
		ClubEvent ev = new ClubEventBuilder().withId("id").withiCalUID("iCalUID").withLocation("location")
				.withCaption("caption").withDescription("description").withOrganizerDisplayName("organizerDisplayName")
				.withStart(startDate).withEnd(startDate).withAllDay(true).build();

		List<Altersgruppe> altersgruppen = TestAltersgruppen.getAltersgruppen();
		List<Person> personen = TestPersonGenerator.generatePersonen(10);
		int count = 1;
		for (Person p : personen) {
			p.setEvents(new HashSet<>(Arrays.asList(ev)));
			p.setBirth(LocalDate.of(1993 + (count * 3), count, count + 2));
			count++;
		}

		ev.setAltersgruppen(new HashSet<>(altersgruppen));
		ev.setPersons(new HashSet<>(personen));

		eventBusiness.setSelected(ev);
		EventMeldung meldung = eventBusiness.createMeldung();
		assertNotNull(meldung);

	}

	@Test
	void testAltersgruppeBetween() {
		Altersgruppe gruppe = new Altersgruppe();
		gruppe.setBezeichnung("bezeichnung");
		gruppe.setStart(2000);
		gruppe.setEnd(2005);

		assertFalse(gruppe.isBetween(startDate));
		assertTrue(gruppe.isBetween(ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0), ZoneId.systemDefault())));
		assertTrue(gruppe.isBetween(ZonedDateTime.of(LocalDateTime.of(2005, 12, 31, 23, 59), ZoneId.systemDefault())));
		assertTrue(gruppe.isBetween(ZonedDateTime.of(LocalDateTime.of(2002, 1, 1, 0, 0), ZoneId.systemDefault())));

		assertFalse(gruppe.isBetween(ZonedDateTime.of(LocalDateTime.of(1999, 12, 31, 23, 59), ZoneId.systemDefault())));
		assertFalse(gruppe.isBetween(ZonedDateTime.of(LocalDateTime.of(2006, 1, 1, 0, 0), ZoneId.systemDefault())));

	}
}
