package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.EventAltersgruppen;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;

public class EventDetails extends GridLayout implements NamedView {

	public static final String VIEW_NAME = "EventDetails";
	private static final long serialVersionUID = 8290150079638390995L;

	private final EventBusiness eventBusiness;
	private final PersonDao personDao;
	private final GroupDao groupDao;
	private final PflichtenDao pflichtenDao;

	private ClubEvent currentEvent;
	private SingleEventView eventView;
	private PersonGrid personGrid;
	private EventAltersgruppen eventAltersgruppen;

	public EventDetails(PersonDao personDao, GroupDao groupDao, EventBusiness eventBusiness,
			PflichtenDao pflichtenDao) {
		super(3, 5);
		this.eventBusiness = eventBusiness;
		this.personDao = personDao;
		this.groupDao = groupDao;
		this.pflichtenDao = pflichtenDao;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Navigator navigator = event.getNavigator();

		currentEvent = eventBusiness.getCurrent();

		eventView = new SingleEventView();
		eventView.setEvent(currentEvent);

		personGrid = new PersonGrid(groupDao, personDao);
		personGrid.setEvent(currentEvent);
		personGrid.setSelectedOnly();
		personGrid.hideFilter();
		personGrid.setSelectionMode(SelectionMode.NONE);

		eventAltersgruppen = new EventAltersgruppen(pflichtenDao, eventBusiness);

		Button back = new Button("Zurück");
		back.addClickListener(ev -> navigator.navigateTo(((NamedView) event.getOldView()).getViewName()));

		addComponent(eventView, 0, 0);
		addComponent(eventAltersgruppen, 1, 0);
		addComponent(personGrid, 2, 0);
		addComponent(back, 0, 4);
		setSizeFull();
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}
