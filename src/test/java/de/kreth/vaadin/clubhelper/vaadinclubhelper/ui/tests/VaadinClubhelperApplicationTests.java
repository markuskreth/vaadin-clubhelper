package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
//@SpringApplicationConfiguration(classes = VaadinClubhelperApplication.class)
//@WebAppConfiguration
public class VaadinClubhelperApplicationTests {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void contextLoads() {

		webClient.get().uri("/").exchange().expectStatus().isOk().expectBody(String.class).isEqualTo("Hello World");
	}

}
