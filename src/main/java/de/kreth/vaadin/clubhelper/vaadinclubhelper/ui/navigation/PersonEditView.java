package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonEditDetails;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

public class PersonEditView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1770993670570422036L;

	private PersonGrid personGrid;

	private PersonEditDetails personDetails;

	private Navigator navigator;

	public PersonEditView(GroupDao groupDao, PersonBusiness personDao, boolean horizontalLayout) {
		setMargin(true);

		personGrid = new PersonGrid(groupDao, personDao);
		personGrid.setSizeFull();
		personGrid.onPersonEdit();
		personGrid.onPersonSelect(ev -> selectedPerson(ev));
		personGrid.enableDeleteColumn(true);

		personDetails = new PersonEditDetails(groupDao.listAll(), personDao, false);
		personDetails.setSizeFull();
		personDetails.setPersonChangeHandler(personGrid::refreshItem);

		if (horizontalLayout) {
			addComponent(createHorizontalLayout());
		}
		else {
			addComponent(createVerticalLayout());
		}
		Button addPerson = new Button("Hinzufügen");
		addPerson.addClickListener(ev -> addPerson());

		addComponent(addPerson);
		Button backButton = new Button("Zurück");
		backButton.addClickListener(ev -> navigator.navigateTo(ClubhelperViews.MainView.name()));
		addComponent(backButton);
	}

	public HorizontalLayout createHorizontalLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponents(personGrid, personDetails);
		layout.setExpandRatio(personGrid, 1f);
		layout.setExpandRatio(personDetails, 2f);
		layout.setSizeFull();
		return layout;
	}

	public VerticalLayout createVerticalLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.addComponents(personGrid, personDetails);
		layout.setSizeFull();
		return layout;
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
		if (personDetails.hasChanges()) {
			VerticalLayout content = new VerticalLayout();
			Window dlg = new Window("Änderungen verwerfen?", content);
			dlg.setClosable(false);
			dlg.setModal(true);

			Label message = new Label(
					"Die Personendaten wurden geändert.<br />Sollen diese Änderungen verworfen werden?",
					ContentMode.HTML);
			content.addComponent(message);
			HorizontalLayout buttons = new HorizontalLayout();
			Button ok = new Button("Ja", ev -> {
				dlg.setVisible(false);
				PersonEditView.this.getUI().removeWindow(dlg);
				Optional<Person> firstSelectedItem = p.getFirstSelectedItem();
				personDetails.setBean(firstSelectedItem.orElse(null));
			});
			Button cancel = new Button("Nein", ev -> {
				dlg.setVisible(false);
				PersonEditView.this.getUI().removeWindow(dlg);
			});
			buttons.addComponents(ok, cancel);

			content.addComponent(buttons);
			getUI().addWindow(dlg);
		}
		else {
			Optional<Person> firstSelectedItem = p.getFirstSelectedItem();
			personDetails.setBean(firstSelectedItem.orElse(null));
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		this.navigator = event.getNavigator();
	}

}
