package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import java.awt.GraphicsEnvironment;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class VaadinClubhelperApplicationTests {

	private static ChromeOptions options;
	@LocalServerPort
	int port;

	@Autowired
	EntityManager em;

	private WebDriver driver;
	private ZonedDateTime now;

	@BeforeAll
	static void setupDriverConfiguration() {

		if (System.getProperty("webdriver.chrome.driver") == null) {
			System.setProperty("webdriver.chrome.driver", System.getenv("webdriver.chrome.driver"));
		}

		options = new ChromeOptions();
		options.setHeadless(GraphicsEnvironment.isHeadless());

	}

	@BeforeEach
	void setUp() throws Exception {
		now = ZonedDateTime.now();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		GroupDef g1 = new GroupDef();
		g1.setName("ADMIN");
		GroupDef g2 = new GroupDef();
		g1.setName("Wettkämpfer");
		GroupDef g3 = new GroupDef();
		g1.setName("ACTIVE");

		em.persist(g1);
		em.persist(g2);
		em.persist(g3);

		ClubEvent ev = new ClubEventBuilder().withAllDay(true).withId("id").withCaption("caption")
				.withDescription("description").withiCalUID("iCalUID").withLocation("location")
				.withOrganizerDisplayName("mtv_allgemein").withStart(now).withEnd(now.plusDays(2)).build();

		em.persist(ev);

		ZonedDateTime holidayStart;
		ZonedDateTime holidayEnd;

		if (now.getDayOfMonth() >= 15) {
			holidayStart = now.minusDays(30);
			holidayEnd = now.minusDays(1);
		} else {

			holidayStart = now.plusDays(1);
			holidayEnd = now.plusDays(30);
		}

		ClubEvent holiday = new ClubEventBuilder().withAllDay(true).withId("holiday").withCaption("holiday")
				.withDescription("holiday").withiCalUID("iCalUID").withLocation("")
				.withOrganizerDisplayName("Schulferien").withStart(holidayStart).withEnd(holidayEnd).build();

		em.persist(holiday);

		Person p = new Person();
		p.setPrename("prename");
		p.setSurname("surname");
		p.setBirth(now.minusYears(13).toLocalDate());
		p.setUsername("username");
		p.setPassword("password");

		List<GroupDef> persongroups = Arrays.asList(g1, g2, g3);
		p.setPersongroups(persongroups);
		em.persist(p);

		tx.commit();

		driver = new ChromeDriver(options);
	}

	@AfterEach
	void shutdown() {
		if (driver != null) {
			driver.close();
		}
	}

	@Test
	public void verifyMonthViewComplete() {
		WebDriverWait driverWait = new WebDriverWait(driver, 45L);

		driver.get("http://localhost:" + port);

		driverWait.until(dr -> dr.findElements(By.id("calendar.month")).size() > 0);

		WebElement monthLabel = driver.findElement(By.id("calendar.month"));
		String month = monthLabel.getText();
		String expected = YearMonth.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
		assertThat(month, containsString(expected));

		List<WebElement> days = driver.findElements(By.className("v-calendar-day-number"));
		assertThat(days, Matchers.hasSize(Matchers.greaterThanOrEqualTo(now.getMonth().length(true))));

		WebElement today = findElementWithContent(String.valueOf(now.getDayOfMonth()));
		WebElement parentElement = today.findElement(By.xpath("./.."));
		WebElement todayContent = parentElement.findElement(By.className("v-calendar-event"));
		assertEquals("caption", todayContent.getText());

		List<WebElement> allEventElements = driver.findElements(By.className("v-calendar-event"));
		assertThat(allEventElements, Matchers.hasSize(Matchers.greaterThanOrEqualTo(16)));

	}

	public WebElement findElementWithContent(String content) {
		return driver.findElement(By.xpath(String.format("//*[contains(text(), '%s')]", content)));
	}
}
