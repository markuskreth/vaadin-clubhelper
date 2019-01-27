package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.Collection;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.ui.TextField;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class ContactGrid extends AbstractDataGrid<Contact> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2573761302198992085L;

	@Override
	public void createColumnAndBinding(Binder<Contact> binder) {
		Binding<Contact, String> typeBinding = binder.bind(new ContactTypeComponent(), Contact::getType,
				Contact::setType);
		Binding<Contact, String> valueBinding = binder.bind(new TextField(), Contact::getValue, Contact::setValue);

		addColumn(Contact::getType).setCaption("Kontaktart").setEditorBinding(typeBinding);
		addColumn(Contact::getValue).setCaption("Wert").setEditorBinding(valueBinding);
	}

	@Override
	protected Collection<? extends Contact> readValues(Person person) {
		return person.getContacts().stream().filter(e -> e.getDeleted() == null).collect(Collectors.toList());
	}

}