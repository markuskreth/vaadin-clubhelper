package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.GraphicsEnvironment;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.test.context.TestPropertySource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.TestDatabaseHelper;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEventBuilder;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Disabled
@TestPropertySource(locations = "classpath:test.properties")
public class VaadinClubhelperApplicationTests {

	private static ChromeOptions options;

	private static final AtomicInteger idCount = new AtomicInteger();

	@LocalServerPort
	int port;

	@Autowired
	EntityManager entityManager;

	@Autowired
	TestDatabaseHelper testDatabaseHelper;

	private WebDriver driver;
	private ZonedDateTime now;
	private WebDriverWait driverWait;

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

		insertDataIntoDatabase();

		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		driverWait = new WebDriverWait(driver, 45L);
	}

	public void insertDataIntoDatabase() {
		TypedQuery<GroupDef> query = entityManager.createQuery("FROM groupDef", GroupDef.class);
		if (!query.getResultList().isEmpty()) {
			return;
		}
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();

		GroupDef adminGroup = new GroupDef();
		adminGroup.setName("ADMIN");
		GroupDef competitorGroup = new GroupDef();
		competitorGroup.setName("Wettkämpfer");
		GroupDef participantGroup = new GroupDef();
		participantGroup.setName("ACTIVE");

		entityManager.persist(adminGroup);
		entityManager.persist(competitorGroup);
		entityManager.persist(participantGroup);

		ClubEvent ev = new ClubEventBuilder().withAllDay(true).withId("id" + idCount.incrementAndGet())
				.withCaption("caption").withDescription("description").withiCalUID("iCalUID").withLocation("location")
				.withOrganizerDisplayName("mtv_allgemein").withStart(now).withEnd(now.plusDays(2)).build();

		entityManager.persist(ev);

		ZonedDateTime holidayStart;
		ZonedDateTime holidayEnd;

		if (now.getDayOfMonth() >= 15) {
			holidayStart = now.minusDays(30);
			holidayEnd = now.minusDays(1);
		} else {

			holidayStart = now.plusDays(1);
			holidayEnd = now.plusDays(30);
		}

		ClubEvent holiday = new ClubEventBuilder().withAllDay(true).withId("holiday" + idCount.incrementAndGet())
				.withCaption("holiday").withDescription("holiday").withiCalUID("iCalUID").withLocation("")
				.withOrganizerDisplayName("Schulferien").withStart(holidayStart).withEnd(holidayEnd).build();

		entityManager.persist(holiday);

		Person p = new Person();
		p.setPrename("prename");
		p.setSurname("Allgroups");
		p.setBirth(now.minusYears(13).toLocalDate());
		p.setUsername("Allgroups");
		p.setPassword("password");
		p.setPersongroups(Arrays.asList(adminGroup, competitorGroup, participantGroup));
		entityManager.persist(p);

		p = new Person();
		p.setPrename("prename");
		p.setSurname("participant");
		p.setBirth(now.minusYears(10).toLocalDate());
		p.setUsername("participant");
		p.setPassword("password");
		p.setPersongroups(Arrays.asList(participantGroup));
		entityManager.persist(p);

		p = new Person();
		p.setPrename("prename");
		p.setSurname("competitor");
		p.setBirth(now.minusYears(10).toLocalDate());
		p.setUsername("competitor");
		p.setPassword("password");
		p.setPersongroups(Arrays.asList(competitorGroup, participantGroup));
		entityManager.persist(p);

		tx.commit();
	}

	@AfterEach
	void shutdown() {
		if (driver != null) {
			driver.close();
		}
		testDatabaseHelper.cleanDatabase();
	}

	@Test
	public void verifyMonthViewComplete() {

		loadApplication();

		WebElement monthLabel = driver.findElement(By.id("calendar.month"));
		String month = monthLabel.getText();
		String expected = YearMonth.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
		assertThat(month, containsString(expected));

		List<WebElement> days = driver.findElements(By.className("v-calendar-day-number"));
		assertThat(days, Matchers.hasSize(Matchers.greaterThanOrEqualTo(now.getMonth().length(true))));

		WebElement today = driver.findElement(By.className("v-calendar-month-day-today"));
		WebElement todayContent = today.findElement(By.className("v-calendar-event-start"));
		assertEquals("caption", todayContent.getText());

		List<WebElement> allEventElements = driver.findElements(By.className("v-calendar-event"));
		assertThat(allEventElements, Matchers.hasSize(Matchers.greaterThanOrEqualTo(16)));

	}

	@Test
	public void verifyPersonViewForEvent() {

		loadApplication();

		clickTodaysEvent();

		By filterGroupId = By.id("person.filter.groups");
		waitFor(filterGroupId);

		WebElement titleElement = driver.findElement(By.id("event.title"));
		assertEquals("caption", titleElement.getAttribute("value"));

		WebElement personList = driver.findElement(By.id("person.grid")).findElement(By.tagName("table"));
		WebElement allGroupsPerson = findElementWithContent(personList, "Allgroups");
		WebElement competitorPerson = findElementWithContent(personList, "competitor");
		WebElement participantPerson = findElementWithContent(personList, "participant");

		assertNotNull(allGroupsPerson);
		assertNotNull(competitorPerson);
		assertNotNull(participantPerson);

	}

	public void clickTodaysEvent() {
		WebElement today = findElementWithContent("caption");
		today.click();
	}

	public void loadApplication() {
		driver.get("http://localhost:" + port);
		waitFor(By.id("calendar.month"));
	}

	public void waitFor(final By idMonth) {
		driverWait.until(dr -> {
			return dr.findElements(idMonth).size() > 0 && dr.findElements(idMonth).get(0).isDisplayed();
		});
	}

	public WebElement findElementWithContent(String content) {
		return findElementWithContent(null, content);
	}

	public WebElement findElementWithContent(WebElement parent, String content) {
		if (parent != null) {
			return parent.findElement(By.xpath(String.format("//*[contains(text(), '%s')]", content)));
		} else {
			return driver.findElement(By.xpath(String.format("//*[contains(text(), '%s')]", content)));
		}
	}
}
