package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.TextField;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarView;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("spring")
@Disabled
public class MainViewDesktopSmokeTest {

	@Autowired
	ApplicationContext context;

	@Autowired
	PersonBusiness personDao;

	@Autowired
	SecurityVerifier securityGroupVerifier;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private EventBusiness eventBusiness;

	private MainViewDesktop mainView;

	@Mock
	private ViewChangeEvent event;

	@BeforeEach
	void initUi() {
		MockitoAnnotations.initMocks(this);
		mainView = new MainViewDesktop(context, personDao, groupDao, eventBusiness, securityGroupVerifier);
		mainView.initUI(event);
	}

	@Test
	void detailShowingEventOnClick() {
		assertNull(find(mainView, SingleEventView.class.getName()));
		ClubEvent ev = new ClubEventBuilder()
				.withAllDay(true)
				.withCaption("caption")
				.withDescription("description")
				.withLocation("location")
				.withStart(ZonedDateTime.now())
				.withEnd(ZonedDateTime.now())
				.build();
		mainView.openDetailForEvent(ev);
		SingleEventView view = (SingleEventView) find(mainView, SingleEventView.class.getName());
		assertNotNull(view);
		TextField title = (TextField) find(view, "event.title");
		assertNotNull(title);
		assertEquals("caption", title.getValue());
		mainView.detailClosed();
		assertNull(find(mainView, SingleEventView.class.getName()));
		view.setEvent(null);
	}

	@Test
	void calendarComponentPresentAndShowsThisMonth() {

		CalendarView calendar = (CalendarView) find(mainView, "main.calendar");
		assertNotNull(calendar);
		ZonedDateTime today = ZonedDateTime.now();
		ZonedDateTime start = calendar.getStartDate();
		assertNotNull(start);
		assertEquals(today.getYear(), start.getYear());
		assertEquals(today.getMonthValue(), start.getMonthValue());
		assertEquals(1, start.getDayOfMonth());

		ZonedDateTime end = calendar.getEndDate();
		assertNotNull(end);

		assertEquals(today.getYear(), end.getYear());
		assertEquals(today.getMonthValue(), end.getMonthValue());
	}

	public Component find(HasComponents view, String id) {
		Component component = null;

		for (Component element : view) {
			if (id.equals(element.getId())) {
				component = element;
				break;
			}
			else if (element instanceof HasComponents) {
				component = find((HasComponents) element, id);
				if (component != null) {
					break;
				}
			}

		}
		return component;
	}
}
