package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.server.VaadinSession;

import de.kreth.googleconnectors.calendar.CalendarAdapter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.TestDatabaseHelper;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // , properties =
																// "spring.main.web-application-type=reactive"
@AutoConfigureWebTestClient
@Tag("spring")
public class SmokeUITest {

	@Autowired
	WebTestClient webClient;

	@Autowired
	PersonDao personDao;

	@Autowired
	GroupDao groupDao;

	@Autowired
	EventBusiness eventBusiness;

	@Autowired
	PflichtenDao pflichtenDao;

	@Autowired
	SecurityVerifier securityGroupVerifier;

	@Autowired
	CalendarAdapter calendarAdapter;

	@Autowired
	ClubhelperNavigation clubhelperNavigation;

	@Test
	public void autowiredComponentsArePresent() {
		List<Field> autowiredFields = Arrays.asList(getClass().getDeclaredFields()).stream()
				.filter(f -> f.getAnnotation(Autowired.class) != null)
				.collect(Collectors.toList());

		for (Field field : autowiredFields) {
			try {
				Object object = field.get(this);
				assertNotNull(object, "Null object in field: " + field);
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException("Error accessing object in Field " + field, e);
			}
		}
	}

	@Test
	void databaseIsEmpty() {
		List<Person> allPersons = personDao.listAll();
		assertEquals(0, allPersons.size());
	}

	@Test
	@Transactional
	void useDatabase() {
		GroupDef group = new GroupDef();
		group.setName("admin");

		this.groupDao.save(group);
		TestDatabaseHelper.afterCommit(() -> {
			assertEquals(1, groupDao.listAll().size());
		});
	}

	@Test
	void securityGroupVerifierIsLoggedin() {
		VaadinSession session = mock(VaadinSession.class);
		VaadinSession.setCurrent(session);
		assertFalse(securityGroupVerifier.isLoggedin());
		Person person = new Person();
		Set<GroupDef> groups = new HashSet<GroupDef>();
		groups.add(new GroupDef());
		person.setGroups(groups);
		securityGroupVerifier.setLoggedinPerson(person); // must be set and have at least one Group defined.
		assertTrue(securityGroupVerifier.isLoggedin());
	}

	@Test
	void webClientAccess() {
		ResponseSpec responseSpec = this.webClient.get().uri("/").exchange();
		responseSpec.expectStatus().isOk();
		BodyContentSpec body = responseSpec.expectBody();
		EntityExchangeResult<byte[]> result = body.returnResult();
		String content = new String(result.getResponseBody());
		assertFalse(content.isBlank());
		assertTrue(content.toLowerCase().contains("vaadin"));
	}
}
