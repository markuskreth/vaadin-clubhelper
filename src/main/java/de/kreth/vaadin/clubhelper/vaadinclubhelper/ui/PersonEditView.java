package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonEditDialog;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

public class PersonEditView extends HorizontalLayout implements View {

	public static final String VIEW_NAME = "PersonEditView";
	private static final long serialVersionUID = 1770993670570422036L;

	private PersonGrid personGrid;
	private PersonDao personDao;
	private GroupDao groupDao;

	public PersonEditView(GroupDao groupDao, PersonDao personDao) {
		this.groupDao = groupDao;
		this.personDao = personDao;
		personGrid = new PersonGrid(groupDao, personDao);
		personGrid.onPersonEdit(p -> onPersonEdit(p));
		addComponent(personGrid);
	}

	private void onPersonEdit(Person p) {
		PersonEditDialog dlg = new PersonEditDialog(groupDao.listAll(), p, personDao);
		getUI().addWindow(dlg);
	}

}
