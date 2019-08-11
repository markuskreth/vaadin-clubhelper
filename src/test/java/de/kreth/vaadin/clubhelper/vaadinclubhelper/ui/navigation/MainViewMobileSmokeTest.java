package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;

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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifierImpl;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarView;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
@Tag("spring")
@Disabled
public class MainViewMobileSmokeTest {

	@Autowired
	ApplicationContext context;

	@Autowired
	PersonBusiness personDao;

	SecurityVerifier securityGroupVerifier;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private EventBusiness eventBusiness;

	private MainView mainView;

	@Mock
	private ViewChangeEvent event;

	@BeforeEach
	void initUi() {
		MockitoAnnotations.initMocks(this);
		assertNotNull(personDao);
		Person person = new Person();
		GroupDef g1 = new GroupDef();
		person.setGroups(new HashSet<GroupDef>(Arrays.asList(g1)));
		securityGroupVerifier = new SecurityVerifierImpl();
		assertFalse(securityGroupVerifier.isLoggedin());
		mainView = new MainViewMobile(context, personDao, groupDao, eventBusiness, securityGroupVerifier);

		assertNotNull(securityGroupVerifier);
		assertNotNull(person);
		securityGroupVerifier.setLoggedinPerson(person);
		mainView.initUI(event);
	}

	@Test
	void detailNotInsideView() {
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
		assertNull(view);
	}

	@Test
	void calendarComponentNotShowing() {

		CalendarView calendar = (CalendarView) find(mainView, "main.calendar");
		assertNull(calendar);
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
