package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import com.vaadin.ui.ComboBox;

public class ContactTypeComponent extends ComboBox<String> {

	private static final long serialVersionUID = 1507070116708489598L;

	public ContactTypeComponent() {
		setItems("Telefon", "Email", "Mobile");
		setEmptySelectionCaption("Bitte wählen");
	}

}
