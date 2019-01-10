package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonEditDialog;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;

public class MainView extends HorizontalLayout implements View {

	public static final String VIEW_NAME = "";

	private static final long serialVersionUID = 4831071242146146399L;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private PersonGrid personGrid;

	private CalendarComponent calendar;

	private Person loggedinPerson;
	private PersonDao personDao;
	private GroupDao groupDao;
	private EventBusiness eventBusiness;
	private Navigator navigator;

	public MainView(PersonDao personDao, GroupDao groupDao, EventBusiness eventBusiness) {

		this.personDao = personDao;
		this.groupDao = groupDao;
		this.eventBusiness = eventBusiness;

	}

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		if (this.navigator == null) {

			navigator = event.getNavigator();

			personGrid = new PersonGrid(groupDao, personDao);
			personGrid.setId("main.person");
			personGrid.setCaption("Personen");
			personGrid.onClosedFunction(() -> detailClosed());
			personGrid.onPersonSelect(ev -> personSelectionChange(ev));
			personGrid.onPersonEdit(p -> onPersonEdit(p));

			calendar = new CalendarComponent();
			calendar.setId("main.calendar");
			calendar.setHandler(this::onItemClick);

			setSizeFull();
			addComponents(calendar);

			setSizeFull();

			ExecutorService exec = Executors.newSingleThreadExecutor();
			exec.execute(() -> {

				final List<ClubEvent> events = eventBusiness.loadEvents();
				LOGGER.info("Loaded events: {}", events);
				final UI ui = calendar.getUI();
				ui.access(() -> {
					calendar.setItems(events);
					ui.push();
				});

			});
			exec.shutdown();
			LOGGER.info("Loaded UI and started fetch of Events");
		} else {
			LOGGER.info("{} already initialized - opening Person View.");
			openPersonViewForEvent(eventBusiness.getCurrent());
			calendar.setToday(eventBusiness.getCurrent().getStart());
		}
	}

	private void onPersonEdit(Person p) {
		PersonEditDialog dlg = new PersonEditDialog(groupDao.listAll(), p, personDao);
		getUI().addWindow(dlg);
	}

	private void personSelectionChange(SelectionEvent<Person> ev) {
		Set<Person> selected = ev.getAllSelectedItems();
		LOGGER.debug("Selection changed to: {}", selected);
		eventBusiness.changePersons(selected);
	}

	private void detailClosed() {
		LOGGER.debug("Closing detail view.");
		removeComponent(personGrid);
	}

	private void onItemClick(CalendarComponentEvents.ItemClickEvent event) {

		loggedinPerson = (Person) getSession().getAttribute(Person.SESSION_LOGIN);
		ClubEvent ev = (ClubEvent) event.getCalendarItem();
		if (loggedinPerson != null) {
			openPersonViewForEvent(ev);
		} else {
			eventBusiness.setSelected(ev);
			navigator.navigateTo(LoginUI.VIEW_NAME + '/' + ev.getId());
		}
	}

	public void openPersonViewForEvent(ClubEvent ev) {
		LOGGER.debug("Opening detail view for {}", ev);

		removeComponent(personGrid);
		addComponent(personGrid);

		eventBusiness.setSelected(null);
		personGrid.setEnabled(false);
		personGrid.setEvent(ev);
		personGrid.setEnabled(true);
		personGrid.setVisible(true);
		eventBusiness.setSelected(ev);
	}

}
