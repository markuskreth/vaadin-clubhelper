package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonGroupValidator implements Validator<Person> {

	private static final long serialVersionUID = -941938281840937295L;

	@Override
	public ValidationResult apply(Person value, ValueContext context) {
		if (value.hasAnyGroup()) {
			return ValidationResult.error("Es müssen Gruppen gesetzt sein!");
		}

		return ValidationResult.ok();
	}
}
