package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import java.util.Optional;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.ui.HorizontalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonEditDetails;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

public class PersonEditView extends HorizontalLayout implements NamedView {

	public static final String VIEW_NAME = "PersonEditView";
	private static final long serialVersionUID = 1770993670570422036L;

	private PersonGrid personGrid;
	private PersonEditDetails personDetails;

	public PersonEditView(GroupDao groupDao, PersonDao personDao) {
		personGrid = new PersonGrid(groupDao, personDao);
		personGrid.setSizeFull();
		personGrid.onPersonEdit();
		personGrid.onPersonSelect(ev -> selectedPerson(ev));
//		personGrid.setSelectionMode(SelectionMode.SINGLE);

		personDetails = new PersonEditDetails(groupDao.listAll(), personDao, false);
		personDetails.setSizeFull();

		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponents(personGrid, personDetails);
		layout.setSizeFull();
		addComponent(layout);
	}

	void selectedPerson(SelectionEvent<Person> p) {
		Optional<Person> firstSelectedItem = p.getFirstSelectedItem();
		personDetails.setBean(firstSelectedItem.orElse(null));
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}
