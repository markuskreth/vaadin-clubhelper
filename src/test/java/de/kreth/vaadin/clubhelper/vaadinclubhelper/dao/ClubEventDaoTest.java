package de.kreth.vaadin.clubhelper.vaadinclubhelper.dao;

import static org.junit.Assert.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableAutoConfiguration
@Ignore
public class ClubEventDaoTest extends AbstractDatabaseTest {

	@Autowired
	private ClubEventDao dao;

//	@Autowired
//	private TestEntityManager entityManager; 

	@Test
	public void storeEvent() {
		dao.save(creteEvent());

		Query<ClubEvent> query = session.createNamedQuery("ClubEvent.findAll",
				ClubEvent.class);
		List<ClubEvent> result = query.list();
		assertEquals(1, result.size());
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
