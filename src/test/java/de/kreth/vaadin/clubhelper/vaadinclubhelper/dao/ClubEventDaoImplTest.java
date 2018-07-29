package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.hibernate.Transaction;
import org.junit.Test;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class ClubEventDaoImplTest extends AbstractDatabaseTest {

	@Test
	public void testInsertPerson() {
		Person p = new Person();
		p.setPrename("prename");
		p.setSurname("surname");
		p.setBirth(new Date());
		Transaction tx = session.beginTransaction();
		session.save(p);
		tx.commit();
	}

	@Test
	public void testInsertEvent() {
		ClubEvent ev = creteEvent();

		Transaction tx = session.beginTransaction();
		session.save(ev);
		tx.commit();
	}

	@Test
	public void testSelectEvents() {
		ClubEvent ev = creteEvent();

		Transaction tx = session.beginTransaction();
		session.save(ev);
		tx.commit();
	}

	private ClubEvent creteEvent() {
		ClubEvent ev = ClubEventBuilder.builder().withId("id").withAllDay(true)
				.withCaption("caption").withDescription("description")
				.withStart(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0,
						ZoneId.systemDefault()))
				.withEnd(ZonedDateTime.of(2018, 8, 13, 0, 0, 0, 0,
						ZoneId.systemDefault()))
				.withiCalUID("iCalUID")
				.withOrganizerDisplayName("organizerDisplayName").build();
		return ev;
	}
}
