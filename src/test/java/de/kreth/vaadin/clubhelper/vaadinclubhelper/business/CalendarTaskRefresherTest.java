package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.kreth.clubhelperbackend.google.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;

class CalendarTaskRefresherTest {

	@Mock
	private ClubEventDao dao;
	@Mock
	private CalendarAdapter calendarAdapter;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(calendarAdapter.getAllEvents(anyString())).thenReturn(Collections.emptyList());
	}

	@Test
	void testSkip() throws IOException, InterruptedException {
		System.setProperty(CalendarTaskRefresher.SKIP_EVENT_UPDATE, Boolean.FALSE.toString());
		CalendarTaskRefresher r = new CalendarTaskRefresher();
		r.setDao(dao);
		r.setCalendarAdapter(calendarAdapter);
		r.synchronizeCalendarTasks();
		verify(calendarAdapter).getAllEvents(anyString());
		System.setProperty(CalendarTaskRefresher.SKIP_EVENT_UPDATE, Boolean.TRUE.toString());

		r = new CalendarTaskRefresher();
		r.setDao(dao);
		r.setCalendarAdapter(calendarAdapter);
		r.synchronizeCalendarTasks();
		verify(calendarAdapter).getAllEvents(anyString());
	}

}
