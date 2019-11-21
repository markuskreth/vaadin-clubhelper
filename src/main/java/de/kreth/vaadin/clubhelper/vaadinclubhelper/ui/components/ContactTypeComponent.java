package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import com.vaadin.ui.ComboBox;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact.Type;

public class ContactTypeComponent extends ComboBox<String> {

	private static final long serialVersionUID = 1507070116708489598L;

	public ContactTypeComponent() {
		Type[] type = Contact.Type.values();
		String[] typeNames = new String[type.length];

		for (int i = 0; i < typeNames.length; i++) {
			typeNames[i] = type[i].getName();
		}
		setItems(typeNames);
		setEmptySelectionCaption("Bitte wählen");
	}

}
