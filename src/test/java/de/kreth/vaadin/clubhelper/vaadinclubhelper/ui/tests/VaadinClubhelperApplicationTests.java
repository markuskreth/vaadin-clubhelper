package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import java.awt.GraphicsEnvironment;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

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
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class VaadinClubhelperApplicationTests {

	private static ChromeOptions options;
//	@Autowired
//	private WebTestClient webClient;
	@LocalServerPort
	int port;

	private WebDriver driver;

	@BeforeAll
	static void setupDriverConfiguration() {

		if (System.getProperty("webdriver.chrome.driver") == null) {
			System.setProperty("webdriver.chrome.driver", System.getenv("webdriver.chrome.driver"));
		}

		options = new ChromeOptions();
		options.setHeadless(!GraphicsEnvironment.isHeadless());
//		options.setPageLoadStrategy(PageLoadStrategy.EAGER);

	}

	@BeforeEach
	void setUp() throws Exception {
		driver = new ChromeDriver(options);
	}

	@AfterEach
	void shutdown() {
		if (driver != null) {
			driver.close();
		}
	}

	@Test
	public void seleniumWebWorkflow() {
		WebDriverWait driverWait = new WebDriverWait(driver, 45L);

		driver.get("http://localhost:" + port);

		driverWait.until(dr -> dr.findElements(By.id("calendar.month")).size() > 0);

		WebElement monthLabel = driver.findElement(By.id("calendar.month"));
		String month = monthLabel.getText();
		String expected = YearMonth.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
		org.hamcrest.MatcherAssert.assertThat(month, Matchers.containsString(expected));
	}
}
