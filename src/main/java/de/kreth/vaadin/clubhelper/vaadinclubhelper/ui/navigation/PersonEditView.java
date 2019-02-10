package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonEditDetails;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

public class PersonEditView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1770993670570422036L;

	private PersonGrid personGrid;
	private PersonEditDetails personDetails;

	private Navigator navigator;

	public PersonEditView(GroupDao groupDao, PersonDao personDao) {
		setMargin(true);

		personGrid = new PersonGrid(groupDao, personDao);
		personGrid.setSizeFull();
		personGrid.onPersonEdit();
		personGrid.onPersonSelect(ev -> selectedPerson(ev));
		personGrid.enableDeleteColumn(true);

		personDetails = new PersonEditDetails(groupDao.listAll(), personDao, false);
		personDetails.setSizeFull();
		personDetails.setPersonChangeHandler(personGrid::refreshItem);

		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponents(personGrid, personDetails);
		layout.setExpandRatio(personGrid, 1f);
		layout.setExpandRatio(personDetails, 2f);
		layout.setSizeFull();
		addComponent(layout);
		Button addPerson = new Button("Hinzufügen");
		addPerson.addClickListener(ev -> addPerson());

		addComponent(addPerson);
		Button backButton = new Button("Zurück");
		backButton.addClickListener(ev -> navigator.navigateTo(ClubhelperViews.MainView.name()));
		addComponent(backButton);
	}

	private void addPerson() {
		Person person = new Person();
		person.setGroups(new HashSet<>());
		person.setAdresses(new ArrayList<>());
		person.setEvents(new HashSet<>());
		person.setRelatives1(new ArrayList<>());
		personDetails.setBean(person);
		personGrid.deselectAll();
	}

	void selectedPerson(SelectionEvent<Person> p) {
		Optional<Person> firstSelectedItem = p.getFirstSelectedItem();
		personDetails.setBean(firstSelectedItem.orElse(null));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		this.navigator = event.getNavigator();
	}

}
