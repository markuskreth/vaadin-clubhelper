package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.Collection;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.TextField;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Adress;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public class AdressComponent extends AbstractDataGrid<Adress> {

	private static final long serialVersionUID = 1608846765622407233L;

	@Override
	protected ValidationResult validate(Adress obj, ValueContext context) {
		return ValidationResult.ok();
	}

	@Override
	protected Adress createNewItem() {
		return new Adress();
	}

	@Override
	protected void createColumnAndBinding(Binder<Adress> binder) {

		addColumn(Adress::getAdress1).setCaption("Adresse 1")
				.setEditorBinding(binder.bind(new TextField(), Adress::getAdress1, Adress::setAdress1));
		addColumn(Adress::getAdress2).setCaption("Adresse 2")
				.setEditorBinding(binder.bind(new TextField(), Adress::getAdress2, Adress::setAdress2));
		addColumn(Adress::getPlz).setCaption("PLZ")
				.setEditorBinding(binder.bind(new TextField(), Adress::getPlz, Adress::setPlz));
		addColumn(Adress::getCity).setCaption("Ort")
				.setEditorBinding(binder.bind(new TextField(), Adress::getCity, Adress::setCity));
	}

	@Override
	protected Collection<? extends Adress> readValues(Person person) {
		return person.getAdresses().stream().filter(e -> e.getDeleted() == null).collect(Collectors.toList());
	}

}
