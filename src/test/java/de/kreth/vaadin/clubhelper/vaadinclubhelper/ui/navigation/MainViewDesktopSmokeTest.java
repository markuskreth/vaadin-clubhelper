package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarView;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("spring")
public class MainViewDesktopSmokeTest {

	@Autowired
	PersonDao personDao;

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
		mainView = new MainViewDesktop(personDao, groupDao, eventBusiness, securityGroupVerifier);
		mainView.initUI(event);
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
