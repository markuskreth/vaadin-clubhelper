package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.TextField;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Contact;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class ContactGrid extends AbstractDataGrid<Contact> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2573761302198992085L;
	private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

	@Override
	protected void createColumnAndBinding(Binder<Contact> binder) {
		Binding<Contact, String> typeBinding = binder.bind(new ContactTypeComponent(), Contact::getType,
				Contact::setType);
		Binding<Contact, String> valueBinding = binder.bind(new TextField(), Contact::getValue, Contact::setValue);

		addColumn(Contact::getType).setCaption("Kontaktart").setEditorBinding(typeBinding);
		addColumn(Contact::getValue).setCaption("Wert").setEditorBinding(valueBinding);
	}

	@Override
	protected ValidationResult validate(Contact obj, ValueContext context) {
		if (obj.getType().equalsIgnoreCase("email")) {
			return new EmailValidator("Emailformat nicht gültig!").apply(obj.getValue(), context);
		} else if (obj.getType().equalsIgnoreCase("Telefon") || obj.getType().equalsIgnoreCase("Mobile")) {
			try {
				PhoneNumber phone = phoneUtil.parse(obj.getValue(), "DE");
				if (phoneUtil.isValidNumber(phone)) {
					obj.setValue(phoneUtil.format(phone,
							com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
					return ValidationResult.ok();
				} else {
					return ValidationResult.error("Fehler beim Validieren von Telefonnummer: " + obj.getValue());
				}
			} catch (NumberParseException e) {
				return ValidationResult.error("Fehler beim Validieren von Telefonnummer: " + obj.getValue());
			}
		}
		return ValidationResult.error("Keine Validation für Typ " + obj.getType());
	}

	@Override
	protected Collection<? extends Contact> readValues(Person person) {
		if (person == null) {
			return Collections.emptyList();
		}
		List<Contact> contacts = person.getContacts();
		if (contacts == null) {
			return Collections.emptyList();
		}
		return contacts.stream().filter(e -> e.getDeleted() == null).collect(Collectors.toList());
	}

	@Override
	protected Contact createNewItem() {
		return new Contact();
	}

}