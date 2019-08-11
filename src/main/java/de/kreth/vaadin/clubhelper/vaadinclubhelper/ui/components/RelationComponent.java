package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.Collection;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relation;

public class RelationComponent extends AbstractDataGrid<Relation> {

	private static final long serialVersionUID = 7813969695936351799L;

	private PersonBusiness dao;

	public RelationComponent(PersonBusiness dao) {
		this.dao = dao;
		setEditorEnabled(false);
	}

	@Override
	protected void createColumnAndBinding(Binder<Relation> binder) {
		addColumn(r -> r.getRelation().getLocalized()).setCaption("Beiehung");
		addColumn(r -> r.getPerson().getPrename() + " " + r.getPerson().getSurname());
	}

	@Override
	protected Collection<? extends Relation> readValues(Person person) {
		return dao.findRelationsFor(person);
	}

	@Override
	protected Relation createNewItem() {
		return null;
	}

	@Override
	protected ValidationResult validate(Relation obj, ValueContext context) {
		return ValidationResult.ok();
	}

}
