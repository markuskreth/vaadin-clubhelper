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
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.EventDetails;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent.ClubEventProvider;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.PersonGrid;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.SingleEventView;

public class MainView extends VerticalLayout implements NamedView {

	public static final String VIEW_NAME = "";

	private static final long serialVersionUID = 4831071242146146399L;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private final PersonDao personDao;
	private final GroupDao groupDao;
	private final EventBusiness eventBusiness;
	private final SecurityVerifier securityVerifier;

	private PersonGrid personGrid;
	private CalendarComponent calendar;
	private HeadView head;
	private SingleEventView eventView;
	private HorizontalLayout eventButtonLayout;

	private Navigator navigator;

	private VerticalLayout eastLayout;

	private HorizontalLayout mainLayout;

	public MainView(PersonDao personDao, GroupDao groupDao, EventBusiness eventBusiness,
			SecurityVerifier securityGroupVerifier) {
		this.personDao = personDao;
		this.groupDao = groupDao;
		this.eventBusiness = eventBusiness;
		this.securityVerifier = securityGroupVerifier;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (this.navigator == null) {
			initUI(event);
			LOGGER.info("Loaded UI and started fetch of Events");
		} else {
			if (securityVerifier.isLoggedin()) {
				LOGGER.info("{} already initialized - opening Person View.", getClass().getName());
				ClubEvent current = eventBusiness.getCurrent();
				openPersonViewForEvent(current);
				if (current != null) {
					calendar.setToday(current.getStart());
				}
				head.updateLoggedinPerson();
			} else {
				LOGGER.info("{} already initialized - but not loggedin.", getClass().getName());
				detailClosed();
				head.updateLoggedinPerson();
			}
		}
	}

	public void initUI(ViewChangeEvent event) {

		navigator = event.getNavigator();

		eventView = new SingleEventView(false);
		eventView.setVisible(false);

		personGrid = new PersonGrid(groupDao, personDao);
		personGrid.setCaption("Personen");
		personGrid.setSelectionMode(SelectionMode.MULTI);
		personGrid.onPersonSelect(ev -> personSelectionChange(ev));
		personGrid.setVisible(false);

		Button close = new Button("Schließen", ev -> {
			detailClosed();
		});
		close.setId("person.close");

		Button eventDetails = new Button("Veranstaltung Details", ev -> {
			navigator.navigateTo(EventDetails.VIEW_NAME);
		});
		eventDetails.setId("person.eventDetails");

		eventButtonLayout = new HorizontalLayout();
		eventButtonLayout.setSpacing(true);
		eventButtonLayout.addComponents(close, eventDetails);
		eventButtonLayout.setVisible(false);

		eastLayout = new VerticalLayout();
		eastLayout.addComponents(eventView, personGrid, eventButtonLayout);

		ClubEventProvider dataProvider = new ClubEventProvider();
		calendar = new CalendarComponent(dataProvider);
		calendar.setSizeFull();
		calendar.setId("main.calendar");
		calendar.setHandler(this::onItemClick);

		head = new HeadView(navigator, () -> calendar.getStartDate(), () -> calendar.getEndDate(), dataProvider,
				securityVerifier);
		head.setWidth("100%");
		head.updateMonthText(calendar.getStartDate());

		calendar.add(dateTime -> head.updateMonthText(dateTime));

		mainLayout = new HorizontalLayout(calendar);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(calendar, 2f);

		addComponent(head);
		addComponent(mainLayout);
		setExpandRatio(mainLayout, 1f);
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
	}

	private void personSelectionChange(SelectionEvent<Person> ev) {
		Set<Person> selected = ev.getAllSelectedItems();
		LOGGER.debug("Selection changed to: {}", selected);
		eventBusiness.changePersons(selected);
	}

	private void detailClosed() {
		LOGGER.debug("Closing detail view.");
		personGrid.setVisible(false);
		eventView.setVisible(false);
		eventButtonLayout.setVisible(false);
		mainLayout.removeComponent(eastLayout);
	}

	private void onItemClick(CalendarComponentEvents.ItemClickEvent event) {

		ClubEvent ev = (ClubEvent) event.getCalendarItem();
		if (securityVerifier.isLoggedin()) {
			openPersonViewForEvent(ev);
		} else {
			eventBusiness.setSelected(ev);
			navigator.navigateTo(LoginUI.VIEW_NAME + '/' + ev.getId());
		}
	}

	public void openPersonViewForEvent(ClubEvent ev) {
		LOGGER.debug("Opening detail view for {}", ev);

		eventBusiness.setSelected(null);
		eventView.setEvent(ev);

		personGrid.setEnabled(false);
		personGrid.setEvent(ev);
		personGrid.setEnabled(true);

		personGrid.setVisible(true);
		eventView.setVisible(true);
		eventButtonLayout.setVisible(true);

		mainLayout.addComponent(eastLayout);
		mainLayout.setExpandRatio(eastLayout, 1f);

		eventBusiness.setSelected(ev);
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}
