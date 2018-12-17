package de.kreth.vaadin.clubhelper.vaadinclubhelper.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.ClubEventDao;

class CalendarTaskRefresherTest {

	@Mock
	private ClubEventDao dao;
	@Mock
	private EventBusiness eventBusiness;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(eventBusiness.loadEvents(any(), anyBoolean())).thenReturn(Collections.emptyList());
	}

	@Test
	void testSkip() {
		CalendarTaskRefresher r = new CalendarTaskRefresher();
		r.setDao(dao);
		r.setEventBusiness(eventBusiness);
		r.synchronizeCalendarTasks();
		verify(eventBusiness).loadEvents(any(), anyBoolean());
		System.setProperty(CalendarTaskRefresher.SKIP_EVENT_UPDATE, Boolean.TRUE.toString());

		r = new CalendarTaskRefresher();
		r.setDao(dao);
		r.setEventBusiness(eventBusiness);
		r.synchronizeCalendarTasks();
		verify(eventBusiness).loadEvents(any(), anyBoolean());
	}

}
