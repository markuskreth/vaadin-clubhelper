package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PersonDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.CalendarComponent.ClubEventProvider;

public class MainViewDesktop extends MainView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3293470536470926668L;

	private VerticalLayout eastLayout;

	private HorizontalLayout mainLayout;

	private HorizontalLayout eventButtonLayout;

	private CalendarComponent calendar;

	private DesktopHeadView head;

	private ApplicationContext context;

	public MainViewDesktop(ApplicationContext context, PersonDao personDao, GroupDao groupDao,
			EventBusiness eventBusiness,
			SecurityVerifier securityGroupVerifier) {
		super(personDao, groupDao, eventBusiness, securityGroupVerifier);
		this.context = context;
	}

	@Override
	public void initUI(ViewChangeEvent event) {
		super.initUI(event);

		ClubEventProvider dataProvider = new ClubEventProvider();
		calendar = new CalendarComponent(dataProvider);
		calendar.setSizeFull();
		calendar.setId("main.calendar");
		calendar.setHandler(this::onItemClick);

		head = new DesktopHeadView(context, navigator, component -> calendar.getStartDate(),
				component -> calendar.getEndDate(),
				dataProvider, securityVerifier);
		head.setWidth("100%");
		head.updateMonthText(calendar.getStartDate());

		calendar.add(dateTime -> head.updateMonthText(dateTime));

		mainLayout = new HorizontalLayout(calendar);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(calendar, 2f);

		Button close = new Button("Schließen", ev -> {
			detailClosed();
		});
		close.setId("person.close");

		Button eventDetails = new Button("Veranstaltung Details", ev -> {
			navigator.navigateTo(ClubhelperViews.EventDetails);
		});
		eventDetails.setId("person.eventDetails");

		eventButtonLayout = new HorizontalLayout();
		eventButtonLayout.setSpacing(true);
		eventButtonLayout.addComponents(close, eventDetails);
		eventButtonLayout.setVisible(false);

		eastLayout = new VerticalLayout();
		eastLayout.addComponents(eventView, personGrid, eventButtonLayout);

		addComponent(head);
		addComponent(mainLayout);
		setExpandRatio(mainLayout, 1f);
		setSizeFull();

		reloadEvents();
	}

	public void reloadEvents() {

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(() -> {

			final List<ClubEvent> events = eventBusiness.loadEvents();
			LOGGER.info("Loaded events: {}", events);
			final UI ui = calendar.getUI();
			if (ui != null) {
				ui.access(() -> {
					calendar.setItems(events);
					ui.push();
				});
			}
			else {
				calendar.setItems(events);
			}

		});
		exec.shutdown();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);

		head.updateLoggedinPerson();
		reloadEvents();
	}

	private void onItemClick(CalendarComponentEvents.ItemClickEvent event) {

		ClubEvent ev = (ClubEvent) event.getCalendarItem();
		if (securityVerifier.isLoggedin()) {
			openDetailForEvent(ev);
		}
		else {
			eventBusiness.setSelected(ev);
			navigator.navigateTo(ClubhelperViews.LoginUI.name() + '/' + ev.getId());
		}
	}

	@Override
	public void openDetailForEvent(ClubEvent ev) {
		super.openDetailForEvent(ev);

		mainLayout.addComponent(eastLayout);
		mainLayout.setExpandRatio(eastLayout, 1f);

		if (ev != null) {
			calendar.setToday(ev.getStart());
		}
		eventButtonLayout.setVisible(true);
	}

	@Override
	public void detailClosed() {
		super.detailClosed();
		mainLayout.removeComponent(eastLayout);
		eventButtonLayout.setVisible(false);
	}

}
