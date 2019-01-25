package de.kreth.vaadin.clubhelper.vaadinclubhelper.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vaadin.server.VaadinSession;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.tests.TestPersonGenerator;

class SecurityGroupVerifierImplTest {

	private SecurityVerifier securityGroupVerifier;
	private Map<SecurityGroups, GroupDef> groupDefinitions;
	private Person person;
	private Set<GroupDef> personGroups;
	@Mock
	private VaadinSession session;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		VaadinSession.setCurrent(session);
		securityGroupVerifier = new SecurityVerifierImpl();
		groupDefinitions = new HashMap<>();

		for (SecurityGroups g : SecurityGroups.values()) {
			GroupDef def = new GroupDef();
			def.setName(g.getValue());
			groupDefinitions.put(g, def);
		}
		person = TestPersonGenerator.generatePersonen(1).get(0);
		personGroups = new HashSet<>();
		person.setGroups(personGroups);
		securityGroupVerifier.setLoggedinPerson(person);
	}

	@Test
	void verifySessionHasPersonSet() {
		verify(session).setAttribute(Person.SESSION_LOGIN, person);
	}

	@Test
	void verifyGroupDefinitions() {
		for (SecurityGroups g : SecurityGroups.values()) {
			assertEquals(g.getValue(), groupDefinitions.get(g).getName());
		}
	}

	@Test
	void testGroupMatch() {
		personGroups.add(groupDefinitions.get(SecurityGroups.UEBUNGSLEITER));
		assertTrue(securityGroupVerifier.isPermitted(SecurityGroups.UEBUNGSLEITER));

		personGroups.add(groupDefinitions.get(SecurityGroups.ADMIN));
		assertTrue(securityGroupVerifier.isPermitted(SecurityGroups.ADMIN));

		personGroups.add(groupDefinitions.get(SecurityGroups.KAMPFRICHTER));
		assertTrue(securityGroupVerifier.isPermitted(SecurityGroups.KAMPFRICHTER));

	}

	@Test
	void testGroupNotMatching() {
		for (SecurityGroups g : SecurityGroups.values()) {
			assertFalse(securityGroupVerifier.isPermitted(g));
		}
		personGroups.add(groupDefinitions.get(SecurityGroups.UEBUNGSLEITER));
		personGroups.add(groupDefinitions.get(SecurityGroups.KAMPFRICHTER));

		assertFalse(securityGroupVerifier.isPermitted(SecurityGroups.ADMIN));

	}

}
