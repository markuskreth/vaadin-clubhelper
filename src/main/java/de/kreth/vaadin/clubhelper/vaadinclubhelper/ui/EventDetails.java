package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;

public class EventDetails extends GridLayout implements NamedView {

	public static final String VIEW_NAME = "EventDetails";
	private static final long serialVersionUID = 8290150079638390995L;

	private final EventBusiness eventBusiness;
	private final PersonDao personDao;
	private final GroupDao groupDao;

	private Person loggedinPerson;
	private ClubEvent currentEvent;
	private SingleEventView eventView;
	private PersonGrid personGrid;

	public EventDetails(PersonDao personDao, GroupDao groupDao, EventBusiness eventBusiness) {
		super(3, 5);
		this.eventBusiness = eventBusiness;
		this.personDao = personDao;
		this.groupDao = groupDao;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Navigator navigator = event.getNavigator();
		loggedinPerson = (Person) getSession().getAttribute(Person.SESSION_LOGIN);
		currentEvent = eventBusiness.getCurrent();

		eventView = new SingleEventView();
		eventView.setEvent(currentEvent);

		personGrid = new PersonGrid(groupDao, personDao);
		personGrid.setEvent(currentEvent);
		personGrid.setSelectedOnly();
		personGrid.hideFilter();
		personGrid.setSelectionMode(SelectionMode.NONE);

		Button back = new Button("Zurück");
		back.addClickListener(ev -> navigator.navigateTo(((NamedView) event.getOldView()).getViewName()));

		addComponent(eventView, 0, 0, 1, 0);
		addComponent(personGrid, 0, 1, 1, 1);
		addComponent(back, 1, 4);
		setSizeFull();
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}
