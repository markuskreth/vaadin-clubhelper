package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import org.springframework.context.ApplicationContext;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.PersonBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.EventAltersgruppen;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.ClubhelperMenuBar;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.MenuItemStateFactory;

public class EventDetails extends GridLayout implements View {

	private static final long serialVersionUID = 8290150079638390995L;

	private final EventBusiness eventBusiness;

	private final PersonBusiness personDao;

	private final GroupDao groupDao;

	private final PflichtenDao pflichtenDao;

	private ClubEvent currentEvent;

	private SingleEventView eventView;

	private PersonGrid personGrid;

	private EventAltersgruppen eventAltersgruppen;

	private MenuItemStateFactory stateFactory;

	public EventDetails(ApplicationContext context) {
		super(3, 5);
		this.stateFactory = context.getBean(MenuItemStateFactory.class);
		this.eventBusiness = context.getBean(EventBusiness.class);
		this.personDao = context.getBean(PersonBusiness.class);
		this.groupDao = context.getBean(GroupDao.class);
		this.pflichtenDao = context.getBean(PflichtenDao.class);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		currentEvent = eventBusiness.getCurrent();
		if (eventView == null) {

			ClubhelperMenuBar menubar = new ClubhelperMenuBar(stateFactory.currentState());
//			ClubNavigator navigator = (ClubNavigator) event.getNavigator();

			eventView = new SingleEventView(true);
//			eventView.setCalendarAdapter(calendarAdapter);

			eventView.addDataUpdatedListener(() -> eventBusiness.storeEventType());

			eventAltersgruppen = new EventAltersgruppen(pflichtenDao, eventBusiness);
			eventView.addDataUpdatedListener(eventAltersgruppen);

			personGrid = new PersonGrid(groupDao, personDao);
			personGrid.hideFilter();
			personGrid.setSelectionMode(SelectionMode.NONE);

			addComponent(menubar, 0, 0, 2, 0);
			addComponent(eventView, 0, 1);
			addComponent(eventAltersgruppen, 1, 1);
			addComponent(personGrid, 2, 1);
			setSizeFull();
		}
		else {
			eventAltersgruppen.updateData();
		}
		eventView.setEvent(currentEvent);
		personGrid.setEvent(currentEvent);
	}

}
