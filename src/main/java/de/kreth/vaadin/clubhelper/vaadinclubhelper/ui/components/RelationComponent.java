package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.Collection;

import com.vaadin.data.Binder;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Relation;

public class RelationComponent extends AbstractDataGrid<Relation> {

	private static final long serialVersionUID = 7813969695936351799L;
	private PersonDao dao;

	public RelationComponent(PersonDao dao) {
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

}
