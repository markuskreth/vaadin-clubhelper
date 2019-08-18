package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.data.ValidationResult;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

class PersonGroupValidatorTest {

	private PersonGroupValidator validator;

	private Person person;

	@BeforeEach
	void setUp() throws Exception {
		validator = new PersonGroupValidator();
		person = new Person();
	}

	@Test
	void testError() {
		ValidationResult result = validator.apply(person, null);
		assertTrue("Error expected on empty groups", result.isError());
	}

	@Test
	void testOk() {
		GroupDef group = new GroupDef();
		person.add(group);
		ValidationResult result = validator.apply(person, null);
		assertFalse(result.isError());
	}

}
