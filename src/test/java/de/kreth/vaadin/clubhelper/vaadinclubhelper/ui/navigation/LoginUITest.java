package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.LoginForm;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // , properties =
public class LoginUITest {

	@Autowired
	PersonBusiness personDao;

	@Autowired
	SecurityVerifier securityGroupVerifier;

	private LoginUI loginUi;

	@Mock
	private Navigator navigator;

	@BeforeEach
	void initUi() {
		MockitoAnnotations.initMocks(this);
		loginUi = new LoginUI(personDao, securityGroupVerifier);
	}

	@Test
	void testLogin() {
		assertNotNull(loginUi);
		ViewChangeEvent event = new ViewChangeEvent(navigator, loginUi, loginUi, "", "");
		loginUi.enter(event);
		Component loginForm = loginUi.getComponent(0);
		assertNotNull(loginForm);
		assertTrue((loginForm instanceof LoginForm));

	}
}
