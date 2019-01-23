package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.Set;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class PersonGroupValidator implements Validator<Person> {

	private static final long serialVersionUID = -941938281840937295L;

	@Override
	public ValidationResult apply(Person value, ValueContext context) {
		Set<GroupDef> gr = value.getGroups();
		if (gr != null && gr.isEmpty() == false) {
			return ValidationResult.ok();
		}

		return ValidationResult.error("Es müssen Gruppen gesetzt sein!");
	}
}
